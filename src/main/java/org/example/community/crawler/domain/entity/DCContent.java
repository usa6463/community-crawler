package org.example.community.crawler.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "dc-content-1")
public class DCContent extends Content {
    private String nickname;
    private String ip;
    private String dt;
    private String viewCount;
    private String recommendCount;
    private String commentCount;
    private ArrayList<DCReply> replyList;
    @Id
    private int contentNum;
}
