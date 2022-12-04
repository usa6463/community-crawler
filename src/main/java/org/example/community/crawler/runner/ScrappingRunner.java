package org.example.community.crawler.runner;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.config.SpringAsyncConfig;
import org.example.community.crawler.domain.entity.PostMeta;
import org.example.community.crawler.domain.service.CommonScrapperFunction;
import org.example.community.crawler.domain.service.Scrapper;
import org.example.community.crawler.domain.service.ScrapperFactory;
import org.example.community.crawler.repository.ESRepository;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@Profile("!test")
public class ScrappingRunner implements CommandLineRunner {

    private final AppConfiguration appConfiguration;
    private final ESRepository esRepository;
    private final ScrapperFactory scrapperFactory;
    private final SpringAsyncConfig springAsyncConfig;

    final static String WEB_DRIVER_ID = "webdriver.chrome.driver";

    @Autowired
    public ScrappingRunner(AppConfiguration appConfiguration, ESRepository esRepository, ScrapperFactory scrapperFactory, SpringAsyncConfig springAsyncConfig) {
        this.appConfiguration = appConfiguration;
        this.esRepository = esRepository;
        this.scrapperFactory = scrapperFactory;
        this.springAsyncConfig = springAsyncConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        Scrapper scrapper = null;
        String baseUrl = appConfiguration.getBoardBaseUrl();

        scrapper = scrapperFactory.getScrapperService(baseUrl);

        WebDriver driver = CommonScrapperFunction.getWebDriver(appConfiguration, WEB_DRIVER_ID);
        String targetDateStr = appConfiguration.getTargetDate();
        LocalDate targetDate = LocalDate.parse(targetDateStr);
        List<PostMeta> targetPostList = scrapper.traverseBoard(targetDate, appConfiguration.getBoardBaseUrl());

        Scrapper finalScrapper = scrapper;
        targetPostList.forEach(post -> {
            String url = finalScrapper.getDomain() + post.getUrl();
            log.debug("target post url: {}", url);

            try { //TODO try catch 대신 throw 하는걸로 통일할 필요 있을듯
                finalScrapper.getCotentAndSave(esRepository, driver, url);
                Thread.sleep(2000); // TODO rate limiter로 변경하고 InterruptedException 제거
            } catch (IOException | InterruptedException e) {
                log.error("{}", e.getMessage());
            }
        });
    }
}
