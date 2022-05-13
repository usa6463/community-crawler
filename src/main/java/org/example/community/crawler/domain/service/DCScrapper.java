package org.example.community.crawler.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.example.community.crawler.domain.entity.Content;
import org.example.community.crawler.domain.entity.InnerReply;
import org.example.community.crawler.domain.entity.PostMeta;
import org.example.community.crawler.domain.entity.Reply;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class DCScrapper extends Scrapper {

    /**
     * DC 게시판에서 확인할 수 있는 게시글 등록일자 포맷
     */
    public static final String DC_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 게시판 번호 붙이는 포맷
     */
    public static final String DC_BOARD_PAGE_URL_FORMAT = "%s&page=%d";
    /**
     * 게시글 url에서 게시글 번호를 추출하기 위한 regex pattern
     */
    public static final String PATTERN_FOR_CONTENT_NUM = "\\S+no=(\\d+).*";

    DCScrapper(String domain) {
        super(domain);
    }


    /**
     * 게시글 내용 파싱하여 반환
     *
     * @param url 파싱할 게시글 url
     * @param doc 파싱할 게시글 Document 객체
     * @return DCContent 데이터 객체 반환
     */
    @Override
    public Content getContent(String url, Document doc, WebDriver driver) {
        String content = removeTag(doc.select(".write_div").html());
        String title = doc.select(".title_subject").html();
        int contentNum = Integer.parseInt(url.replaceAll(PATTERN_FOR_CONTENT_NUM, "$1"));

        Elements fl = doc.select(".fl");
        String nickname = removeTag(fl.select(".nickname").html());
        String ip = CommonScrapperFunction.removeParenthesis(fl.select(".ip")
                .html());
        String date = getZonedDatetime(fl.select(".gall_date")
                        .html(),
                "Asia/Seoul", "Asia/Seoul", "yyyy.MM.dd HH:mm:ss");

        Elements fr = doc.select(".fr");
        String viewCount = fr.select(".gall_count")
                .html()
                .replaceAll("조회 ", "");
        String recommendCount = fr.select(".gall_reply_num")
                .html()
                .replaceAll("추천 ", "");
        String commentCount = removeTag(fr.select(".gall_comment")
                .html())
                .replaceAll("댓글 ", "");

        Content dcContent = Content.builder()
                .title(title)
                .url(url)
                .content(content)
                .nickname(nickname)
                .ip(ip)
                .dt(date)
                .viewCount(viewCount)
                .recommendCount(recommendCount)
                .commentCount(commentCount)
                .contentNum(contentNum)
                .replyList(getReplyList(url, driver))
                .build();

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Content>> violations = validator.validate(dcContent);
        violations.forEach(x -> log.error(x.getMessage()));

        return dcContent;
    }


    /**
     * 댓글 정보 파싱하여 반환
     *
     * @param url 파싱할 게시글 url
     * @return 파싱된 댓글 리스트 반환
     */
    private ArrayList<Reply> getReplyList(String url, WebDriver driver) {
        ArrayList<Reply> result = new ArrayList<>();

        driver.get(url);
        Document doc = Jsoup.parse(driver.getPageSource());
//        driver.close();

        Elements commentBox = doc.select(".comment_box");
        Elements replyList;
        if (commentBox.isEmpty()) {
            return result;
        }

        replyList = commentBox.select("li[id^=comment_li_]");
        replyList.stream()
                .filter(element -> !element.attr("id").split("_")[2].equals("0")) //댓글돌이 제거
                .forEach(
                        element -> {
                            Reply reply = parseCommentLi(element, commentBox);
                            result.add(reply);
                        }
                );
        return result;
    }

    /**
     * id가 comment_li인 태그 내용을 파싱
     *
     * @param element    comment_li의 element
     * @param commentBox comment_box의 elements
     * @return 파싱결과 데이터 객체 반환
     */
    private Reply parseCommentLi(Element element, Elements commentBox) {
        String replyId = element.attr("id").split("_")[2];
        Reply result = new Reply();
        result.setId(replyId);

        result.setNickname(element.select("em").html());
        result.setIp(
                convertEmptyStringToNull(CommonScrapperFunction.removeParenthesis(element.select(".ip")
                        .html())));
        result.setContent(removeTag(element.select("p[class^=usertxt]").html()));
        result.setDate(element.select("span[class^=date_time]").html());
        result.setInnerReplyList(getInnerReply(replyId, commentBox));
        return result;
    }

    /**
     * 내부 댓글 파싱하여 반환
     *
     * @param replyId    댓글 id
     * @param commentBox comment_box의 elements
     * @return 파싱결과 데이터 객체 반환
     */
    private ArrayList<InnerReply> getInnerReply(String replyId, Elements commentBox) {
        ArrayList<InnerReply> result = new ArrayList<>();
        String cssQuery = String.format("ul[id=reply_list_%s]", replyId);
        Elements innerReplyList = commentBox.select(cssQuery);
        innerReplyList.forEach(
                element -> {
                    InnerReply innerReply = new InnerReply();
                    innerReply.setNickname(element.select("em[title]").html());
                    innerReply.setIp(convertEmptyStringToNull(CommonScrapperFunction.removeParenthesis(element.select(".ip").html())));
                    innerReply.setContent(removeTag(element.select("p[class^=usertxt]").html()));
                    innerReply.setDate(element.select("span[class^=date_time]").html());
                    result.add(innerReply);
                }
        );
        return result;
    }

    /**
     * 게시판 순회. target_date 이전 날짜 나오기 시작하면 중단
     *
     * @param targetDate   수집대상 게시글 작성일
     * @param boardBaseUrl 순회 대상 게시판 URL(페이지 번호 제외)
     * @return 게시판 순회하면서 target_date 날짜 게시글 링크를 list에 append 후 반환
     * @throws IOException Jsoup으로 get 수행시 발생 가능
     */
    @Override
    List<PostMeta> traverseBoard(LocalDate targetDate, String boardBaseUrl) throws IOException, InterruptedException {
        int boardPage = 1;
        boolean targetDateFlag = true;
        List<PostMeta> result = new ArrayList<>();
        while (targetDateFlag) {
            String boardUrl = String.format(DC_BOARD_PAGE_URL_FORMAT, boardBaseUrl, boardPage);

            log.debug("get Post info from {}", boardUrl);
            List<PostMeta> list = getDcPosts(Jsoup.connect(boardUrl).get());
            targetDateFlag = checkTargetDateBeforePost(targetDate, list);

            result.addAll(getTargetDatePost(targetDate, list));

            boardPage = boardPage + 1;
            Thread.sleep(1000); // TODO rate limiter로 변경하고 InterruptedException 제거
        }
        log.info("last traversed page : {}", boardPage);
        log.info("target post num : {}, list : {}", result.size(), result);
        return result;
    }

    /**
     * target date의 게시글만 남도록 필터링 한다.
     *
     * @param targetDate 수집하고자 하는 게시글 등록일자
     * @param postList   게시판 페이지에서 수집한 게시글 정보 리스트
     * @return target date 게시글만 남은 리스트
     */
    private List<PostMeta> getTargetDatePost(LocalDate targetDate, List<PostMeta> postList) {
        return postList.stream()
                .filter(post -> LocalDate.parse(post.getDate(), DateTimeFormatter.ofPattern(DC_DATETIME_FORMAT))
                        .isEqual(targetDate))
                .collect(Collectors.toList());
    }

    /**
     * 게시판 페이지에 target date 이전 날짜의 게시글이 있는지 체크
     *
     * @param targetDate 수집하고자 하는 게시글 등록일자
     * @param postList   게시판 페이지에서 수집한 게시글 정보 리스트
     * @return target date 이전 날짜 게시글이 없으면 true, 있으면 false
     */
    private Boolean checkTargetDateBeforePost(LocalDate targetDate, List<PostMeta> postList) {
        long targetDateBeforePostCount = postList.stream()
                .filter(post -> LocalDate.parse(post.getDate(), DateTimeFormatter.ofPattern(DC_DATETIME_FORMAT))
                        .isBefore(targetDate))
                .count();
        log.debug("targetDateBeforePostCount : {}", targetDateBeforePostCount);
        return targetDateBeforePostCount <= 0;
    }

    /**
     * DC 게시판을 파싱하여 게시글 정보 수집
     *
     * @param doc 스크래핑할 페이지 Doc
     * @return 게시글 정보 리스트
     * @throws IOException Jsoup으로 get 수행시 발생 가능
     */
    public List<PostMeta> getDcPosts(Document doc) throws IOException {

        Elements gallDateList = doc.select(".gall_date");
        Elements gallNumList = doc.select(".gall_num");
        Elements gallCountList = doc.select(".gall_count");
        Elements gallUrlList = doc.select(".gall_tit > a:not(.reply_numbox)");

        int minListSize = Collections.min(Arrays.asList(gallDateList.size(),
                gallNumList.size(), gallCountList.size(), gallUrlList.size()));

        return IntStream
                .range(0, minListSize)
                .mapToObj(i -> new PostMeta(
                        gallDateList.get(i).attr("title"),
                        gallNumList.get(i).ownText(),
                        gallCountList.get(i).ownText(),
                        gallUrlList.get(i).attr("href")
                ))
                .filter(obj -> obj.getCount()
                        .chars()
                        .allMatch(Character::isDigit)) // 조회수가 "-" 인 경우 필터링
                .filter(obj -> obj.getNum()
                        .chars()
                        .allMatch(Character::isDigit)) // 번호가 공지, 뉴스, 설문 인 경우 필터링
                .collect(Collectors.toList());
    }
}
