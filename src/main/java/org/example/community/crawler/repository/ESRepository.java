package org.example.community.crawler.repository;

import org.example.community.crawler.domain.entity.DCContent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESRepository extends ElasticsearchRepository<DCContent, String> {

    @Override
    <S extends DCContent> S save(S entity);
}
