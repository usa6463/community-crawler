import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Set;
import java.util.regex.Pattern;

public class DCCrawler extends WebCrawler {
    public static final String HOST = "https://gall.dcinside.com/board/view/?id=neostock&no=";
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp4|zip|gz))$");

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !FILTERS.matcher(href).matches()
                && href.startsWith(HOST);
    }

    @Override
    public void visit(Page page) {
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

        if (page.getParseData() instanceof HtmlParseData) { // html 형태의 Data일 경우

            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            logger.debug("Text length: {}", text.length());
            logger.debug("Html length: {}", html.length());
            logger.debug("Number of outgoing links: {}", links.size());

            // 페이지의 HTML을 파싱하기
            Document doc = Jsoup.parse(html);


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
        }
    }
}
