package org.example.community.crawler.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class DCInnerReply {
    private String nickname;
    private String ip;
    private String date;
    private String content;
}
