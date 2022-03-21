package org.example.community.crawler.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DCScrapper {
    public void scrap() {
        log.info("DCScrapper start");

        // 게시판 순회. target_date 이전 날짜 나오기 시작하면 중단
        // 게시판 순회하면서 target_date 날짜 게시글 링크를 list에 append
    }
}
