FROM maven:3.8.3-openjdk-11 AS build-env

WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN rm -rf /app/target

RUN mvn -Dmaven.test.skip=true -Djava.version=11 package

FROM usa6463/google-chrome-stable:100.0.4896.127-1

RUN groupadd -r -g 2001 appuser && useradd -r -u 1001 -g appuser appuser
RUN mkdir /home/appuser && chown appuser /home/appuser
USER appuser
COPY --chown=appuser:appuser --from=build-env /app/target/*.jar /home/appuser/app.jar

ENV ES_HOST="elasticsearch-master.default.svc.cluster.local"
ENV ES_PORT="9200"
ENV TARGET_DATE="2022-04-14"
ENV LOGGING_LEVEL="info"
ENV BOARD_BASE_URL="https://gall.dcinside.com/board/lists/?id=rlike"
ENV ES_INDEX_NAME="dc-content-loglike"
ENV WEB_DRIVER_PATH="/chromedriver"

ENTRYPOINT ["java","-jar","/home/appuser/app.jar"]
CMD ["--spring.profiles.active=prod"]
