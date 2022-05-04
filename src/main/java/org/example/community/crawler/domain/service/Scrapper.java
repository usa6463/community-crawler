package org.example.community.crawler.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.entity.DCPostMeta;
import org.example.community.crawler.repository.ESRepository;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public abstract class Scrapper {
    final static String WEB_DRIVER_ID = "webdriver.chrome.driver";

    /**
     * 커뮤니티 사이트의 특정날짜 게시글을 스크래핑 하여 스토리지에 저장
     */
    public void scrap(AppConfiguration appConfiguration, ESRepository esRepository){
        WebDriver driver = CommonScrapperFunction.getWebDriver(appConfiguration, WEB_DRIVER_ID);

        log.info("DCScrapper start"); // TODO 실제 실행 클래스명이 들어가도록 수정

        String targetDateStr = appConfiguration.getTargetDate();
        LocalDate targetDate = LocalDate.parse(targetDateStr);

        try {
            List<DCPostMeta> targetPostList = traverseBoard(targetDate, appConfiguration.getBoardBaseUrl());

            scrapPosts(targetPostList, esRepository, driver);

            // TODO rate limiter 적용해서 요청 쓰로틀링 필요
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    abstract List<DCPostMeta> traverseBoard(LocalDate targetDate, String boardBaseUrl) throws IOException, InterruptedException;
    abstract void scrapPosts(List<DCPostMeta> targetPostList, ESRepository esRepository, WebDriver driver);
}