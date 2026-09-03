FROM hbt.zjs.com.cn/base/nginx:1.18.0-alpine
# 设置时区为上海
# RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
# 描述
MAINTAINER atliwen
## 打包文采后的路径
COPY nginx.conf  /etc/nginx/nginx.conf
COPY dist  /usr/share/nginx/html/

COPY amis /usr/share/amis/html/
