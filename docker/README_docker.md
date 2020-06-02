# run docker container

``` 
docker run -it --rm hseeberger/scala-sbt:8u222_1.3.5_2.13.1
```

# run in container
```
apt-get install nano
```

# list all running containers
```
docker container ls -a
```

# save current container state
```
docker commit <containerId> rummy-docker

```

# next time run docker from rummy
```
docker run -it --rm rummy-docker
docker run -it --rm --net=rumnet rummy-docker
```

# export as tar
```
docker export rummy-docker > rummydocker.tar
```

# create a network
```
docker network create rumnet
```