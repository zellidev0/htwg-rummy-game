# 1. run docker container
``` 
docker run -it --rm hseeberger/scala-sbt:8u222_1.3.5_2.13.1
```

# 2 run in container
```
apt-get install nano
apt-get install netcat
git clone <URL>
git checkout <branch>
```

# 3. run in other terminal to save current state in rummy-docker
```
docker commit <containerId> rummy-docker
```

u# 4. next time run docker from rummy
```
docker run -it --rm -p 9000:9000 rummy-docker
```

# 5. run docker compose (in dir of docker-compose file)
```
docker-compose up
```


# list all runnning containers
```docker container ls -a```
# export as tar
```docker export rummy-docker > rummydocker.tar```
# create a network
```docker network create rumnet```
