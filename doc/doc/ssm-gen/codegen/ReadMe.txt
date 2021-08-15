这是一个SSM代码生成器，用来生成基于（Spring，SpringMVC，Mybatis）的javaWeb工程。
技术选型
    本工具选定的技术搭配是 mysql＋mybatis＋springMVC＋maven＋orikaBeanMapper＋tomcat，其中mybatyis、spring,SpringMVC是必须使用的。

当前工具支持的数据库是mysql、支持的构建工具是maven，这两个方向将在后续扩展

工具简介
    本工具是一个java代码生成器，他根据数据库表信息自动生成crud操作的java代码。除此之外，生成工具类、生成工程配置文件。使用工具生成的代码是一个完整的java工程，可以直接在tomcat直接运行，并且可以通过rest接口对数据进行操作。

工具特点

    功能完备
        其他代码生成器大多生成一部分curd代码，需要用户自己拷贝，或者植入正在开发的系统。本工具生成的代码是一个完整的java工程
    独立性
        其他代码生成器大多将代码生成器模块作为某一个框架的一部分，而这些框架一般是笨重的，而且各自有各自的技术选型。用户要比较好的使用这类工具需要使用很多精力来理解框架。本工具没有和指定框架绑定。除了一些必要的工具类（有限的几个）直接植入了util，其他依赖都是常见的，必须的，主流的工具。用户不需要学习就可以看懂代码。
    主流技术
        这套技术选型几乎是国内中型项目的方案标配。作者根据在互联网公司实习经历选定的合适工具。
    无模版
        生成器不使用模版技术，生成的代码都是统一的数据结构描述，然后通过统一格式化工具输出。对输出结构有精确的控制能力，有统一的代码格式。对于必须使用模型转换的场景，工具使用eclipse的代码分析工具进行解析
    数据结构转换
        一个项目中，可能存在不同层次的数据结构，他们服务于不同的处理层。本工具生成的代码定义了三成数据结构，分别是VO，Model，Entity，服务于Controller，Service，Repository。在不同层级的数据结构转换的时候，通过orika自动实现属性拷贝，orika类似于apache的BeanUtils，不过功能要强于后者，他能递归的处理子属性，集合，数组，而且是新建的对象，切断了map前后属性之前的联系。
    数据查询
        工具使用mybatis code generator作为核心，扩展了两个mybatis 生成的方法。主要是selectCount和selectPage，他通过判断传入entity对应属性是否为空来组织查询语句，selectPage能够满足大部分条件查询需求
    代码格式化
        无论是xml文件还是java文件，都可以使用统一的格式化工具对代码格式化。生成的代码在格式上也会有统一的风格
    模型修改/merge
        开发过程中，数据模型的修改是一个不可避免的问题，工具提供一种方式对数据模型进行修改，实现增量开发。当前代码实现了表定义增加，字段定义增加两种功能。（字段定义增加功能尚未测试通过）

运行方式

打包：项目使用maven管理，执行mvn clean package 就可以生成jar包，然后将jar包拷贝到codegen/lib/目录

执行：运行cn.edu.scu.virjarjcd.codegertator.Generator，或者执行bin目录下面的codegen脚本

注意在classpath下面放置config.properties文件

配置

########################################

project configuration
########################################

#生成路径
project.exportPath=/Users/virjar/Desktop/codegentarget
#工程名称
project.name=testcodegen

#包名
project.basePackage=com.virjar

#构建工具，目前只支持maven
project.buildTool=maven
#lombok特性，开启lombok后，会使用lombok来描述pojo，这样可以使的代码更加简洁，特别是对于字段特别多的对象的时候。lombok会自动生成Getter、Setter方法
project.lombok =true
#xml->logback.xml or groovy->logack.groovy

＃日志格式，目前只支持logback.xml的生成

project.logback=xml
#single or multi
project.moduleType=multi
# multi会生成父子工程，single将会生成单项目工程
project.modelLayer=multi
# 如果不想使用orika，可以将这个参数设置为single，数据模型只会有model一层。

########################################
database configuration
########################################
db.driver=com.mysql.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/meiweidi
db.userId=rot
db.pwd=
#数据库名称
db.schema=meiweidi
#表：对象名称，需要配置需要为那些表生成代码，格式为table1:Model1,table2:Model2,table3:Model3
db.tables=data:Data

#########################################
lombok configuration
#########################################
#GetterSetter ToString
lombok.annotations=Data,AllArgsConstructor,NoArgsConstructor

#########################################
formater configuration
#########################################

#mybatis, jcg, astparser,jalopy

#default is mybatis java文件建议使用astparser，xml建议使用jcg，
#astparser支持java1.8的新特性，同时，他是安装eclipse的标准格式化规范来进行代码格式化的。
#对于idea，可以安装Eclipse Code Formatter插件，实现不同类型IDE，不同版本IDE的格式化规则统一
#解决多人开发因为手痒格式化代码引起的大量diff出现问题（astparser格式化逻辑和插件逻辑一致）
#idea插件地址：http://plugins.jetbrains.com/plugin/6546?pr=idea

#mybatis格式化速度最快，因为模型由mybatis数据结构描述
formater.default=mybatis
formater.java=jalopy
formater.mapper=mytatis
formater.pom=jcg
formater.webxml=jcg
formater.spring=jcg

#########################################
logger configuration
logger.pattern=%d{yyyy/MM/dd-HH:mm:ss} %-5level [%thread] %class{5}:%line>>%msg%n
#or ch.qos.logback.core.ConsoleAppender
logger.appender=ch.qos.logback.core.rolling.RollingFileAppender
#default is ${catalina.base}/logs/{appname}/
logger.basefolder=