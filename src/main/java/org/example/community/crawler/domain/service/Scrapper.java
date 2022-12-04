package org.example.community.crawler.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.entity.Content;
import org.example.community.crawler.domain.entity.PostMeta;
import org.example.community.crawler.repository.ESRepository;
import org.example.community.crawler.utils.ScrapperType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service
public abstract class Scrapper {
    final static String WEB_DRIVER_ID = "webdriver.chrome.driver";

    /**
     * 커뮤니티 사이트의 특정날짜 게시글을 스크래핑 하여 스토리지에 저장
     */
    public void scrap(AppConfiguration appConfiguration, ESRepository esRepository) throws IOException, InterruptedException {
        WebDriver driver = CommonScrapperFunction.getWebDriver(appConfiguration, WEB_DRIVER_ID);
        log.info("DOMAIN TEST: {}", getDomain());

        log.info("{} start", this.getClass().getSimpleName());

        String targetDateStr = appConfiguration.getTargetDate();
        LocalDate targetDate = LocalDate.parse(targetDateStr);


        List<PostMeta> targetPostList = traverseBoard(targetDate, appConfiguration.getBoardBaseUrl());

        scrapPosts(targetPostList, esRepository, driver);

        // TODO rate limiter 적용해서 요청 쓰로틀링 필요
    }


    /**
     * 각 게시글 대상으로 스크래핑
     *
     * @param targetPostList 스크래핑 대상 게시글 정보 리스트
     */
    void scrapPosts(List<PostMeta> targetPostList, ESRepository esRepository, WebDriver driver) {
        targetPostList.forEach(post -> {
            String url = getDomain() + post.getUrl();
            log.debug("target post url: {}", url);

            try { //TODO try catch 대신 throw 하는걸로 통일할 필요 있을듯
                getCotentAndSave(esRepository, driver, url);
                Thread.sleep(1000); // TODO rate limiter로 변경하고 InterruptedException 제거
            } catch (IOException | InterruptedException e) {
                log.error("{}", e.getMessage());
            }
        });
    }

    @Async
    public void getCotentAndSave(ESRepository esRepository, WebDriver driver, String url) throws IOException {
        try {
            long threadId = Thread.currentThread().getId();
            log.info("Thread # " + threadId + " is doing this task");
            Thread.sleep(8000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Document doc = Jsoup.connect(url).get();
        Content content = getContent(url, doc, driver);
        log.info("content : {}", content);

        // ES에 저장
        esRepository.save(content);
    }


    abstract List<PostMeta> traverseBoard(LocalDate targetDate, String boardBaseUrl) throws IOException, InterruptedException;

    abstract public Content getContent(String url, Document doc, WebDriver driver);

    abstract public ScrapperType getScrapperType();
    abstract public String getDomain();

}