#!/bin/bash

set -e

curl -sSL https://get.docker.com/ | sh
docker --version
mkdir -p /etc/docker/
cat > /etc/docker/daemon.json <<EOF
{
    "registry-mirrors":[
        "https://docker.mirrors.ustc.edu.cn",
        "http://hub-mirror.c.163.com",
        "https://dhyjbe13.mirror.aliyuncs.com"
    ],
    "insecure-registries": ["127.0.0.1/8"],
    "max-concurrent-downloads":10,
    "log-driver":"json-file",
    "log-level":"warn",
    "log-opts":{
        "max-size":"10m",
        "max-file":"3"
    },
    "data-root":"/var/lib/docker"
}
EOF
service docker restart

curl -L https://github.com/docker/compose/releases/download/v2.2.2/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose && chmod +x /usr/local/bin/docker-compose
docker-compose --version

