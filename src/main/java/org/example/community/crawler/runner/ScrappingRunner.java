package org.example.community.crawler.runner;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.service.Scrapper;
import org.example.community.crawler.domain.service.ScrapperFactory;
import org.example.community.crawler.domain.service.TestService;
import org.example.community.crawler.repository.ESRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("!test")
public class ScrappingRunner implements CommandLineRunner {

    private final AppConfiguration appConfiguration;
    private final ESRepository esRepository;
    private final TestService testService;

    @Autowired
    public ScrappingRunner(AppConfiguration appConfiguration, ESRepository esRepository, TestService testService) {
        this.appConfiguration = appConfiguration;
        this.esRepository = esRepository;
        this.testService = testService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scrapper scrapper = null;
        String baseUrl = appConfiguration.getBoardBaseUrl();
        scrapper = ScrapperFactory.getScrapper(baseUrl);

        for (int i = 0; i < 20; i++) {
            testService.test();
        }

        scrapper.scrap(appConfiguration, esRepository);
    }
}
