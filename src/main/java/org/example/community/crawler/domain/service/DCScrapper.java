package org.example.community.crawler.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;

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
    }

    private void traverseBoard() throws IOException {
        Document doc = Jsoup.connect("https://gall.dcinside.com/board/lists/?id=neostock&page=1").get();
        Elements gallDateList = doc.select(".gall_date");
        Elements gallNumList = doc.select(".gall_num");
//        gallDateList.stream().collect(Collectors.toList()).
        log.info("{}", gallDateList);
        log.info("{}", gallNumList);
    }
}
