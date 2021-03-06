package org.example.community.crawler.domain.entity;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InnerReply {
    private String nickname;
    private String ip;
    private String date;
    private String content;
}
