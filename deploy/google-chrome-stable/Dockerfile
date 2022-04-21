FROM openjdk:11.0.14.1-jre-buster
#buster 의미 : debian OS의 oldstable - The previous stable release (Buster).

RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN echo 'deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main' | tee /etc/apt/sources.list.d/google-chrome.list
RUN apt-get update
RUN apt-get -y install google-chrome-stable=100.0.4896.127-1
COPY chromedriver_linux64 /