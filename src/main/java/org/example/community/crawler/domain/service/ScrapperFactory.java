package org.example.community.crawler.domain.service;

import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.repository.ESRepository;

public class ScrapperFactory {
    public static Scrapper getScrapper(String baseUrl, AppConfiguration appConfiguration, ESRepository esRepository) {
        Scrapper scrapper;
        if (baseUrl.startsWith("https://gall.dcinside.com/mgallery/board/lists?id=")) {
            scrapper = new DCMinorScrapper(appConfiguration, esRepository);
        } else if (baseUrl.startsWith("https://gall.dcinside.com/board/lists?id=")) {
            scrapper = new DCScrapper(appConfiguration, esRepository);
        } else {
            throw new IllegalArgumentException("Not Supported Board URL");
        }
        return scrapper;
    }
}