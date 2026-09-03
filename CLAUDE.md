# CLAUDE.md

请始终使用使用中文与我对话，输入中文，返回中文。

本文件为 Claude Code (claude.ai/code) 在此仓库中工作时提供指导。

## 仓库概述

**一句话：** 本仓库是围绕「把数据库 / 脚本快速变成 HTTP API」的**低代码 / 零代码 API 平台**单体仓库；**当前可构建、可运行的主工程是 `jun_dbapi`（DBApi v2.3.1 定制版）**。

**解决什么问题：** 让团队在不手写传统 Controller 的前提下，通过配置 SQL（及插件做转换、缓存、告警）快速对外提供 REST 接口。典型场景：BI 报表/数据大屏后端、前后端联调临时接口、企业数据资产 HTTP 化发布与统一管理。

根目录 `pom.xml` 当前**仅激活** `<module>jun_dbapi</module>`。下列能力在文档或其它仓库中有说明，但**本工作区目录中通常不存在完整源码**（修改前请先 `ls` 确认）：

| 名称 | 说明 | 本仓库 |
|------|------|--------|
| **jun_dbapi** (v2.3.1) | 零代码 SQL → HTTP API（单机） | ✅ 主工程 |
| **jun-groovy-api-spring-boot-starter** | Groovy/SQL/Java 动态 API Starter | 📄 见根 `README.md`；Starter 见 `jun_dbapi_spring_boot_starter` 等 |
| **jun_api_service** | Groovy 动态 API 完整应用 | ❌ 未包含（路径见下文「关联生态」） |
| **dbapi/db-api-4.0** | DBApi 集群版 (v4.0) | ❌ 未包含；对比任务见 `README_AI.md` |

三者概念相近（动态 SQL、缓存、转换），架构与部署方式不同，**彼此无直接 Maven 依赖**。

---

## 本仓库目录与职责

```
jun_api_service_main/              # 本仓库根（groupId: io.github.wujun728）
├── jun_dbapi/                     # ★ 主工程 DBApi 2.3.1
│   ├── dbapi-core/                # Spring Boot 后端（端口 8520）
│   ├── dbapi-ui/                  # 原 Vue 前端（需 Node 构建）
│   ├── jun_ui_sa_admin/           # 新前端：静态页 + sa-admin，Nginx 部署
│   ├── dbapi-plugin/              # 插件接口（Cache/Transform/Alarm 等）
│   ├── jun-dbapi-plugin/          # 插件实现（从 dbapi/*.src 搬迁）
│   ├── dbapi-common/
│   └── dbapi-assembly/            # 打包、bin 脚本、sql 初始化
├── dbapi/                         # 插件反编译/参考源码（*.src）
├── jun_dbapi_spring_boot_starter/ # 将 DBApi 嵌入其它 Spring Boot 项目
├── jun_dbapi_spring_boot_starter_demo/
├── jun_dbapi_plugin_demo/
├── long-running-agent/            # 长时运行 AI 智能体框架（与 DBApi 弱耦合）
├── README.md                      # jun-groovy-api-starter 使用说明
├── README_AI.md                   # 前端迁移、插件搬迁、4.0 对比等任务记录
└── pom.xml                        # 父 POM，modules: jun_dbapi
```

**jun_dbapi 请求执行流程（简图）：**

```
管理端配置 SQL + 参数 → 元库 api_config
        ↓
HTTP 请求 → 缓存插件命中? → 是 → 返回 JSON
        ↓ 否
解析动态 SQL → 执行 JDBC → 转换/告警插件 → 写缓存 → 返回
```

**近期本仓库常见改动方向**（见 `README_AI.md`、git 状态）：`dbapi-ui` → `jun_ui_sa_admin` 前端迁移；`dbapi/*.src` → `jun-dbapi-plugin` 插件兼容搬迁；加密/密码工具类；CodeMirror SQL 编辑体验。

---

## 关联生态（可能不在本仓库）

完整 **Groovy 动态 API 应用** 与 **DBApi 4.0 集群** 常在其它路径维护，本文件仍保留其构建/架构说明供对比与升级参考：

