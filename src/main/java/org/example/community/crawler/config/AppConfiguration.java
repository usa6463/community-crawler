package org.example.community.crawler.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfiguration {

    @Value("${app.board_base_url}")
    String boardBaseUrl;

    @Value("${app.target_date}")
    String targetDate;

    @Value("${app.web_driver_path}")
    String webDriverPath;

    @Value("${app.es_index_name}")
    String esIndexName;
}
