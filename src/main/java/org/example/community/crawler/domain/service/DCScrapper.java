package org.example.community.crawler.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.domain.entity.DCBoard;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class DCScrapper {
    public void scrap() {
        log.info("DCScrapper start");

        try {
            traverseBoard();
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
        // 게시판 순회. target_date 이전 날짜 나오기 시작하면 중단
        // 게시판 순회하면서 target_date 날짜 게시글 링크를 list에 append

        // 각 게시글 대상으로 스크래핑 및 ES 저장
        // rate limiter 적용해서 요청 쓰로틀링 필요
    }

    private void traverseBoard() throws IOException {
        Document doc = Jsoup.connect("https://gall.dcinside.com/board/lists/?id=neostock&page=1").get();

        Elements gallDateList = doc.select(".gall_date");
        Elements gallNumList = doc.select(".gall_num");
        Elements gallCountList = doc.select(".gall_count");
        Elements gallUrlList = doc.select(".gall_tit > a");

        int minListSize = Collections.min(Arrays.asList(gallDateList.size(),
                gallNumList.size(), gallCountList.size(), gallUrlList.size()));

        log.info("gallUrlList : {}", gallUrlList);
        log.info("minListSIze : {}", minListSize);

        List<DCBoard> list = IntStream
                .range(0, minListSize)
                .mapToObj(i -> new DCBoard(
                        gallDateList.get(i).ownText(),
                        gallNumList.get(i).ownText(),
                        gallCountList.get(i).ownText(),
                        gallUrlList.get(i).attr("href")
                ))
                .filter(obj -> !obj.getCount().equals("-")) // 설문 필터링
                .filter(obj -> !obj.getNum().equals("공지")) // 공지 필터링
                .collect(Collectors.toList());

        log.info("{}", list);
    }
}
