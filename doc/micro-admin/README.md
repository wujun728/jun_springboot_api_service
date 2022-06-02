# micro-admin
micro-admin 业务系统快速开发框架

服务基于springboot、springcloud、mybatis搭建的业务系统快速开发框架。页面基于bootstrap。

数据库默认使用mysql，也可以切换为oracle。同时框架还支持使用Groovy开发controller、servicebean、dao层业务逻辑，支持热部署。提高开发效率和部署发布效率。

内置用户登录、权限控制、用户管理、角色管理、字典管理、菜单管理等功能，内置页面组件开发样例，这些通用功能可以直接使用使开发人员集中精力快速开发定制化的业务功能。

项目支持使用groovy开发业务代码，使用的groovy框架是micro-mvc。框架说明和源码见github。https://github.com/jeffreyning/micro-mvc
开发时可以用eclipse和idea
使用idea时注意，需要修改idea配置不编译groovy。file>settings>build,execution,deployment>compiler>resource patterns中删除!?*.groovy;
另外在idea中启动调式时使用spring-boot:run启动否则可能访问不到jsp页面.

eclipse中调式groovy时，需要安装groovy插件（idea不用默认支持），在eclipse中的软件市场搜索即可。

向生产环境部署时，将打出的war包直接执行，java -jar xxx.war
但注意生产环境中项目groovy目录需复制一份到war包外面，并修改application.properties配置文件的两个参数
nhmicro.config.dirFlag=false
nhmicro.config.rulePath=复制的groovy目录绝对路径