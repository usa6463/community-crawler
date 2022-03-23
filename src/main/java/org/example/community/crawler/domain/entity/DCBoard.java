package org.example.community.crawler.domain.entity;

import lombok.Data;

/**
 * DC 게시판 정보
 * 아래와 같은 페이지에서 나오는 내용
 * ex) https://gall.dcinside.com/board/lists/?id=neostock&page=1
 */
@Data
public class DCBoard {
    final String date; // 작성일
    final String num; // 번호
    final String count; // 조회
}
