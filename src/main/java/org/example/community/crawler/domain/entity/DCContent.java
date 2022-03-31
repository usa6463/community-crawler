package org.example.community.crawler.domain.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
@Builder
@Document(indexName = "#{@appConfiguration.getEsIndexName()}")
public class DCContent {
    @Id
    private int contentNum;
    @NotNull
    private String title;
    private String content;
    private String url;
    private String nickname;
    private String ip;
    private String dt;
    private String viewCount;
    private String recommendCount;
    private String commentCount;
    private ArrayList<DCReply> replyList;
}
