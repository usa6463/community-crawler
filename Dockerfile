FROM usa6463/google-chrome-stable:99.0.4844.74-1

COPY ./target/ /tmp
COPY ./chromedriver_linux64/chromedriver /tmp

WORKDIR /tmp

ENTRYPOINT ["java","-jar","community-crawler-1.0-SNAPSHOT.jar", \
 "--web_driver_path", "/tmp/chromedriver"]
CMD ["--target_date", "2022-01-24", \
      "--last_content_num", "2203516",\
      "--elasticsearch_hostname", "elasticsearch-master.default.svc.cluster.local",\
      "--elasticsearch_port", "9200",\
      "--elasticsearch_index_name", "dc-content-test"]