package org.example.community.crawler.domain.service;

import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.entity.Content;
import org.example.community.crawler.domain.entity.InnerReply;
import org.example.community.crawler.domain.entity.Reply;
import org.example.community.crawler.repository.ESRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "command.line.runner.enabled=false")
@ActiveProfiles("test")
class DCScrapperTest {

    @Test
    void getContentTest(@Autowired AppConfiguration appConfiguration, @Autowired ESRepository esRepository, @Autowired ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:dc-content-sample.html");
        Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8);
        String postSampleHtml = FileCopyUtils.copyToString(reader);

        Document doc = Jsoup.parse(postSampleHtml);
        String url = "https://gall.dcinside.com/board/view/?id=rlike&no=406576&page=1";
        DCScrapper dcScrapper = new DCScrapper("https://gall.dcinside.com");
        final String WEB_DRIVER_ID = "webdriver.chrome.driver";
        WebDriver driver = CommonScrapperFunction.getWebDriver(appConfiguration, WEB_DRIVER_ID);
        Content actual = dcScrapper.getContent(url, doc, driver);

        Content expected = Content.builder()
                .contentNum(406576)
                .title("??????) ????????? ?????? ???????????? ???????????? ???")
                .content("+7 long sword of draining??? Throatcutter, ????????? ????????? ?????? ??? ?????? ?????? ?????? ?????? ????????? " +
                        "????????? ???????????? ????????? ???????????? ??? ?????? " +
                        "3~8?????? ??????????????? ????????? ???????????? ????????? ??? ????????? ????????? ????????? ????????? ?????????")
                .url("https://gall.dcinside.com/board/view/?id=rlike&no=406576&page=1")
                .nickname("??????")
                .ip("121.136")
                .dt("2022.04.09 19:05:06")
                .viewCount("61")
                .recommendCount("0")
                .commentCount("6")
                .replyList(new ArrayList<>(
                                Arrays.asList(
                                        new Reply("1409573", "??????", "39.125", "04.09 19:07:20", "???????????? ????????? ????????? ??????? ????????? ?????? ??????????????? ??????", new ArrayList<InnerReply>(
                                                Arrays.asList(new InnerReply("??????", "121.136", "04.09 19:08:55", "??? ?????? ????????? ?????? ?????? ??? ????????? ??? ???????????????. ??????????????? ????????? ????????? ????????? ??????"))
                                        )),
                                        new Reply("1409577", "?????????", null, "04.09 19:18:44", "?????? ?????? ?????? ?????? ?????? ????????? ???????????? ????????? ???????", new ArrayList<>(
                                                Arrays.asList(new InnerReply("??????", "39.125", "04.09 19:32:45", "?????? ?????? ?????? ???????????????"))
                                        )),
                                        new Reply("1409580", "??????", null, "04.09 19:24:12", "????????? ???????????? ???????????? ???????????? ???????????????", new ArrayList<>()),
                                        new Reply("1409581", "??????", "49.163", "04.09 19:24:35", "????????? ?????? ????????? ?????? ?????? ???????????? ????????? ??????+????????? ?????? OOD??? ????????? ??????????????? ???????", new ArrayList<>())
                                )
                        )
                )
                .build();

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

}