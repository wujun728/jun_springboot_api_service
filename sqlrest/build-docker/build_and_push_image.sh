#!/bin/sh

set -e 

SQLREST_VERSION=1.0.0
BUILD_DOCKER_DIR="$( cd "$( dirname "$0"  )" && pwd  )"
PROJECT_ROOT_DIR=$( dirname "$BUILD_DOCKER_DIR")
DOCKER_SQLREST_DIR=$BUILD_DOCKER_DIR/sqlrest

# build project
cd $PROJECT_ROOT_DIR && sh docker-maven-build.sh && cd -

# copy files
cd $BUILD_DOCKER_DIR \
 && tar zxvf $PROJECT_ROOT_DIR/target/sqlrest-release-${SQLREST_VERSION}.tar.gz -C /tmp \
 && cp -r /tmp/sqlrest-release-${SQLREST_VERSION}/lib/* ${BUILD_DOCKER_DIR}/sqlrest/sqlrest-release/lib/ \
 && cp -r /tmp/sqlrest-release-${SQLREST_VERSION}/drivers/* ${BUILD_DOCKER_DIR}/sqlrest/sqlrest-release/drivers/ \
 && rm -rf /tmp/sqlrest-release-*

# build image
cd ${DOCKER_SQLREST_DIR} && tar zcvf sqlrest-release.tar.gz sqlrest-release/

docker build -f Dockerfile-manager -t inrgihc/sqlrest-manager:${SQLREST_VERSION} .
docker build -f Dockerfile-executor -t inrgihc/sqlrest-executor:${SQLREST_VERSION} .
docker build -f Dockerfile-gateway -t inrgihc/sqlrest-gateway:${SQLREST_VERSION} .

rm -f sqlrest-release.tar.gz && rm -rf sqlrest-release/lib/* && rm -rf sqlrest-release/drivers/*

# clean project
cd $PROJECT_ROOT_DIR && sh docker-maven-clean.sh && cd -

# login and push docker image
docker push registry.cn-hangzhou.aliyuncs.com/inrgihc/sqlrest-manager:${SQLREST_VERSION}
docker push registry.cn-hangzhou.aliyuncs.com/inrgihc/sqlrest-executor:${SQLREST_VERSION}
docker push registry.cn-hangzhou.aliyuncs.com/inrgihc/sqlrest-gateway:${SQLREST_VERSION}

