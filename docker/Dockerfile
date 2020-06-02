FROM hseeberger/scala-sbt:8u222_1.3.5_2.13.1
MAINTAINER NAME EMAIL
RUN apt-get -y update && apt-get -y upgrade && git clone https://github.com/ju851zel/htwg-rummy-game.git && cd htwg-rummy-game && git checkout SA-docker-06 && cd rummy-main-service && sbt run
