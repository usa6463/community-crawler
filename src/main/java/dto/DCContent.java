package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DCContent extends Content {
    private String nickname;
    private String ip;
    private String date;
    private String view_count;
    private String recommend_count;
    private String comment_count;
}
