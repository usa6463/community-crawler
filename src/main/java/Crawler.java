import com.google.common.collect.Lists;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Crawler {
    public static void main(String[] args) throws Exception {

//        int numberOfCrawlers = 1;

//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCrawlers);
//        List<String> seedList = IntStream.range(1, 2).mapToObj(page->String.format("https://gall.dcinside.com/board/lists?id=neostock&page=%d", page)).collect(Collectors.toList());
//        System.out.println(seedList);
//        https://gall.dcinside.com/board/view/?id=neostock&no=1264154

        File file = new File("property.dat");
        Properties prop = new Properties();
//        prop.setProperty("LastContentNum", "1257592");
//        saveProperties(file, prop);
        loadProperties(file, prop);
        System.out.println(prop.get("LastContentNum"));



//        CrawlConfig config = new CrawlConfig();
//        config.setMaxDepthOfCrawling(100);			// 시작 URL에서 몇 단계까지 탐색할지 설정
//        config.setPolitenessDelay(300);				// 동일 호스트에 대한 요청 delay 설정 (ms)
//        config.setCrawlStorageFolder("data/crawl");	// 크롤러의 데이터 저장 디렉터리 지정
//
//
//        // CrawController 준비하기
//        PageFetcher pageFetcher = new PageFetcher(config);
//        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
//        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
//        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

//        // 크롤링 시작 URL 지정하기
//        controller.addSeed("https://gall.dcinside.com/board/lists?id=neostock");
//
//        // 크롤링 시작하기
//        controller.start(DCCrawler.class, numberOfCrawlers);
    }

    private static void saveProperties(File file, Properties p) throws IOException {
        FileOutputStream fr = new FileOutputStream(file);
        p.store(fr, "Properties");
        fr.close();
    }

    private static void loadProperties(File file, Properties p) throws IOException
    {
        FileInputStream fi=new FileInputStream(file);
        p.load(fi);
        fi.close();
    }
}
