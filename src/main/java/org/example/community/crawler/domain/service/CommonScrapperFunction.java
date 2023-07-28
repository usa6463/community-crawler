package org.example.community.crawler.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.config.AppConfiguration;
import org.jsoup.Jsoup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class CommonScrapperFunction {

    public static WebDriver getWebDriver(AppConfiguration appConfiguration, String webDriverId) {
        final WebDriver driver;
        System.setProperty(webDriverId, appConfiguration.getWebDriverPath());
        System.setProperty("webdriver.chrome.whitelistedIps", "");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--single-process");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        log.debug("chromedriver option setting complete");

        driver = new ChromeDriver(chromeOptions);
        return driver;
    }

    static String removeParenthesis(String src) {
        return src.replaceAll("[\\(,\\)]", "");
    }

    static String convertEmptyStringToNull(String input) {
        if (input.equals("")) {
            return null;
        }
        return input;
    }

    static String removeTag(String html) {
        return Jsoup.parse(html).text();
    }

    /**
     * dateTime 문자열을 파싱하고 특정 zone의 시간대로 변경한 뒤 다시 문자열로 반환.
     * 예를들어 fromZone이 "Asia/Seoul" 이고 toZone이 "UTC" 라면 9시간을 뺀 datetime 문자열을 반환한다.
     *
     * @param dateTime 대상 dateTime 문자열
     * @param fromZone 대상 dateTime 문자열의 time zone
     * @param toZone   변경하고자 하는 time zone
     * @param pattern  문자열 dateTime의 패턴
     * @return dateTime 특정 zone의 시간대로 변경한 뒤의 문자열
     */
    static String getZonedDatetime(String dateTime, String fromZone, String toZone, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        ZonedDateTime zonedDateTime = ZonedDateTime.of(LocalDateTime.parse(dateTime, formatter), ZoneId.of(fromZone));
        return zonedDateTime.withZoneSameInstant(ZoneId.of(toZone)).format(formatter);
    }
}