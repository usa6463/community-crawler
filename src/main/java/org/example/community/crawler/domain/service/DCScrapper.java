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
    /**
     * DC 인사이드 커뮤니티 사이트의 특정날짜 게시글을 스크래핑 하여 스토리지에 저장
     */
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
        int boardPage = 1;
        Document doc = Jsoup.connect(String.format("https://gall.dcinside.com/board/lists/?id=neostock&page=%d", boardPage)).get();

        Elements gallDateList = doc.select(".gall_date");
        Elements gallNumList = doc.select(".gall_num");
        Elements gallCountList = doc.select(".gall_count");
        Elements gallUrlList = doc.select(".gall_tit > a:not(.reply_numbox)");

        int minListSize = Collections.min(Arrays.asList(gallDateList.size(),
                gallNumList.size(), gallCountList.size(), gallUrlList.size()));

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
