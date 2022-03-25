package org.example.community.crawler.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.example.community.crawler.domain.entity.DCPost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class DCScrapper {

    /**
     * DC 게시판에서 확인할 수 있는 게시글 등록일자 포맷
     */
    public static final String DC_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 게시판 번호 붙이는 포맷
     */
    public static final String DC_BOARD_PAGE_URL_FORMAT = "%s&page=%d";
    private AppConfiguration appConfiguration;

    @Autowired
    public DCScrapper(AppConfiguration appConfiguration) {
        this.appConfiguration = appConfiguration;
    }

    /**
     * DC 인사이드 커뮤니티 사이트의 특정날짜 게시글을 스크래핑 하여 스토리지에 저장
     */
    public void scrap() {
        log.info("DCScrapper start");

        String targetDateStr = appConfiguration.getTargetDate();
        LocalDate targetDate = LocalDate.parse(targetDateStr);

        try {
            traverseBoard(targetDate, appConfiguration.getBoardBaseUrl());
            // 각 게시글 대상으로 스크래핑 및 ES 저장
            // rate limiter 적용해서 요청 쓰로틀링 필요
        } catch (Exception e) {
            log.error("{}", e.getMessage());
        }
    }

    /**
     * 게시판 순회. target_date 이전 날짜 나오기 시작하면 중단
     *
     * @param targetDate 수집대상 게시글 작성일
     * @param boardBaseUrl 순회 대상 게시판 URL(페이지 번호 제외)
     * @return 게시판 순회하면서 target_date 날짜 게시글 링크를 list에 append 후 반환
     * @throws IOException
     */
    private List<DCPost> traverseBoard(LocalDate targetDate, String boardBaseUrl) throws IOException, InterruptedException {
        int boardPage = 1;
        Boolean targetDateFlag = true;
        List<DCPost> result = new ArrayList<>();
        while (targetDateFlag) {
            String boardUrl = String.format(DC_BOARD_PAGE_URL_FORMAT, boardBaseUrl, boardPage);
            List<DCPost> list = getDcPosts(boardUrl);
            long targetDateBeforePostCount = list.stream()
                    .filter(post -> LocalDate.parse(post.getDate(), DateTimeFormatter.ofPattern(DC_DATETIME_FORMAT))
                            .isBefore(targetDate))
                    .count();
            if (targetDateBeforePostCount > 0) {
                targetDateFlag = false;
            }
            log.info("targetDateBeforePostCount : {}", targetDateBeforePostCount);
            log.info("{}", list);
            result.addAll(list);
            boardPage = boardPage + 1;
            Thread.sleep(1000); // TODO rate limiter로 변경하고 InterruptedException 제거
        }
        return result;
    }

    /**
     * DC 게시판을 파싱하여 게시글 정보 수집
     *
     * @param boardUrl     스크래핑할 페이지 URL
     * @return 게시글 정보 리스트
     * @throws IOException
     */
    private List<DCPost> getDcPosts(String boardUrl) throws IOException {
        log.info("get Post info from {}", boardUrl);
        Document doc = Jsoup.connect(boardUrl).get();

        Elements gallDateList = doc.select(".gall_date");
        Elements gallNumList = doc.select(".gall_num");
        Elements gallCountList = doc.select(".gall_count");
        Elements gallUrlList = doc.select(".gall_tit > a:not(.reply_numbox)");

        int minListSize = Collections.min(Arrays.asList(gallDateList.size(),
                gallNumList.size(), gallCountList.size(), gallUrlList.size()));

        List<DCPost> list = IntStream
                .range(0, minListSize)
                .mapToObj(i -> new DCPost(
                        gallDateList.get(i).attr("title"),
                        gallNumList.get(i).ownText(),
                        gallCountList.get(i).ownText(),
                        gallUrlList.get(i).attr("href")
                ))
                .filter(obj -> obj.getCount()
                        .chars()
                        .allMatch(Character::isDigit)) // 조회수가 "-" 인 경우 필터링
                .filter(obj -> obj.getNum()
                        .chars()
                        .allMatch(Character::isDigit)) // 번호가 공지, 뉴스, 설문 인 경우 필터링
                .collect(Collectors.toList());
        return list;
    }
}
