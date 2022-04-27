package org.example.community.crawler.domain.service;

import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.entity.DCPostMeta;
import org.example.community.crawler.repository.ESRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "command.line.runner.enabled=false")
@ActiveProfiles("test")
class DCMinorScrapperTest {

    @Test
    void getDcPosts(@Autowired AppConfiguration appConfiguration, @Autowired ESRepository esRepository, @Autowired ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:dc-minor-content-sample.html");
        Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8);
        String boardSampleHtml = FileCopyUtils.copyToString(reader);

        Document doc = Jsoup.parse(boardSampleHtml);

        DCMinorScrapper dcMinorScrapper = new DCMinorScrapper(appConfiguration, esRepository);
        List<DCPostMeta> actual = dcMinorScrapper.getDcPosts(doc);

        List<DCPostMeta> expected = new ArrayList<DCPostMeta>(
                Arrays.asList(new DCPostMeta("04.26", "279087", "44", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279087&page=8"))
        );

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

    }
}