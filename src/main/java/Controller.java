import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.beust.jcommander.JCommander;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

public class Controller {
    public static void main(String[] args) throws Exception {
        Argument argument = getArgument(args);

        Logger logger = LoggerFactory.getLogger(Controller.class);

        int latestContentNum = DCCrawler.getLatestContentNum("https://gall.dcinside.com/board/lists?id=neostock");
        int numberOfCrawlers = 5;

        CrawlConfig config = new CrawlConfig();
        config.setMaxDepthOfCrawling(0);			// 시작 URL에서 몇 단계까지 탐색할지 설정
        config.setPolitenessDelay(300);				// 동일 호스트에 대한 요청 delay 설정 (ms)
        config.setCrawlStorageFolder("data/crawl");	// 크롤러의 데이터 저장 디렉터리 지정

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        LocalDate targetDate = LocalDate.parse(argument.getTargetDate(), DateTimeFormatter.ISO_DATE);
        System.out.println(targetDate);

        int lastContentNum = argument.getLastContentNum();
        logger.info("lastContentNum : {}, latestContentNum : {}", lastContentNum, latestContentNum);

        ElasticsearchClient client = getElasticsearchClient(argument.getElasticsearchHostname(), argument.getElasticsearchPort());

        // 크롤링 시작 URL 지정하기
        IntStream.range(lastContentNum+1, latestContentNum+1)
                .forEach(contentNum-> controller.addSeed(String.format("https://gall.dcinside.com/board/view/?id=neostock&no=%d", contentNum)));
        CrawlController.WebCrawlerFactory<DCCrawler> factory = () -> new DCCrawler(targetDate, argument.getWebDriverPath(), client);
        // 크롤링 시작하기
        controller.start(factory, numberOfCrawlers);
    }

    private static ElasticsearchClient getElasticsearchClient(String elasticsearchHostname, int elasticsearchPort) {
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                new HttpHost(elasticsearchHostname, elasticsearchPort)).build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);
        return client;
    }

    private static Argument getArgument(String[] args) {
        Argument argument = new Argument();
        JCommander.newBuilder()
                .addObject(argument)
                .build()
                .parse(args);
        return argument;
    }
}
