import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CrawlingResult;
import dto.DCContent;
import dto.DCReply;
import dto.HtmlMeta;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

public class DCCrawler extends WebCrawler {
    public static final String HOST = "https://gall.dcinside.com/board/view/?id=neostock&no=";
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    private final static String WEB_DRIVER_ID = "webdriver.chrome.driver";
    private final static String WEB_DRIVER_PATH = "C:\\Program Files\\chromedriver_win32\\chromedriver.exe";

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith(HOST);
    }

    @Override
    public void visit(Page page) {
        logUrlInfo(page);
        parseHtml(page);
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
        if (!(page.getParseData() instanceof HtmlParseData)) { // html 형태의 Data일 경우
            return;
        }

        CrawlingResult result = new CrawlingResult();
        ObjectMapper mapper = new ObjectMapper();

        HtmlMeta htmlMeta = getHtmlMeta(page);
        result.setHtmlMeta(htmlMeta);

        DCContent content = getContent(page);
        result.setContent(content);

        try {
            logger.info("json result : {}", mapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private DCContent getContent(Page page) {
        DCContent result = new DCContent();
        Document doc = Jsoup.parse(((HtmlParseData) page.getParseData()).getHtml());

        String content = doc.select(".write_div").html();
        String title = doc.select(".title_subject").html();

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
        result.setContent(content);
        result.setNickname(nickname);
        result.setIp(ip);
        result.setDate(date);
        result.setView_count(viewCount);
        result.setRecommend_count(recommendCount);
        result.setComment_count(commentCount);

        result.setReplyList(getReplyList(page));

        return result;
    }

    private ArrayList<DCReply> getReplyList(Page page) {
        ArrayList<DCReply> result = new ArrayList<>();
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        WebDriver driver = new ChromeDriver();
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
                    DCReply reply = parseCommentLi(element);
                    result.add(reply);
                }
        );
        return result;
    }

    private DCReply parseCommentLi(org.jsoup.nodes.Element element) {
        String replyId = element.attr("id");
        DCReply result = new DCReply();
        result.setId(replyId);
        Elements innerReplyList = element.select(String.format("li[id^=reply_li_{%s}]", replyId));
        result.setNickname(element.select("em[title]").html());
        result.setIp(element.select(".ip").html());
        result.setContent(element.select("p[class^=usertxt]").html());
        result.setDate(element.select("span[class^=date_time]").html());
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
