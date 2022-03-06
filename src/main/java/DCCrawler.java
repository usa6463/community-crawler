import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.*;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

public class DCCrawler extends WebCrawler {
    public static final String CONTENT_URL = "https://gall.dcinside.com/board/view/?id=neostock";
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    private final static String WEB_DRIVER_ID = "webdriver.chrome.driver";

    private final LocalDate targetDate;
    private final String webDriverPath;
    private final ElasticsearchClient elasticsearchClient;
    private final String elasticsearchIndexName;


    public DCCrawler(LocalDate targetDate, String webDriverPath, ElasticsearchClient elasticsearchClient, String elasticsearchIndexName) {
        this.targetDate = targetDate;
        this.webDriverPath = webDriverPath;
        this.elasticsearchClient = elasticsearchClient;
        this.elasticsearchIndexName = elasticsearchIndexName;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && (href.startsWith(CONTENT_URL));
    }

    /**
     * 스크래핑 대상 페이지에 대해서 처리하는 로직의 main 함수
     *
     * @param page 페이지 정보 객체
     */
    @Override
    public void visit(Page page) {
        logUrlInfo(page);
        parseHtml(page);
    }

    public static int getLatestContentNum(String pageUrl) throws IOException {
        Document doc = Jsoup.connect(pageUrl).get();
        Elements gallNumList = doc.select(".gall_num");
        int result = gallNumList.stream()
                .filter(e -> e.html()
                        .matches("[0-9]+"))
                .mapToInt(e -> Integer.parseInt(e.html()))
                .max()
                .orElseThrow(NoSuchElementException::new);

        logger.info("Latest Content Number : {}", result);
        return result;
    }

    private void logUrlInfo(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();

        logger.debug("Docid: {}", docid);
        logger.info("URL: {}", url);
        logger.debug("Domain: '{}'", domain);
        logger.debug("Sub-domain: '{}'", subDomain);
        logger.debug("Path: '{}'", path);
        logger.debug("Parent page: {}", parentUrl);
        logger.debug("Anchor text: {}", anchor);
    }

    private void parseHtml(Page page) {
        if (!(page.getParseData() instanceof HtmlParseData)) { // html 형태의 Data가 아닐 경우
            return;
        }
        CrawlingResult result = new CrawlingResult();
        ObjectMapper mapper = new ObjectMapper();

        DCContent content = getContent(page);
        LocalDate contentDate = LocalDate.parse(content.getDt(), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));

        if (!contentDate.isBefore(targetDate)) {
            return;
        }
        result.setContent(content);

        HtmlMeta htmlMeta = getHtmlMeta(page);
        result.setHtmlMeta(htmlMeta);

        try {
            logger.info("json result : {}", mapper.writeValueAsString(result));
            elasticsearchClient.create(r -> r
                    .index(elasticsearchIndexName)
                    .document(result)
                    .id(UUID.randomUUID().toString())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DCContent getContent(Page page) {
        DCContent result = new DCContent();
        Document doc = Jsoup.parse(((HtmlParseData) page.getParseData()).getHtml());

        String content = doc.select(".write_div").html();
        String title = doc.select(".title_subject").html();
        String url = page.getWebURL().getURL();

        Elements fl = doc.select(".fl");
        String nickname = fl.select(".nickname").html();
        String ip = fl.select(".ip").html();
        String date = fl.select(".gall_date").html();

        Elements fr = doc.select(".fr");
        String viewCount = fr.select(".gall_count").html();
        String recommendCount = fr.select(".gall_reply_num").html();
        String commentCount = fr.select(".gall_comment").html();

        logger.debug("title : {}", title);
        logger.debug("content : {}", content);
        logger.debug("nickname : {}", nickname);
        logger.debug("ip : {}", ip);
        logger.debug("date : {}", date);
        logger.debug("viewCount : {}", viewCount);
        logger.debug("recommendCount : {}", recommendCount);
        logger.debug("commentCount : {}", commentCount);

        result.setTitle(title);
        result.setUrl(url);
        result.setContent(content);
        result.setNickname(nickname);
        result.setIp(ip);
        result.setDt(date);
        result.setViewCount(viewCount);
        result.setRecommendCount(recommendCount);
        result.setCommentCount(commentCount);

        result.setReplyList(getReplyList(page));

        return result;
    }

    private ArrayList<DCReply> getReplyList(Page page) {
        ArrayList<DCReply> result = new ArrayList<>();
        System.setProperty(WEB_DRIVER_ID, webDriverPath);
        System.setProperty("webdriver.chrome.whitelistedIps", "");

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--single-process");
        chromeOptions.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(chromeOptions);
        driver.get(page.getWebURL().getURL());
        Document doc = Jsoup.parse(driver.getPageSource());
        driver.close();

        Elements commentBox = doc.select(".comment_box");
        Elements replyList;
        if (commentBox.isEmpty()) {
            return result;
        }

        replyList = commentBox.select("li[id^=comment_li_]");
        replyList.forEach(
                element -> {
                    DCReply reply = parseCommentLi(element, commentBox);
                    result.add(reply);
                }
        );
        return result;
    }

    private DCReply parseCommentLi(Element element, Elements commentBox) {
        String replyId = element.attr("id").split("_")[2];
        DCReply result = new DCReply();
        result.setId(replyId);

        result.setNickname(element.select("em[title]").html());
        result.setIp(element.select(".ip").html());
        result.setContent(element.select("p[class^=usertxt]").html());
        result.setDate(element.select("span[class^=date_time]").html());
        result.setInnerReplyList(getInnerReply(replyId, commentBox));
        return result;
    }

    private ArrayList<DCInnerReply> getInnerReply(String replyId, Elements commentBox) {
        ArrayList<DCInnerReply> result = new ArrayList<>();
        String cssQuery = String.format("ul[id=reply_list_%s]", replyId);
        Elements innerReplyList = commentBox.select(cssQuery);
        innerReplyList.forEach(
                element -> {
                    DCInnerReply innerReply = new DCInnerReply();
                    innerReply.setNickname(element.select("em[title]").html());
                    innerReply.setIp(element.select(".ip").html());
                    innerReply.setContent(element.select("p[class^=usertxt]").html());
                    innerReply.setDate(element.select("span[class^=date_time]").html());
                    result.add(innerReply);
                }
        );
        return result;
    }

    private HtmlMeta getHtmlMeta(Page page) {
        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
        String text = htmlParseData.getText();
        String html = htmlParseData.getHtml();
        Set<WebURL> links = htmlParseData.getOutgoingUrls();

        int textLength = text.length();
        int htmlLength = html.length();
        int outgoingLinkCount = links.size();
        logger.debug("Text length: {}", textLength);
        logger.debug("Html length: {}", htmlLength);
        logger.debug("Number of outgoing links: {}", outgoingLinkCount);

        HtmlMeta result = new HtmlMeta();
        result.setTextLength(textLength);
        result.setHtmlLength(htmlLength);
        result.setOutgoingLinkCount(outgoingLinkCount);
        return result;
    }
}
