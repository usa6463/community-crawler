package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class DCReply extends Content {
    private String nickname;
    private String ip;
    private String date;
    private String content;
    private ArrayList<DCInnerReply> innerReplyList;
}
