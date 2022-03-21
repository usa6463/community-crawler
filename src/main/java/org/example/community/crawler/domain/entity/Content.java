package org.example.community.crawler.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Content {
    private String title;
    private String content;
    private String url;
}