```
# 典型外部路径（勿假定已 clone 到本仓库）
jun_api_service/
├── jun_api_online_groovy/         # ApiServiceApplication，端口 8888
├── jun_api_online_groovy_ui/
└── jun_layui_jfinal/

dbapi/db-api-4.0/
├── dbapi-standalone/
├── dbapi-cluster-gateway/
├── dbapi-cluster-manager/
└── dbapi-cluster-apiServer/
```

---

## 构建命令

### jun_api_service (Groovy API 平台) — 本仓库通常无此目录

> 以下命令适用于**已单独 clone** 的 `jun_api_service` 仓库；在本 `jun_api_service_main` 中请先确认目录是否存在。

```bash
# 进入项目根目录
cd jun_api_service/

# 使用 Maven 构建
mvn clean package

# 运行主应用程序
cd jun_api_online_groovy/
mvn spring-boot:run

# 运行测试
mvn test
```

**主入口点：** `io.github.wujun728.ApiServiceApplication`
**端口：** 8888
**Swagger UI：** http://localhost:8888/swagger-ui.html
**API 上下文：** `/api`

### jun_dbapi (v2.3.1) — 本仓库默认构建目标

```bash
# 从仓库根目录构建（推荐，与根 pom.xml 一致）
cd jun_api_service_main/
mvn clean package

# 或仅进入 jun_dbapi
cd jun_dbapi/
mvn clean package

# 仅运行后端（开发模式）
cd jun_dbapi/dbapi-core/
mvn spring-boot:run

# 原 Vue 前端（开发模式，需 Node）
cd jun_dbapi/dbapi-ui/
npm install
npm run serve

# 新前端 jun_ui_sa_admin：静态资源，由 Nginx 托管（无 npm build）
# 示例：http://dbapi.localhost/ ，配置见 README_AI.md

# 生产部署（tar.gz）
# mvn package 后，使用 dbapi-assembly/bin/ 中的脚本
sh dbapi-assembly/bin/dbApi.sh start         # Linux - 前台运行
sh dbapi-assembly/bin/dbApi.sh -d start      # Linux - 后台运行
sh dbapi-assembly/bin/dbApi.sh stop          # Linux - 停止
dbapi-assembly\bin\dbApi.bat                 # Windows

# Docker 部署
docker run -d -p 8520:8520 freakchicken/db-api

# 更新子模块版本（修改父版本时）
mvn -N versions:update-child-modules
```

**主入口点：** `com.gitee.freakchicken.dbapi.DBApiApplication`
**后端端口：** 8520
**前端端口（开发）：** 8521
**默认凭据：** admin/admin

### dbapi/db-api-4.0 (集群版本) — 本仓库通常无此目录

> 集群版源码与 `jun_dbapi` 老版本对比说明见 `README_AI.md`。

```bash
# 进入 db-api-4.0 根目录（需单独 clone）
cd dbapi/db-api-4.0/

# 构建整个集群系统
mvn clean package

# 集群部署
cd dbapi-assembly/bin/
sh start-all.sh        # 启动所有集群组件
sh status-all.sh       # 检查状态
sh stop-all.sh         # 停止所有组件

# Docker 构建和推送
cd dbapi-assembly/
mvn docker:build
mvn docker:push
```

---

## 架构概览

### 1. jun_api_service - Groovy 动态 API 生成

**技术栈：**
- SpringBoot 2.5.14
- Groovy 2.4.7
- MyBatis-Plus 3.5.3.1
- Druid 1.2.16
- Redis（缓存、令牌管理）
- JWT 认证
- Knife4j 2.0.2（Swagger UI）

**核心机制：**

1. **动态代码加载：**
   - Groovy/Java 源代码 → `GroovyClassLoader.parseClass()` → Java 类
   - 注册为 Spring Bean：`BeanDefinitionRegistry.registerBeanDefinition()`

2. **动态映射注册：**
   - 运行时创建 RequestMapping：`RequestMappingHandlerMapping.registerMapping()`
   - 注销旧映射：`RequestMappingHandlerMapping.unregisterMapping()`

3. **多语言支持：**
   - **SQL 模式：** MyBatis 风格的动态 SQL 执行
   - **Class 模式：** 实现 `IExecutor` 接口，动态编译 Groovy/Java
   - **Groovy 模式：** 通过 Groovy 引擎执行脚本
   - **Jython（待开发）：** Python 脚本执行
   - **JavaScript（待开发）：** 通过 ScriptEngineManager 执行 JS
   - **JRuby（待开发）：** Ruby 脚本执行

