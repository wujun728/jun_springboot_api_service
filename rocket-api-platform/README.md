# rocket-api-platform
### **服务端面向SQL开发API的低代码平台** 


#### 介绍
统一数据接口平台
来源于rocket-api,可以实现在页面上动态管理多数据源配置，实现数据源的热加载，切换。无需重启
可以用于作统一的数据接口平台

#### 软件架构
基于Rocket-API

1.内置市面上大部分数据库驱动，如果没有你要的数据库， **欢迎PR**     
2.这个项目需要大家的积极贡献，以此来支持更多的数据源            
PR参见：[https://alenfive.gitbook.io/rocket-api/pei-zhi/shu-ju-yuan-pei-zhi-fang-shi-er-1](https://alenfive.gitbook.io/rocket-api/pei-zhi/shu-ju-yuan-pei-zhi-fang-shi-er-1)

#### 安装教程

1.  clone 本项目
2.  表结构创建，非关系性不用，详见：https://alenfive.gitbook.io/rocket-api/shu-ju-ku-chuang-jian-jiao-ben/mysql
3.  初始化 `DefaultDataSourceManager`，这里只需要配置一个数据源，用于程序运行期间的信息存储

```java
/**
 * 默认数据源管理器，主数据源需要手动配置
 */
@Component
public class DefaultDataSourceManager extends DataSourceManager {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        Map<String, DataSourceDialect> dialects = new HashMap<>();
        dialects.put("mysql", new MySQLDataSource(jdbcTemplate, true));
        super.setDialectMap(dialects);
    }
}

```

4.  启动运行后如下，在页面中动态管理其他数据源

![输入图片说明](https://images.gitee.com/uploads/images/2020/1127/191524_ad3aae9d_5139840.png "屏幕截图.png")


5. 其他数据源按照以下方式新增 

![输入图片说明](https://images.gitee.com/uploads/images/2020/1127/202527_48ee63ac_5139840.png "屏幕截图.png")

```yml
spring:
  rocket-api:
    multi-datasource:
      - name: mysql2
        factory-class-name: com.github.alenfive.rocketapi.datasource.factory.MySQLFactory
        config:
          jdbcUrl: jdbc:mysql://127.0.0.1:3306/test
          username: root
          password: root
          driverClassName: com.mysql.cj.jdbc.Driver
      - name: mysql3
        factory-class-name: com.github.alenfive.rocketapi.datasource.factory.MySQLFactory
        config:
          jdbcUrl: jdbc:mysql://127.0.0.1:3306/test
          username: root
          password: root
          driverClassName: com.mysql.cj.jdbc.Driver
      - name: mongodb
        factory-class-name: com.github.alenfive.rocketapi.datasource.factory.MongoFactory
        config:
          url: mongodb://root:123@127.0.0.1:27017/test
```

### 保存刷新，完成数据源的新增

### 参数说明见：https://alenfive.gitbook.io/rocket-api/pei-zhi/shu-ju-yuan-pei-zhi-fang-shi-er-1 
