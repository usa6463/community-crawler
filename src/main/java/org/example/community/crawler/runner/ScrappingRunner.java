package org.example.community.crawler.runner;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.service.DCMinorScrapper;
import org.example.community.crawler.domain.service.DCScrapper;
import org.example.community.crawler.domain.service.Scrapper;
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

    @Autowired
    public ScrappingRunner(AppConfiguration appConfiguration, ESRepository esRepository) {
        this.appConfiguration = appConfiguration;
        this.esRepository = esRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Scrapper scrapper = null;
        String baseUrl = appConfiguration.getBoardBaseUrl();
        if (baseUrl.startsWith("https://gall.dcinside.com/mgallery/board/lists/?id=")) {
            scrapper = new DCMinorScrapper(appConfiguration, esRepository);
        } else if (baseUrl.startsWith("https://gall.dcinside.com/board/lists/?id=")) {
            scrapper = new DCScrapper(appConfiguration, esRepository);
        } else {
            throw new IllegalArgumentException("Not Supported Board URL");
        }

        scrapper.scrap();
    }
}
