# community-crawler

## 소개
- 커뮤니티 사이트 게시글을 스크랩핑하여 ElasticSearch에 적재하는 일배치 java app
- 게시판 단위로 타겟 일자의 게시글 스크래핑
- 동작 방식
    - 게시판 페이지를 순회하면서 타겟 일자의 게시글 URL 리스트를 추출
    - 리스트의 URL 순회하며 스크래핑(싱글 스레드, Politeness: 1초)
- Selenium 사용
    
- 지원 커뮤니티 사이트
    - DC 인사이드
    - DC 인사이드 마이너 갤러리

## profile 설정
--spring.profiles.active={dev, local, prod 중 택1)

## 환경변수 세팅
- ES_HOST: 스크래핑 결과 적재할 ElasticSearch의 호스트명 (ex. "localhost")
- ES_PORT: 스크래핑 결과 적재할 ElasticSearch의 포트 (ex. 9200)
- TARGET_DATE: 스크래핑 하고자 하는 게시글의 게시 일자 (ex. "2022-05-15")
- LOGGING_LEVEL: 로깅 레벨 (ex. "info")
- BOARD_BASE_URL: 스크래핑 대상 게시판 URL (ex. "https://gall.dcinside.com/mgallery/board/lists?id=mf")
- ES_INDEX_NAME: 스크래핑 결과 적재할 인덱스명 
- WEB_DRIVER_PATH: Selenium을 위한 웹 드라이버의 경로