package org.example.community.crawler.repository;

import org.example.community.crawler.domain.entity.Content;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESRepository extends ElasticsearchRepository<Content, String> {

    @Override
    <S extends Content> S save(S entity);
}
