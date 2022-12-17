package org.example.community.crawler.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.entity.Content;
import org.example.community.crawler.domain.entity.PostMeta;
import org.example.community.crawler.repository.ESRepository;
import org.example.community.crawler.utils.ScrapperType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Future;

public interface Scrapper {

    @Async
    Future<String> getCotentAndSave(ESRepository esRepository, WebDriver driver, String url) throws IOException;

    List<PostMeta> traverseBoard(LocalDate targetDate, String boardBaseUrl) throws IOException, InterruptedException;

    Content getContent(String url, Document doc, WebDriver driver);

    ScrapperType getScrapperType();
    String getDomain();

}