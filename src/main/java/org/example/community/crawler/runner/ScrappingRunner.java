package org.example.community.crawler.runner;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.service.Scrapper;
import org.example.community.crawler.domain.service.ScrapperFactory;
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
    private final ScrapperFactory scrapperFactory;

    @Autowired
    public ScrappingRunner(AppConfiguration appConfiguration, ESRepository esRepository, ScrapperFactory scrapperFactory) {
        this.appConfiguration = appConfiguration;
        this.esRepository = esRepository;
        this.scrapperFactory = scrapperFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        Scrapper scrapper = null;
        String baseUrl = appConfiguration.getBoardBaseUrl();

        scrapper = scrapperFactory.getScrapperService(baseUrl);
        scrapper.scrap(appConfiguration, esRepository);
    }
}
