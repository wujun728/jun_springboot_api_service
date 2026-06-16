FROM alpine:latest
# 安装Nginx
RUN apk add --no-cache nginx
# 安装JDK 8
RUN apk add --no-cache openjdk8-jre
# 当前目录挂载到容器内的tmp
WORKDIR /
# 将jar包添加到容器中并更名为app.jar
ADD ./ruoyi-admin/target/ruoyi-admin.jar app.jar
# 把自己本地的html里放的前端项目，放入nginx默认的资源目录里
COPY ./ruoyi-ui/dist /usr/share/nginx/html
# 将自己的nginx.conf 配置文件放到docker里nginx默认的配置文件位置
COPY nginx.conf /etc/nginx/nginx.conf
# 将nginx监听的端口6868暴露出来，启动容器实例时通过--net=host不用指定端口映射，会默认使用这个端口
EXPOSE 6868
# 运行jar包，ENTRYPOINT这条命令只能出现一次，如有多条，则只执行最后一条。该命令只有容器启动时才执行
CMD ["sh", "-c", "nohup  java -jar /app.jar --spring.profiles.active=prod & nginx -g 'daemon off;'"]
