package org.example.community.crawler.domain.service;

import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.repository.ESRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.Assertions;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "command.line.runner.enabled=false")
@ActiveProfiles("test")
class ScrapperFactoryTest {

    @Test
    void baseUrlDCMinorTest(@Autowired ScrapperFactory scrapperFactory) {
        Scrapper scrapper = scrapperFactory.getScrapperService("https://gall.dcinside.com/mgallery/board/lists?id=mf");

        Assertions.assertTrue(scrapper instanceof DCMinorScrapper);
    }
}