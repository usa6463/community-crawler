package org.example.community.crawler.domain.entity;

import lombok.*;

import java.util.ArrayList;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DCReply {
    private String id;
    private String nickname;
    private String ip;
    private String date;
    private String content;
    private ArrayList<DCInnerReply> innerReplyList;
}
