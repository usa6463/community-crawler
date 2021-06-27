package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DCInnerReply extends Content {
    private String nickname;
    private String ip;
    private String date;
    private String content;
}
