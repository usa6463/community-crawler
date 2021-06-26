package dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DCPost {
    private HtmlMeta meta;
    private String title;
    private String content;
    private String nickname;
    private String ip;
    private String date;
    private String view_count;
    private String recommend_count;
    private String comment_count;
}
