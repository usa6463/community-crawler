package org.example.community.crawler.domain.service;

import org.example.community.crawler.config.AppConfiguration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class CommonScrapperFunction {

    static WebDriver getWebDriver(AppConfiguration appConfiguration, String webDriverId) {
        final WebDriver driver;
        System.setProperty(webDriverId, appConfiguration.getWebDriverPath());
        System.setProperty("webdriver.chrome.whitelistedIps", "");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--single-process");
        chromeOptions.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(chromeOptions);
        return driver;
    }
}