**关键配置：**
```yaml
project:
  groovy-api:
    enable: true
    servicename: jun-engine-api
    context: /api
    datasource: [可配置]
```

**数据库模式：**
- 主表：`api_config` - 存储 API 定义（路径、bean 名称、源代码、脚本类型）
- 启动时从 `api_config` 加载所有配置并注册为 bean

**刷新机制：**
- 无需重启 JVM 的热重载
- 调用刷新端点重新加载修改后的 API 配置

### 2. jun_dbapi - 零代码 SQL API (v2.3.1)

**技术栈：**
- SpringBoot 2.1.3.RELEASE
- SQLite（默认元数据库）或 MySQL
- Orange 动态 SQL 引擎
- EHCache 结果缓存
- 前端二选一：**dbapi-ui**（Vue 2.6 + Element-UI，需 Node）或 **jun_ui_sa_admin**（静态 HTML + sa-admin，Nginx）
- 多数据库支持：MySQL、PostgreSQL、SQL Server、Oracle、Hive、Kylin、ClickHouse

**插件系统：**

位于 `dbapi-plugin/src/main/java/com/gitee/freakchicken/dbapi/plugin/`：

- **CachePlugin 接口：** 自定义缓存实现（Redis、MongoDB、Elasticsearch）
  - `get(key)` - 从缓存中检索
  - `put(key, value, expire)` - 带 TTL 的存储
  - `invalid(key)` - 失效缓存条目

- **TransformPlugin 接口：** 数据转换（脱敏、加密、格式化）
  - `transform(jsonResult, params)` - 转换结果 JSON

插件通过 `PluginManager` 动态加载，并在 UI 中按 API 配置。

**数据库模式（SQLite/MySQL）：**

```sql
-- 核心表
api_config        -- API 定义（SQL 模板、参数、插件）
datasource        -- 数据库连接
token             -- 访问令牌
auth_user         -- 用户
api_group         -- API 分组
api_auth          -- API 级别权限
ip_white          -- IP 白名单
ip_black          -- IP 黑名单
alarm             -- 告警配置
```

**执行流程：**
1. 通过 UI 配置 API：SQL 模板 + 参数（JSON）+ 插件
2. 存储在元数据库
3. HTTP 请求时：
   - 首先检查缓存插件
   - 如果未命中：解析带参数的 SQL → 执行 → 转换 → 缓存 → 返回

**认证：**
- 基于 JWT 令牌的访问控制
- `JwtAuthenticationInterceptor` 验证令牌
- 基于组的 API 权限
- IP 白名单/黑名单防火墙

**关键控制器：**
- `ApiConfigController` - API 增删改查
- `DataSourceController` - 数据源管理
- `FirewallController` - IP 控制
- `TokenController` - 令牌管理
- `TableController` - 数据库内省

### 3. dbapi/db-api-4.0 - 集群版本

**架构：**
- **Gateway：** 负载均衡、路由、限流
- **Manager：** 集中式集群配置管理
- **API Servers：** 分布式执行节点
- **Standalone：** 单实例部署选项

**增强功能：**
- 多节点水平扩展
- 集中管理控制台
- 高级监控和日志
- 高可用性支持

---

## 重要实现细节

### 动态 Bean 注册（jun_api_service）

在 `jun_api_online_groovy` 中添加/修改 API 时：

1. **解析源代码：**
   ```java
   Class clazz = groovyClassLoader.parseClass(groovyInfo.getGroovyContent());
   ```

2. **注册为 Spring Bean：**
   ```java
   BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
   BeanDefinition beanDefinition = builder.getBeanDefinition();
   registry.registerBeanDefinition(groovyInfo.getBeanName(), beanDefinition);
   ```

3. **注册 HTTP 映射：**
   ```java
   requestMappingHandlerMapping.unregisterMapping(mappingInfo);  // 移除旧的
   requestMappingHandlerMapping.registerMapping(mappingInfo, mappingFactory, targetMethod);
   ```

### 插件开发（jun_dbapi）

创建自定义插件：

