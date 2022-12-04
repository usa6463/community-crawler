package org.example.community.crawler.domain.service;

import org.example.community.crawler.utils.ScrapperType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScrapperFactory {

    private final Map<ScrapperType, Scrapper> ScrapperMap = new HashMap<>();

    public ScrapperFactory(List<Scrapper> ScrapperServices) {
        ScrapperServices.forEach(s -> ScrapperMap.put(s.getScrapperType(), s));
    }

    public Scrapper getScrapperService(String baseUrl) {
        ScrapperType scrapperType;
        if (baseUrl.startsWith("https://gall.dcinside.com/mgallery/board/lists?id=")) {
            scrapperType = ScrapperType.DC_MINOR;
        } else if (baseUrl.startsWith("https://gall.dcinside.com/board/lists?id=")) {
            scrapperType = ScrapperType.DC;
        } else {
            throw new IllegalArgumentException("Not Supported Board URL");
        }
        return ScrapperMap.get(scrapperType);
    }
}