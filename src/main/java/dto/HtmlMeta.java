package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HtmlMeta {
    private int textLength;
    private int htmlLength;
    private int outgoingLinkCount;
}
