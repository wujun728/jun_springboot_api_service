######内置tomcat对外服务端口##########
server.port=8080
########################################################
#                  #数据库配置                          #
########################################################
# 下面为连接池的补充设置，应用到上面所有数据源中
# 初始化大小，最小，最大
wanshifu.dataSource.initialSize=5
wanshifu.dataSource.minIdle=5
wanshifu.dataSource.maxActive=20
# 配置获取连接等待超时的时间
wanshifu.dataSource.maxWait=60000
# 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
wanshifu.dataSource.timeBetweenEvictionRunsMillis=60000
# 配置一个连接在池中最小生存的时间，单位是毫秒
wanshifu.dataSource.minEvictableIdleTimeMillis=300000
wanshifu.dataSource.validationQuery=SELECT 1 FROM DUAL
wanshifu.dataSource.testWhileIdle=true
wanshifu.dataSource.testOnBorrow=true
wanshifu.dataSource.testOnReturn=true
# 打开PSCache，并且指定每个连接上PSCache的大小
wanshifu.dataSource.poolPreparedStatements=false
wanshifu.dataSource.maxPoolPreparedStatementPerConnectionSize=20
# 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
wanshifu.dataSource.filters=stat,wall,log4j
# 通过connectProperties属性来打开mergeSql功能；慢SQL记录
wanshifu.dataSource.connectionProperties=servlets.stat.mergeSql=true;servlets.stat.slowSqlMillis=5000
# 合并多个DruidDataSource的监控数据
#wanshifu.dataSource.useGlobalDataSourceStat=true
########################################################
#                  Mybatis配置#                         #
########################################################
mybatis.mapper-locations=classpath*:mapper/*.xml
mybatis.type-aliases-package=com.soukuan.domains
########################################################
#                 通用mapper配置                        #
########################################################
#mappers 多个接口时逗号隔开
mapper.mappers=${basePackage}.framework.persistence.base.IBaseCommMapper
mapper.not-empty=false
mapper.identity=MYSQL
#########################################################
#               pagehelper分页配置                       #
#########################################################
pagehelper.helperDialect=mysql
pagehelper.reasonable=true
pagehelper.supportMethodsArguments=true
pagehelper.params=count=countSql
#########################################################
#                      日志配置                          #
#########################################################
logging.config.classpath=classpath:logback-spring.xml
##########################################################
#                      业务参数配置                       #
##########################################################

##########################################################
#                       log文件路径                       #
##########################################################
logging.path=/data/servicesLog/tomcat-${projectName}/logs/


##########################################################
#                        监控配置                         #
##########################################################
#打开actuator监控
management.security.enabled=false
#取消javamelody代理
javamelody.advisor-auto-proxy-creator-enabled=false
###########################################################
#           profile配置对应环境的properties文件            #
###########################################################
#代码提交时取消该注释，并注释调下面的local。@deploy.env@占位符会根据实际环境指定配置文件
spring.profiles.active=@deploy.env@