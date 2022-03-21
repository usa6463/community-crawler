package org.example.community.crawler.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class CrawlingResult {
    private HtmlMeta htmlMeta;
    private Content content;
}
