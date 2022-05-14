package org.example.community.crawler.domain.entity;

import lombok.Getter;

@Getter
public class DCMinorPostMeta extends PostMeta {
    final String subject; // 말머리

    public DCMinorPostMeta(String date, String num, String count, String url, String subject) {
        super(date, num, count, url);
        this.subject = subject;
    }
}
