# community-crawler

### JIRA
https://fenderi.atlassian.net/secure/RapidBoard.jspa?rapidView=1&projectKey=CSA

### profile 설정
--spring.profiles.active={dev, local, prod 중 택1)

### PARAMETER
--target_date : target date 이전 날짜의 게시글만 수집한다. format: yyyy-mm-dd

--last_content_num : 이전에 가장 마지막으로 파싱한 게시글 번호. 이 번호 이후 게시글 수집한다.

--web_driver_path : chrome driver 경로 명시. default 값은 "C:\\Program Files\\chromedriver_win32\\chromedriver.exe"