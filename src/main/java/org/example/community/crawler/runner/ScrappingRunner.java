package org.example.community.crawler.runner;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.service.DCScrapper;
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
        DCScrapper dcScrapper = new DCScrapper(appConfiguration, esRepository);
        dcScrapper.scrap();
    }
}
