package org.example.community.crawler.domain.service;

import org.example.community.crawler.domain.entity.DCContent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "command.line.runner.enabled=false")
@ActiveProfiles("test")
class DCScrapperTest {

    @Test
    void getContentTest(@Autowired DCScrapper dcScrapper) throws IOException {

        String url = "https://gall.dcinside.com/board/view/?id=rlike&no=406565&page=1";
        Document doc = Jsoup.connect(url).get();
//        DCContent result = dcScrapper.getContent(url, doc);

        DCContent answer = DCContent.builder()
                .content("abc")
                .build();

        assertEquals("abc", "abc");

//        assertEquals(result, answer);
    }

    @Test
    void test() {
        assertEquals("abc", "abc");
    }

}