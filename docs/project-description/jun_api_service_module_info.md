# jun_api_service 模块信息

> 信息收集任务 T002 产出文档，供后续文档编写引用。

## 基本信息

| 项目 | 值 |
|------|-----|
| GroupId | `io.github.wujun728` |
| ArtifactId | `jun_api_service` (父) / `jun_api_online_groovy` (主模块) |
| Version | `1.0` |
| Packaging | 父 pom，子 jar (SpringBoot repackage) |
| Java 版本 | 1.8 |
| 主入口类 | `io.github.wujun728.ApiServiceApplication` |
| 端口 | 8888 |
| 应用名 | `groovy_api_service` |
| API 上下文 | `/api` |

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 框架 | Spring Boot | 2.5.14 |
| 模板引擎 | Thymeleaf | (由 Spring Boot 管理) |
| 模板引擎 | Freemarker | (由 Spring Boot 管理) |
| ORM | MyBatis-Plus | 3.5.3.1 |
| 数据源 | Druid | 1.2.16 |
| 数据库驱动 | MySQL Connector/J | (由 Spring Boot 管理) |
| 数据库驱动 | SQLite JDBC | (由 Spring Boot 管理) |
| 缓存/会话 | Spring Data Redis | (由 Spring Boot 管理) |
| 动态语言 | Groovy | 2.4.7 |
| 核心 Starter | jun-groovy-api-spring-boot-starter | 1.0.23 |
| API 文档 | Springfox Swagger2 + Swagger UI | 2.8.0 |
| API 文档 | Knife4j | 2.0.2 |
| 认证 | java-jwt (Auth0) | 4.4.0 |
| 工具库 | Hutool | 5.8.18 |
| 工具库 | FastJSON | 1.2.74 |
| 工具库 | Commons Lang | 2.6 |
| 工具库 | Commons IO | 2.11.0 |
| 工具库 | Commons Configuration | 1.10 |
| 工具库 | Commons DbUtils | 1.7 |
| 代理 | CGLIB | 3.3.0 |
| 邮件 | Spring Boot Mail | (由 Spring Boot 管理) |
| 定时任务 | Spring @EnableScheduling | (内置) |
| 配置加密 | Jasypt (PBEWithMD5AndDES) | — |
| 代码简化 | Lombok | (由 Spring Boot 管理) |
| 验证 | Spring Boot Validation | (由 Spring Boot 管理) |

## 依赖版本汇总 (pom.xml properties)

```properties
springfox.version=2.8.0
groovy.version=2.4.7
mybatis-plus.version=3.5.3.1
mybatis-plus-dynamic.version=2.5.5  # 注释掉，未启用
ojdbc6.version=12.1.0.1-atlassian-hosted  # 注释掉
sqlserver.version=4.0  # 注释掉
commons.lang.version=2.6
commons.io.version=2.11.0
commons.configuration.version=1.10
velocity.version=1.7
quartz.version=2.3.2
druid.version=1.2.16
fastjson.version=1.2.74
knife4j.version=2.0.2
easy-captcha.version=1.6.2
```

## 功能列表

### 核心功能
1. **Groovy 动态 API 生成** — 通过 `jun-groovy-api-spring-boot-starter` 实现运行时动态加载和注册 API
2. **多语言脚本支持** — SQL / Groovy / Java Class 三种模式
3. **热重载** — 无需重启 JVM，通过刷新端点重新加载 API 配置
4. **动态 Bean 注册** — GroovyClassLoader 编译源码 → 注册为 Spring Bean → 注册 RequestMapping

### API 控制器
5. **ApiSqlController** — 基于 SQL 的 CRUD API（list / page / one / update / insert / delete）
6. **App1Controller / App2Controller2** — 应用级控制器
7. **TestController111** — 测试控制器
8. **HomeController** — 首页/示例控制器

### 基础设施
9. **CORS 跨域** — `CrossConfig` 全局 CORS 配置，支持所有域名
10. **认证过滤器** — `AuthFilter` 基于 Session 的认证（白名单路径 `/admin/login`）
11. **API Header 过滤器** — `ApiHeaderFilter` 请求头处理
12. **WebMvc 配置** — `WebMvcConfig` Web 层配置
13. **Redis Token 管理** — 支持 JWT token 存储，7 天过期，支持多端登录
14. **多数据库支持** — MySQL（默认）、Oracle、SQL Server（驱动已注释，需手动启用）
15. **Swagger/Knife4j API 文档** — 自动生成 API 文档
16. **邮件服务** — Spring Boot Mail
17. **定时任务** — @EnableScheduling
18. **配置加密** — Jasypt 加密敏感配置

### 配置特点
19. **HTTP/2 支持** — `server.http2.enabled: true`
20. **多 profile 配置** — `spring.profiles.active: test`
21. **MyBatis-Plus 逻辑删除** — deleted 字段，0=删除/1=未删除
22. **文件上传** — 单文件 10MB，总请求 100MB

## 源码结构

```
src/main/java/io/github/wujun728/
├── ApiServiceApplication.java          # 主启动类
├── amis/api/
│   ├── ApiSqlController.java           # SQL API 控制器
│   ├── app/controller/
│   │   ├── App1Controller.java
│   │   ├── App2Controller2.java
│   │   └── TestController111.java
│   └── core/
│       ├── config/
│       │   ├── CrossConfig.java        # CORS 配置
│       │   └── WebMvcConfig.java       # MVC 配置
│       └── filter/
│           ├── ApiHeaderFilter.java    # API 头过滤器
│           └── AuthFilter.java         # 认证过滤器
└── sql/
    ├── App.java
    ├── DataSourceConfig.java           # 数据源配置
    ├── controller/
    │   └── HomeController.java
    └── entity/
        └── Student.java                # 示例实体
```

## 构建信息

- Maven 编译插件: 3.8.0
- Spring Boot Maven 插件: 2.6.2 (repackage)
- Maven 仓库: 阿里云镜像 (`maven.aliyun.com`)
- 发布仓库: Sonatype OSSRH (Maven Central)

## 注意事项

1. 源码位于 `doc1/jun_api_service/` 目录下（非顶层目录）
2. 核心动态 API 逻辑封装在 `jun-groovy-api-spring-boot-starter` 中（已发布到 Maven Central）
3. Oracle/SQL Server 驱动已注释，需要时手动启用
4. 多数据源 (`dynamic-datasource`) 已注释，未启用
5. `AuthFilter` 未通过 `@WebFilter` 注解启用，需检查实际过滤器注册方式
