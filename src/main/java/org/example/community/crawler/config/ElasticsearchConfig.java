package org.example.community.crawler.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
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

        final RestClientBuilder restClientBuilder = RestClient.builder(HttpHost.create(uri));
        // optionally perform some other configuration of restClientBuilder here if needed
        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                /* optionally perform some other configuration of httpClientBuilder here if needed */
                .setDefaultIOReactorConfig(IOReactorConfig.custom()
                        /* optionally perform some other configuration of IOReactorConfig here if needed */
                        .setSoKeepAlive(true)
                        .build()));
        return new RestHighLevelClient(restClientBuilder);


//        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
//                .connectedTo(uri)
//                .build();
//
//        return RestClients.create(clientConfiguration).rest();
    }
}
