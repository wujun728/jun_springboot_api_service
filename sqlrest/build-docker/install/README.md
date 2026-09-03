# SQLREST基于docker-compose的一键部署脚本

**要求**: 准备一个能够安装docker的linux操作系统

## 一、安装docker及docker-compose

```
[root@localhost install]# ls -l
总用量 12
-rw-r--r-- 1 root root 625 2月  11 22:24 docker-compose.yml
-rw-r--r-- 1 root root 787 2月  11 20:54 docker_install.sh
-rw-r--r-- 1 root root 378 2月  11 22:38 README.md

[root@localhost install]# sh docker_install.sh
```

## 二、一键部署dbswitch

```
[root@localhost install]# ls -l
总用量 12
-rw-r--r-- 1 root root 625 2月  11 22:24 docker-compose.yml
-rw-r--r-- 1 root root 787 2月  11 20:54 docker_install.sh
-rw-r--r-- 1 root root 378 2月  11 22:38 README.md

[root@localhost install]# docker-compose up -d
```

## 三、常用操作命令

- 1. 一键创建并启动容器服务:
```
docker-compose up -d
```

- 2. 销毁容器服务: 
```
docker-compose down
```

- 3. 停止容器:
```
docker-compose stop
```

- 4. 重启容器:
```
docker-compose restart
```

- 5. 启动容器:
```
docker-compose start
```

