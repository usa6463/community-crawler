package dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CrawlingResult {
    private HtmlMeta htmlMeta;
    private Content content;
}
