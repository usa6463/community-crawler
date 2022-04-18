package org.example.community.crawler.domain.service;

import org.example.community.crawler.domain.entity.DCContent;
import org.example.community.crawler.domain.entity.DCInnerReply;
import org.example.community.crawler.domain.entity.DCReply;
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = "command.line.runner.enabled=false")
@ActiveProfiles("test")
class DCScrapperTest {

    @Test
    void getContentTest(@Autowired DCScrapper dcScrapper, @Autowired ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:dc-content-sample.html");
        Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8);
        String postSampleHtml = FileCopyUtils.copyToString(reader);

        Document doc = Jsoup.parse(postSampleHtml);
        String url = "https://gall.dcinside.com/board/view/?id=rlike&no=406576&page=1";
        DCContent actual = dcScrapper.getContent(url, doc);

        DCContent expected = DCContent.builder()
                .contentNum(406576)
                .title("ㄷㅈ) 픽다트 지형 만드는데 아이디어 좀")
                .content("+7 long sword of draining인 Throatcutter, 상대방 체력이 낮을 때 즉사 확률 있는 장검 픽다트 " +
                        "지형을 만들려고 하는데 아이디어 뭐 없나 " +
                        "3~8층에 꽂는데다가 초반용 픽다트라 어렵게 할 필요는 없는데 테마가 생각이 안나네")
                .url("https://gall.dcinside.com/board/view/?id=rlike&no=406576&page=1")
                .nickname("ㅇㅇ")
                .ip("121.136")
                .dt("2022.04.09 19:05:06")
                .viewCount("61")
                .recommendCount("0")
                .commentCount("6")
                .replyList(new ArrayList<>(
                                Arrays.asList(
                                        new DCReply("1409573", "ㅇㅇ", "39.125", "04.09 19:07:20", "유령계열 몹들이 지키고 있기? 저거에 뒈진 놈들이라는 설정", new ArrayList<DCInnerReply>(
                                                Arrays.asList(new DCInnerReply("ㅇㅇ", "(121.136)", "04.09 19:08:55", "흠 그건 모르그 단검 쪽에 더 어울릴 거 같기도하고. 이제보니까 모르그 단검이 지형이 없네"))
                                        )),
                                        new DCReply("1409577", "", "", "04.09 19:18:44", "그걸 들고 있는 몹과 딸피 몹들이 갇혀있는 처형장 컨셉?", new ArrayList<>(
                                                Arrays.asList(new DCInnerReply("ㅇㅇ", "(39.125)", "04.09 19:32:45", "이야 이거 좋다 ㅋㅋㅋㅋㅋ"))
                                        )),
                                        new DCReply("1409580", "", "", "04.09 19:24:12", "뼉다구 시체타일 늘어놓고 키쿠제단 박기ㅋㅋㅋ", new ArrayList<>()),
                                        new DCReply("0", "", "", "", "", new ArrayList<>()),
                                        new DCReply("1409581", "ㅇㅇ", "49.163", "04.09 19:24:35", "픽다트 앞에 인간형 시체 한구 놓아두고 주변에 딸피+드레인 걸린 OOD랑 시체들 널부러진건 어떰?", new ArrayList<>())
                                )
                        )
                )
                .build();

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

}