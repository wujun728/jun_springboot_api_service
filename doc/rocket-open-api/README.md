# rocket-open-api

#### Description
基于rocket-api实现的开放平台，实现代理接口的鉴权，熔断，限流，缓存，请求与返回改写，网关等功能，以及分布式环境下的接口动态管理，不需要重启服务


#### rocket语法

```
//鉴权
//Utils.loadAPI("GET:/cgi-bin/token/validate")

route.init()

.limiter(5,2)   //限流，熔断.每5秒中允许请求2次

.cacheTime(10)    //接口缓存10秒

.requestHeader({item->["masterId":"111"]}) //请求header重写

.requestBody({item->["hello":"hello"]}) //请求体改写

.requestMethod("POST")  //请求方法重写

.to("http://localhost:8080/user/list") //请求地址重写

.responseBody({item->item.data.userId="555";return item.data; })//返回实体改写

.responseHeader({item->     //返回header改写
    item.masterId = "123";
    return item;
})
.done();
```