1. 实现 `CachePlugin` 或 `TransformPlugin` 接口
2. 将 JAR 放入 `plugins/` 目录
3. 在 API 配置 UI 中配置插件类名
4. 插件参数以 JSON 格式传递

示例插件仓库：
- 内存缓存：`memory-cache-plugin-1.0.0.zip`
- 邮件告警：`email-alarm-plugin-1.0.0.zip`
- AES 解密：`aes-param-decrypt-plugin-1.0.0`
- 更多插件示例见 `dbapi/` 目录

### 多数据库支持

两个平台都通过 JDBC 支持多种数据库：
- MySQL、PostgreSQL、SQL Server、Oracle（需要手动将驱动放入 `lib/` 目录）
- Hive、Kylin、ClickHouse
- 任何具有 JDBC 驱动的数据库

**注意：** 对于 Oracle，需手动将 `ojdbc` 驱动复制到 `lib/` 目录。

### 动态 SQL 语法（jun_dbapi）

使用 MyBatis 风格的动态 SQL：

```sql
SELECT * FROM users
WHERE 1=1
<#if name??>
  AND name = #{name}
</#if>
<#if age??>
  AND age > #{age}
</#if>
```

在 UI 中以 JSON 定义参数：
```json
[
  {"name": "name", "type": "String", "required": false},
  {"name": "age", "type": "Integer", "required": false}
]
```

---

## 配置文件

### jun_api_service
- **主配置：** `jun_api_service/jun_api_online_groovy/src/main/resources/application.yml`
- **数据库：** 可配置数据源（MySQL、Oracle、SQL Server）
- **Redis：** 令牌存储和缓存
- **Groovy API 设置：** `project.groovy-api.*`

### jun_dbapi
- **主配置：** `jun_dbapi/dbapi-core/src/main/resources/application.properties`
- **元数据库：** SQLite（默认）或外部 MySQL
- **缓存配置：** `ehcache.xml`
- **SQL 初始化脚本：** `dbapi-assembly/sql/`（SQLite 和 MySQL 版本）

### dbapi/db-api-4.0
- **集群配置：** 每个组件的 `conf/application.properties`
- **网关设置：** 端口、路由规则
- **管理器设置：** 集群协调

---

## 关键依赖

### jun_api_service
- `jun-groovy-api-spring-boot-starter:1.0.23` - 核心 Groovy API starter（已发布到 Maven 中央仓库）
- MyBatis-Plus 3.5.3.1
- Druid 1.2.16
- Groovy 2.4.7

### jun_dbapi
- Orange 动态 SQL 引擎（外部依赖，需手动安装：从 https://gitee.com/freakchicken/orange 执行 `mvn install`）
- EHCache
- Vue.js 2.6.11 + Element-UI 2.4.5

---

## 测试

### jun_api_service

```bash
# 运行所有测试
cd jun_api_service/jun_api_online_groovy/
mvn test

# 通过 Swagger 手动测试 API
# 1. 启动应用：mvn spring-boot:run
# 2. 打开：http://localhost:8888/swagger-ui.html
# 3. 使用 Knife4j 界面测试 API
```

### jun_dbapi

```bash
# 运行后端测试
cd jun_dbapi/dbapi-core/
mvn test

# 手动 API 测试
# 1. 启动后端：mvn spring-boot:run
# 2. 访问 UI：http://localhost:8520
# 3. 创建 API，配置 SQL，在内置 SQL 编辑器中测试
```

---

## 常见工作流

### 添加新 API（jun_api_service）

1. 在 `api_config` 表中插入记录：
   - `path`：HTTP 端点（例如 `/api/users/list`）
   - `bean_name`：唯一的 bean 标识符
   - `groovy_content`：源代码（SQL/Groovy/Java）
   - `script_type`：SQL/Class/Groovy

2. 调用刷新端点重新加载配置

3. API 立即在配置的路径可用

### 添加新 API（jun_dbapi）

1. 登录 UI：http://localhost:8520（admin/admin）
2. 导航到"API 管理" → "新建 API"
3. 配置：
   - 路径、名称、描述
   - 选择数据源
   - 编写 SQL（支持动态 SQL 语法）
   - 定义参数（JSON）
   - 可选：添加缓存插件、转换插件
4. 点击"发布"
5. 在内置请求工具中测试

### 热重载（jun_api_service）

