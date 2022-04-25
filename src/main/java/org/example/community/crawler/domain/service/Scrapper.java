package org.example.community.crawler.domain.service;

public interface Scrapper {

    /**
     * 커뮤니티 사이트의 특정날짜 게시글을 스크래핑 하여 스토리지에 저장
     */
    void scrap();
}