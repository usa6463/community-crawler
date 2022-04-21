package org.example.community.crawler.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
@Slf4j
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {
    @Value("${spring.elasticsearch.host}")
    private String host;
    @Value("${spring.elasticsearch.port}")
    private int port;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        String uri = String.format("%s:%s", host, port);
        log.info("elasticsearch uri : {}", uri);

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(uri)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