无需重启服务器：
1. 修改数据库中的 API 源代码
2. 调用刷新端点：`GET /api/refresh`
3. 更新的 API 立即可用

### 插件安装（jun_dbapi）

1. 构建插件 JAR（实现 `CachePlugin` 或 `TransformPlugin`）
2. 将 JAR 复制到 `plugins/` 目录
3. 重启应用
4. 在 API 编辑 UI 中配置插件（输入完全限定的类名）

---

## 部署说明

### 生产部署（jun_dbapi）

**选项 1：Tar.gz**
1. 构建：`mvn clean package`
2. 解压：`tar -xzf dbApi.tar.gz`
3. 配置：编辑 `conf/application.properties`
4. 启动：`sh bin/dbApi.sh -d start`

**选项 2：Docker**
```bash
docker run -d -p 8520:8520 freakchicken/db-api
```

**元数据库：** 默认是 SQLite（嵌入式）。对于多实例生产环境，使用外部 MySQL：
```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/dbapi?useSSL=false
spring.datasource.username=root
spring.datasource.password=password
```

从 `sql/` 目录运行初始化脚本。

### 集群部署（dbapi 4.0）

1. 构建所有模块：`mvn clean package`
2. 首先部署 Manager 节点
3. 部署 Gateway 节点
4. 部署 API Server 节点
5. 使用 `bin/start-all.sh` 协调启动

---

## 故障排除

### jun_api_service

- **API 未加载：** 检查 `api_config` 表，验证 `groovy_content` 能否编译
- **热重载不工作：** 确保在数据库更改后调用了刷新端点
- **编译错误：** 检查 Groovy 语法，验证导入，确保实现了 `IExecutor` 接口

### jun_dbapi

- **Orange 依赖错误：** 手动安装 Orange 库：`git clone https://gitee.com/freakchicken/orange && cd orange && mvn install`
- **Oracle 连接问题：** 将 Oracle JDBC 驱动复制到 `lib/` 目录
- **前端构建错误：** 清除 node_modules：`rm -rf node_modules && npm install`
- **插件未加载：** 检查 `plugins/` 目录中的插件 JAR，验证类实现了正确的接口

---

## 重要文件位置

### jun_api_service
- 主应用程序：`jun_api_service/jun_api_online_groovy/src/main/java/io/github/wujun728/ApiServiceApplication.java`
- 配置：`jun_api_service/jun_api_online_groovy/src/main/resources/application.yml`
- Groovy 加载器：搜索 `GroovyDynamicLoader` 类

### jun_dbapi（本仓库主路径）
- 主应用程序：`jun_dbapi/dbapi-core/src/main/java/com/gitee/freakchicken/dbapi/DBApiApplication.java`
- 插件接口：`jun_dbapi/dbapi-plugin/src/main/java/com/gitee/freakchicken/dbapi/plugin/`
- 插件实现：`jun_dbapi/jun-dbapi-plugin/`
- 插件参考源码：`dbapi/*-plugin-1.0.0.src/`
- 控制器：`jun_dbapi/dbapi-core/src/main/java/com/gitee/freakchicken/dbapi/controller/`
- 原 Vue 前端：`jun_dbapi/dbapi-ui/src/`
- 新静态前端：`jun_dbapi/jun_ui_sa_admin/`（`sa-view/dbapi/` 为业务页）
- SQL 脚本：`jun_dbapi/dbapi-assembly/sql/`
- Starter：`jun_dbapi_spring_boot_starter/`
- 任务与迁移记录：`README_AI.md`

---

## 外部链接

- **jun-groovy-api-starter Maven 中央仓库：** https://central.sonatype.com/artifact/io.github.wujun728/jun-groovy-api-spring-boot-starter
- **Orange SQL 引擎：** https://gitee.com/freakchicken/orange
- **DBApi 插件示例：** https://gitee.com/freakchicken/dbapi-plugin-demo


## 修改记录

| 日期 | 说明 |
|------|------|
| 2026-05-19 | 补充「本仓库实际内容与定位」、目录树、执行流程；标明根 pom 仅构建 `jun_dbapi`；区分本仓库与 Groovy/4.0 外部生态；增加 `jun_ui_sa_admin`、插件与辅助工程说明 |

### 每次修改都记录修改的内容到 README.md 文件中。