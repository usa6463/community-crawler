import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
    public static void main(String[] args) throws Exception {

        int numberOfCrawlers = 1;

        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(0);			// 시작 URL에서 몇 단계까지 탐색할지 설정
        config.setPolitenessDelay(500);				// 동일 호스트에 대한 요청 delay 설정 (ms)
        config.setCrawlStorageFolder("data/crawl");	// 크롤러의 데이터 저장 디렉터리 지정


        // CrawController 준비하기
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        // 크롤링 시작 URL 지정하기
//        controller.addSeed("https://gall.dcinside.com/board/lists?id=neostock");
        controller.addSeed("https://gall.dcinside.com/board/view/?id=neostock&no=1215907&page=1"); //TODO 테스트용 시드 추후 변경 필요

        // 크롤링 시작하기
        controller.start(DCCrawler.class, numberOfCrawlers);
    }
}
