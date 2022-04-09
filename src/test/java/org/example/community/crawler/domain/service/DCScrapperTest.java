package org.example.community.crawler.domain.service;

import org.example.community.crawler.domain.entity.DCContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "command.line.runner.enabled=false")
@ActiveProfiles("test")
class DCScrapperTest {

    @Test
    void getContentTest(@Autowired DCScrapper dcScrapper, @Autowired ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:dc-content-sample.html");
        Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8);
        String postSampleHtml = FileCopyUtils.copyToString(reader);
        System.out.println(postSampleHtml);

//        Document doc = Jsoup.parse(htmlSample);
//        System.out.println(doc.html());
//        DCContent result = dcScrapper.getContent(url, doc);
//
//        DCContent answer = DCContent.builder()
//                .content("abc")
//                .build();
//
//        assertEquals(result, answer);
    }

}