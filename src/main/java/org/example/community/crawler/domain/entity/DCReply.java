package org.example.community.crawler.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class DCReply {
    private String id;
    private String nickname;
    private String ip;
    private String date;
    private String content;
    private ArrayList<DCInnerReply> innerReplyList;
}
