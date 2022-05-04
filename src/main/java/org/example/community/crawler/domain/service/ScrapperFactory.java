package org.example.community.crawler.domain.service;

import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.repository.ESRepository;

public class ScrapperFactory {
    public static Scrapper getScrapper(String baseUrl) {
        Scrapper scrapper;
        if (baseUrl.startsWith("https://gall.dcinside.com/mgallery/board/lists?id=")) {
            scrapper = new DCMinorScrapper();
        } else if (baseUrl.startsWith("https://gall.dcinside.com/board/lists?id=")) {
            scrapper = new DCScrapper();
        } else {
            throw new IllegalArgumentException("Not Supported Board URL");
        }
        return scrapper;
    }
}