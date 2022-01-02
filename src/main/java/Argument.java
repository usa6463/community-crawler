import com.beust.jcommander.Parameter;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Argument {
    @Parameter(names = "--target_date", description = "target date 이전 날짜의 게시글만 수집한다. format: yyyy-mm-dd", required = true)
    private String targetDate;

    @Parameter(names = "--last_content_num", description = "이전에 가장 마지막으로 파싱한 게시글 번호", required = true)
    private int lastContentNum;

    @Parameter(names = "--web_driver_path", description = "chrome driver path")
    private String webDriverPath="C:\\Program Files\\chromedriver_win32\\chromedriver.exe";
}
