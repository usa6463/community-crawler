package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.joda.time.DateTime;

import java.util.ArrayList;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class DCContent extends Content {
    private String nickname;
    private String ip;
    private DateTime dateTime;
    private String viewCount;
    private String recommendCount;
    private String commentCount;
    private ArrayList<DCReply> replyList;
}
