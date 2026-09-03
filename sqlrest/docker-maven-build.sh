#!/bin/sh

docker run -it --rm \
	--name my-maven-project \
	-v ~/.m2:/opt/maven/localRepository \
	-v "$PWD":/usr/src/mymaven \
	-w /usr/src/mymaven \
  registry.cn-hangzhou.aliyuncs.com/inrgihc/maven-aliyun:3.6.3-jdk-8 mvn clean package

