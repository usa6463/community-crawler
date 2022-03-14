FROM openjdk:11

RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo 'deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main' | tee /etc/apt/sources.list.d/google-chrome.list
RUN apt-get update
RUN apt-get -y install google-chrome-stable=99.0.4844.51-1

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