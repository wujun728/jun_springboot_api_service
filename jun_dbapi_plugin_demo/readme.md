# DBAPI插件开发案例

[DBAPI](https://www.51dbapi.com) 插件开发案例

## 概述

## [DBAPI插件开发指南](https://www.51dbapi.com/v4.0.0/plugin/)

## 使用方式
```xml
<dependency>
    <groupId>com.gitee.freakchicken.dbapi</groupId>
    <artifactId>dbapi-plugin</artifactId>
    <version>4.0.16</version>
    <scope>provided</scope>
</dependency>
```

## 注意
> 插件编写需要手动注册

在`resources`目录下新建文件夹`META-INF`,再在`META-INF`文件夹下新建`services` 文件夹

在`META-INF/services`目录下新建文件`com.gitee.freakchicken.dbapi.plugin.CachePlugin`，并在此文件中填写编写的缓存插件的java类名

在`META-INF/services`目录下新建文件`com.gitee.freakchicken.dbapi.plugin.TransformPlugin`，并在此文件中填写编写的数据转换插件的java类名

在`META-INF/services`目录下新建文件`com.gitee.freakchicken.dbapi.plugin.AlarmPlugin`，并在此文件中填写编写的告警插件的java类名

在`META-INF/services`目录下新建文件`com.gitee.freakchicken.dbapi.plugin.GlobalTransformPlugin`，并在此文件中填写编写的全局数据转换插件的java类名

在`META-INF/services`目录下新建文件`com.gitee.freakchicken.dbapi.plugin.ParamProcessPlugin`，并在此文件中填写编写的参数处理插件的java类名