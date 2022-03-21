package org.example.community.crawler.runner;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.domain.service.DCScrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScrappingRunner implements CommandLineRunner {

    private final DCScrapper dcScrapper;

    @Autowired
    public ScrappingRunner(DCScrapper dcScrapper) {
        this.dcScrapper = dcScrapper;
    }


    @Override
    public void run(String... args) throws Exception {
        dcScrapper.scrap();


    }
}
