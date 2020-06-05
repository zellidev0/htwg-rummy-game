docker network create rumnet
docker run -it --net rumnet --net-alias main --rm -p 9000:9000 rummy-docker
docker run -it --net rumnet --net-alias game --rm -p 9001:9001 rummy-docker
docker run -it --net rumnet --net-alias player --rm -p 9002:9002 rummy-docker
