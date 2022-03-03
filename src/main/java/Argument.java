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

    @Parameter(names = "--elasticsearch_hostname", description = "데이터를 적재할 ES의 hostname")
    private String elasticsearchHostname="localhost";

    @Parameter(names = "--elasticsearch_port", description = "데이터를 적재할 ES의 port")
    private int elasticsearchPort=9200;

    @Parameter(names = "--elasticsearch_index_name", description = "데이터를 적재할 ES의 인덱스명")
    private String elasticsearchIndexName="my-index-1";
}
