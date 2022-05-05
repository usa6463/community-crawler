package org.example.community.crawler.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.entity.Content;
import org.example.community.crawler.domain.entity.PostMeta;
import org.example.community.crawler.repository.ESRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public abstract class Scrapper {
    final static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    String domain;

    Scrapper(String domain) {
        this.domain = domain;
    }

    /**
     * 커뮤니티 사이트의 특정날짜 게시글을 스크래핑 하여 스토리지에 저장
     */
    public void scrap(AppConfiguration appConfiguration, ESRepository esRepository) {
        WebDriver driver = CommonScrapperFunction.getWebDriver(appConfiguration, WEB_DRIVER_ID);
        log.info("DOMAIN TEST: {}", domain);

        log.info("DCScrapper start"); // TODO 실제 실행 클래스명이 들어가도록 수정

        String targetDateStr = appConfiguration.getTargetDate();
        LocalDate targetDate = LocalDate.parse(targetDateStr);

        try {
            List<PostMeta> targetPostList = traverseBoard(targetDate, appConfiguration.getBoardBaseUrl());

            scrapPosts(targetPostList, esRepository, driver);

            // TODO rate limiter 적용해서 요청 쓰로틀링 필요
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }


    /**
     * 각 게시글 대상으로 스크래핑
     *
     * @param targetPostList 스크래핑 대상 게시글 정보 리스트
     */
    void scrapPosts(List<PostMeta> targetPostList, ESRepository esRepository, WebDriver driver) {
        targetPostList.forEach(post -> {
            String url = domain + post.getUrl();
            log.debug("target post url: {}", url);

            try { //TODO try catch 대신 throw 하는걸로 통일할 필요 있을듯
                Document doc = Jsoup.connect(url).get();
                Content content = getContent(url, doc, driver);
                log.info("dcContent : {}", content);

                // ES에 저장
                esRepository.save(content);
                Thread.sleep(1000); // TODO rate limiter로 변경하고 InterruptedException 제거
            } catch (IOException | InterruptedException e) {
                log.error("{}", e.getMessage());
            }
        });
    }

    abstract List<PostMeta> traverseBoard(LocalDate targetDate, String boardBaseUrl) throws IOException, InterruptedException;

    abstract public Content getContent(String url, Document doc, WebDriver driver);

}