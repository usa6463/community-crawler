import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.time.LocalDate;
import java.util.stream.IntStream;

public class Controller {
    public static void main(String[] args) throws Exception {

        int numberOfCrawlers = 5;

        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(0);			// 시작 URL에서 몇 단계까지 탐색할지 설정
        config.setPolitenessDelay(300);				// 동일 호스트에 대한 요청 delay 설정 (ms)
        config.setCrawlStorageFolder("data/crawl");	// 크롤러의 데이터 저장 디렉터리 지정

        // CrawController 준비하기
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        LocalDate currentDate = LocalDate.now();
        LocalDate before1Day = currentDate.minusDays(1);
        System.out.println(before1Day);
//        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        int lastContentNum = 1272610;

        // 크롤링 시작 URL 지정하기
        IntStream.range(1, 100).forEach(
                x->{
                    int contentNum = lastContentNum + x;
                    controller.addSeed(String.format("https://gall.dcinside.com/board/view/?id=neostock&no=%d", contentNum));
                }
        );
        CrawlController.WebCrawlerFactory<DCCrawler> factory = () -> new DCCrawler(before1Day);
        // 크롤링 시작하기
        controller.start(factory, numberOfCrawlers);




    }
}
