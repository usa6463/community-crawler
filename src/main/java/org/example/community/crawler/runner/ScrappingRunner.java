package org.example.community.crawler.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScrappingRunner implements CommandLineRunner {

    @Value("${test.address}")
    String address;

    @Override
    public void run(String... args) throws Exception {
        log.info("HI this is address {}", address);
    }
}
