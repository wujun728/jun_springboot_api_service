<p align="center">
	<img alt="logo" src="https://oscimg.oschina.net/oscnet/up-fbd2a2963d9259e9684db9ed5b8392b2beb.png">
</p>

<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">fastapi v2.0.0</h1>

## 平台目标

> 1. 通过对接口的统一管理、统一配置、统一调用,来满足公司日益变化的接口需求<br>
> 2. 通过配置,实现零开发,零发布,零运维,从而让开发人员多一些快乐,少一些加班

## 平台架构
> 本平台基于ruoyi 3.8.2进行开发<br>
> 前端采用Vue、Element UI。<br>
> 后端采用Spring Boot、Spring Security、Redis & Jwt。
 
## 操作文档

### 基础配置
#### 数据源配置

mysql连接url

```
mysql连接url: jdbc:mysql://xxx:3306/xxx?characterEncoding=utf8&useUnicode=true&useSSL=false&autoReconnect=true&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true&allowPublicKeyRetrieval=true
```  

oracle连接url

```
oracle连接url: jdbc:oracle:thin:@xxx:1521/orcl
```  

postgresql连接url

```
oracle连接url: jdbc:postgresql://xxx:5432/xxx
```  

sqlserver连接url

```
sqlserver连接url: jdbc:sqlserver://xxx:1433;DatabaseName=xxx
```  

>  如果你需要检查数据库连接,可点击一键自检对数据库健康度进行检查<br>
>  oracle jar需要自己手动添加至maven


#### 应用管理

> 为了方便管理,一个大屏页面为一个应用


### 接口配置
> 查询接口配置

1. 根据sql语句结果,配置相对应的结果类型

2. 根据业务和实际接口请求时长,决定是否开启缓存

3. 接口描述最好填写,方便日后进行维护(最好直接为页面指标项)


### 参数传递


1. get请求直接在url中拼接数据即可

2. post请求在body中以json方式传参,在配置sql中可变参数以 [\`user_name={userName}\`]此形式进行配置
如:select * from sys_user where [\`user_name={userName}\`]当前台传入userName语句时会自动拼接上[]中的语句** 

3. 模糊查询用法select * from sys_user where [\`user_name like %{userName}%\`]

>  中文只支持post请求<br>
>  oracle jar需要自己手动添加至maven参考中


 
### 接口调用

```
{ip}:{端口}/reverse/{应用前缀}/{接口名称} 
```  

### 接口合并调用
> 目前通过接口平台进行配置后,每一个接口为一个单独指标项,这就意味着前端在进行调用时<br>
> 需要调用大量的后台接口,所以为了前端调用遍历,故有了此功能<br>
> 接口进行合并调用时需要注意,只能是同一个应用下的接口才能够进行合并调用,且同时最多合并五个接口

```
{ip}:{端口}/reverse/{应用前缀}/{接口名称1},{接口名称2},{接口名称3}... 
```  

### 接口权限控制
> 接口平台目前支持登录调用/密钥调用/系统调用(定时任务)/开放调用四种权限控制<br>
> 可通过系统管理->参数设置,进行灵活的权限配置

#### 登录调用
head中必须带有系统用户的token才能进行调用,当不填写authType时,默认为登陆调用

```
head
Authorization:Bearer xxx
authType:authToken
```

#### 密钥调用
接口平台支持第三方系统,使用密钥进行调用,调用前对接口进行授权

```
head
authType:authCode
clientId:xxx
clientSecret:xxx
```

#### 系统调用
由内部系统定时任务发起的数据落地操作

#### 开放调用
接口平台不进行权限控制,只需将系统管理->参数设置->开启登录校验设置为off即可

### 接口服务
> 接口平台提供第三方应用,以密钥方式进行对接,可单独对每个应用进行细粒度接口权限控制

#### 应用管理
> 创建第三方应用,同时用于管理员进行授权操作

#### 调用历史
> 记录接口调用历史,可在(参数设置->调用历史-开启调用历史)中设置开启或关闭

### 接口数据落地
> 在ScheduleTask类中,给出了数据落地的调用方法,根据实际情况进行开发即可

### 数据转换

#### kv转换

内容格式：

```
{"name":"返回字段名称","value":"返回字段名称"}
```

返回格式：

```
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "result": [
            {
                "name": 202110,
                "value": 1078085
            },
            {
                "name": 202111,
                "value": 1136770
            }
        ]
    }
}
```

#### 行列转换


内容格式：

```
{"name":"返回字段名称","value":"返回字段名称"}
```

返回格式：

```
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "result": {
            "202112": 1238529,
            "202201": 1202956
        }
    }
}
```


#### key提取


内容格式：

```
{"key":"返回字段名称"}
```

返回格式：

```
{
    "msg": "操作成功",
    "code": 200,
    "data": {
        "result": [
            {
                "202110": {
                    "VAL": 1078085,
                    "MONTH": 202110,
                    "CANTCODE": "370000",
                    "CANTNAME": "山东省"
                }
            },
            {
                "202111": {
                    "VAL": 1136770,
                    "MONTH": 202111,
                    "CANTCODE": "370000",
                    "CANTNAME": "山东省"
                }
            }
        ]
    }
}
```

## 演示图
<table>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-9360ffc92975b4dd9e5b317c028e0982b84.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-6632849a1100afd86449a0eff6f80ed0f9c.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-4e92700afd01f768086ffad84335eb54f17.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-7f362f7b17252817fef1a35db19fd4b294d.png"/></td>
    </tr>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-63c31873ef90bf3a9e761f9d95f926609a0.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-299b3b31db276f65614f63a5b54d9205a4c.png"/></td>
    </tr>
</table>
 
## 捐献支持
<table>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-5a3c08e665bfe52ee69808fa61d13e6d953.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-905e15d29d9a07b7ba4b0b99f4cd9f9751b.png"/></td>
    </tr>
</table>
 
