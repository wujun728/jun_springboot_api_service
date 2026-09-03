
```
$ curl -s -XPOST -d 'clientId=test&secret=test' 'http://127.0.0.1:8092/token/generate'
{
    "code":0,
    "message":"success",
    "data":{
        "realName":"测试",
        "appKey":"test",
        "accessToken":"fc99c78b19c49c861f119dd8959ab3d4",
        "expireSeconds":7200
    }
}


$ curl -s -XGET -H 'Authorization:Bearer fc99c78b19c49c861f119dd8959ab3d4' 'http://127.0.0.1:8092/api/test?id=1'
{
    "code":0,
    "data":[
    ],
    "message":"success"
}

```