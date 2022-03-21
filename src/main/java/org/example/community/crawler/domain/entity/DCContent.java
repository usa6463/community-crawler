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
public class DCContent extends Content {
    private String nickname;
    private String ip;
    private String dt;
    private String viewCount;
    private String recommendCount;
    private String commentCount;
    private ArrayList<DCReply> replyList;
    private int contentNum;
}
