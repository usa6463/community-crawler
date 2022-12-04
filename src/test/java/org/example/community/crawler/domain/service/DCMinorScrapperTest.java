package org.example.community.crawler.domain.service;

import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.entity.*;
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
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "command.line.runner.enabled=false")
@ActiveProfiles("test")
class DCMinorScrapperTest {

    @Test
    void getDcPosts(@Autowired AppConfiguration appConfiguration, @Autowired ESRepository esRepository, @Autowired ResourceLoader resourceLoader, @Autowired DCMinorScrapper dcMinorScrapper) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:dc-minor-board-sample.html");
        Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8);
        String boardSampleHtml = FileCopyUtils.copyToString(reader);

        Document doc = Jsoup.parse(boardSampleHtml);
        List<PostMeta> actual = dcMinorScrapper.getDcPosts(doc);

        List<DCMinorPostMeta> expected = new ArrayList<DCMinorPostMeta>(
                Arrays.asList(
                        new DCMinorPostMeta("2022-04-26 09:18:49", "279087", "44", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279087&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 09:15:37", "279086", "94", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279086&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 09:01:49", "279085", "90", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279085&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 09:00:47", "279084", "186", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279084&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 08:57:04", "279083", "86", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279083&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 08:50:33", "279082", "179", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279082&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 08:48:52", "279081", "66", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279081&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 08:43:51", "279080", "35", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279080&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 08:06:55", "279079", "186", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279079&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 05:11:06", "279078", "132", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279078&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 04:59:35", "279077", "118", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279077&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 03:40:43", "279076", "141", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279076&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 03:30:21", "279075", "180", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279075&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 03:08:14", "279074", "285", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279074&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 03:06:21", "279073", "84", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279073&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 03:02:01", "279072", "68", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279072&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 02:52:30", "279071", "44", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279071&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 02:09:36", "279069", "362", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279069&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 01:56:07", "279067", "27", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279067&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 01:55:45", "279066", "53", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279066&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 01:41:03", "279065", "107", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279065&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 01:17:09", "279064", "205", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279064&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 01:04:08", "279063", "368", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279063&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 01:02:29", "279062", "128", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279062&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 00:45:18", "279058", "238", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279058&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 00:41:36", "279057", "142", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279057&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 00:41:10", "279056", "264", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279056&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 00:32:08", "279055", "127", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279055&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 00:27:26", "279054", "176", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279054&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 00:27:11", "279053", "32", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279053&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 00:25:58", "279052", "39", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279052&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 00:19:53", "279051", "146", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279051&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-26 00:03:22", "279050", "159", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279050&page=8", "질문"),
                        new DCMinorPostMeta("2022-04-26 00:02:36", "279049", "82", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279049&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:56:02", "279048", "1436", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279048&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:48:09", "279047", "288", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279047&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:35:57", "279046", "160", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279046&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:35:25", "279045", "78", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279045&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:26:07", "279044", "223", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279044&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:19:42", "279043", "300", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279043&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:18:06", "279042", "161", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279042&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:15:06", "279041", "192", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279041&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:11:26", "279040", "167", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279040&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:10:55", "279039", "103", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279039&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:08:43", "279038", "97", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279038&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:08:12", "279037", "252", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279037&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 23:02:45", "279036", "124", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279036&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 22:59:11", "279035", "229", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279035&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 22:52:02", "279034", "99", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279034&page=8", "일반"),
                        new DCMinorPostMeta("2022-04-25 22:50:45", "279033", "178", "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279033&page=8", "일반"))
        );

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

    }

    @Test
    void getContent(@Autowired AppConfiguration appConfiguration, @Autowired ESRepository esRepository, @Autowired ResourceLoader resourceLoader, @Autowired DCMinorScrapper dcMinorScrapper) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:dc-minor-content-sample.html");
        Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8);
        String boardSampleHtml = FileCopyUtils.copyToString(reader);

        Document doc = Jsoup.parse(boardSampleHtml);
        String url = "https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279772&page=2";

        final String WEB_DRIVER_ID = "webdriver.chrome.driver";
        WebDriver driver = CommonScrapperFunction.getWebDriver(appConfiguration, WEB_DRIVER_ID);
        Content actual = dcMinorScrapper.getContent(url, doc, driver);

        Content expected = Content.builder()
                .contentNum(279772)
                .title("와이드랑 테이퍼드 사이즈 똑같이 사도 댐??")
                .content("테이퍼드 27입는데 와이드도 똑같이 가면 되나... - dc official App")
                .url("https://gall.dcinside.com/mgallery/board/view/?id=mf&no=279772&page=2")
                .nickname("ㅇㅇ")
                .ip("61.102")
                .dt("2022.04.29 14:55:35")
                .viewCount("66")
                .recommendCount("0")
                .commentCount("5")
                .replyList(new ArrayList<>(
                                Arrays.asList(
                                        new Reply("904436", "ㅇㅇ", null, "04.29 14:57:34", "허벅지랑 허리사이즈를 비교해", new ArrayList<InnerReply>()),
                                        new Reply("904444", "ㅇㅇ", "121.177", "04.29 14:59:07", "니가 입는 바지랑 실측표를 보면 감이 오지 않을까", new ArrayList<>(
                                                Arrays.asList(new InnerReply("ㅇㅇ", "61.102", "04.29 16:28:19", "ㅇㅋㅇㅋ - dc App"))
                                        )),
                                        new Reply("904463", "ㅇㅇ", "1.216", "04.29 15:37:27", "실측표 비교", new ArrayList<>(
                                                Arrays.asList(new InnerReply("ㅇㅇ", "61.102", "04.29 16:28:21", "ㅇㅋㅇㅋ - dc App"))
                                        ))
                                )
                        )
                )
                .build();

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

    }
}