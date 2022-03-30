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
@Document(indexName = "dc-content-1") // TODO 설정파일 통해서 변경 가능하도록 하기
public class DCContent extends Content {
    @Id
    private int contentNum;
    private String nickname;
    private String ip;
    private String dt;
    private String viewCount;
    private String recommendCount;
    private String commentCount;
    private ArrayList<DCReply> replyList;
}
