# AGENTS.md — 开发指引

# 全局工作规则
## 一、基础交互 & 文件管理规则（最高优先级）
1. 全程使用**简体中文**沟通，回答保持专业、简洁。
2. 每次无条件将当前会话内容（提问及回答均保存）导出为 MD 文件并保存至 `./docs/chat` 目录；文件名称根据对话核心内容自动提炼，文档名称必须为中文。
3. 工作前优先读取 `./docs` 目录历史内容，涉及过往业务、代码、方案时参考已有文档。
4. 如需生成脚本文件，统一存放至 `./script` 目录。python脚本放到 `./script/python` 目录。sql脚本放到 `./script/sql` 目录。
5. 没有说明代码要编译及运行的时候，无需编译、运行。我会自己尝试自动编译、运行，但请勿依赖此功能。
6. 在对话中发现对全局配置、依赖关系、运行方式时、系统配置、全局规则等配置，对其他的会话有益或者有效的，可以更新到当前的文档claude.md中。
7. 使用mcp的时候需要在控制台打印出log日志。mcp连接mysql的时候，需要打印出 本次查询什么数据，查询的结构，以及查询的SQL等等，查询的条件等等。
8. 当需要生成脚本的时候优先选择Python脚本，优先使用Python脚本。次选powershell脚本，次选shell脚本。
9. 当ID为较长的数字的时候，例如雪花ID，bigInteger等的时候，不可丢失ID的精度。 通过 MCP 查 MySQL 雪花 ID 时，统一用 CAST(id AS CHAR) 避免精度丢失。
10. 不能浪费时间，不要重复写代码，不要幻想，需要有理有据，根据已有进行推断，不确认的逻辑，需要先行确认，如果在执行中遇到问题，请及时反馈给使用者，或者注释、或者自行处理。
11. 没有我的允许，不可编译项目，不可运行项目；不可在完成任务后自动编译项目、运行项目；在完成任务后，提示我是否生成对话文档或者接口文档等；
12. 生成的代码需要添加注释，注释中必须包含：功能描述、参数说明、返回值说明、调用示例、CURL 示例。
13. 生成的代码需要有详细的日志，日志必须能准确的定位关键节点及步骤与流程走向，能便于定位问题，便于后续排查，解决bug。
14. 所有调用外部系统的请求或者被外部调用的接口、包括定时任务的接口，必须添加调用日志，打印完整curl请求，包括入参、出参及返回值等。
15. 生成代码里面不用写重复的工具类及方法，统一使用项目已有的工具类，或者hutools工具类，实在没有就自行实现工具类（正常都是有的）。
16. 写完代码后立马检查一下、依赖，包映入、方法调用、类引用，无异常，不能丢三落四，反复折腾浪费时间。
17. 同类型mcp连接，比如数据库连接，你不确定是那个mcp就问我或者你自己尝试一下，不要过早的下结论，mcp数据走不通、表没有等等；
18. 默认使用的id是雪花id，需要考虑丢失进度的场景；特别是mcp取id的时候，请务必使用CAST(id AS CHAR)来处理超长数字；
19. PaaS的表分两种一种是不带_PV后缀的，一种是带_PV后缀的，区别在于请求是否带预览的应用code，预览的应用code是走的_PV表，非预览应用code是走正常的表。
20. PaaS日志文件路径：D:\data\logs\app.log
21. 本地redis路径：D:\Java\Redis-x64-5.0.10\
22. 本地JDK路径：D:\Java\jdk1.8.0_181
22. 注意乱码问题，请使用utf-8编码。还要控制台乱码、shell乱码、powershell乱码、CMD乱码等等
23. 将执行任务的时候觉得重要的信息、后面可能会用到的信息、规则、流程等等，汇总后精炼后(每次不超过200字)追加到【D:\workspace\scrm-paas\AGENTS.md】文档中。
24. 新增接口协议默认使用GET、POST以及JSON出入参格式（默认通用使用）。特殊情况PUT、DELETE等及XML报文等其他格式报文体需要主动提示用户确认；


## 二、启动前置流程（核心原则：先对齐，再动手）（高优先级）
在正式编写代码、修改文件、落地方案前，严格执行以下步骤：
1. (重要)先用自己的语言复述：本次需要解决的问题、最终交付物；**明确标出所有存疑内容与自行做出的假设**。若有更优技术方案，直接提出，由使用者决策。
2. 需求信息不明确时，可向使用者提问，**单次最多提 3 个核心问题**，每个问题附带可选方案，并标注默认最优组合。
   需直至完全确认以下三点，方可继续：
    - 真实业务目标（区分字面描述与实际诉求）
    - 隐藏约束与偏好（技术栈、性能要求、兼容规则、不可修改内容等）
    - 整体实现思路、核心方案及选型原因
3. 未收到使用者明确的「可以开始」指令前，**禁止编写任何代码、修改任何文件**。

## 三、代码开发通用规范
1. 修改现有方法时，必须检查关联依赖；若存在依赖关系，同步完成依赖更新。
2. 代码类注释中，若 `author` 字段为空，统一默认填写：`@author Wujun`。
3. 新增接口必须附带完整接口文档，内容包含：接口URL、功能描述、通信协议、请求信息、入参说明、响应信息、返回值说明、调用示例、CURL 示例。
4. 代码与功能开发完成后，先自行校验功能可用性；校验通过后同步生成对应测试用例，并完成基础测试。

## 四、模型行为 & 调度策略（高优先级）
### 4.1 Sonnet 模型专属行为
若当前使用 Sonnet 模型，解读逻辑以**代码字面内容为主**，优先解答字面问题，不主动质疑原有代码设计与假设。

### 4.2 模型调用路径规则
#### （1）常规调用路径
Sonnet 负责主体开发与代码阅读 → codeGraph 辅助代码定位与依赖分析 → Opus 执行复审校验。

#### （2）模型升级切换规则
默认优先使用 Sonnet 快速探测问题复杂度，再按以下规则判定是否升级为 Opus：
- 仅 CRUD 开发、字段补全、标准化套路实现：全程使用 Sonnet
- 满足以下任一条件，**必须升级为 Opus**：
    1. 需求涉及跨 3 个及以上模块；
    2. 工作内容为否定类判断（排查缺失项、问题漏洞，而非罗列现有内容），可搭配复审机制；
    3. Sonnet 输出的方案出现自相矛盾、需要反复修订。
- 问题分析阶段优先使用 Opus，问题落地开发阶段使用 Sonnet。

## 五、codeGraph 使用规范（高优先级）
### 5.1 前置校验
启动工作后**先执行 `codegraph_status` 校验工具可用性**，再开展后续操作。

### 5.2 强制使用场景（不可用 Read/Grep 替代）
codeGraph 可用时，以下场景**必须优先调用对应能力**：
1. 分析、排查、梳理现有代码逻辑 → 使用 `codegraph_context`
2. 定位指定类名、方法名、接口路径 → 使用 `codegraph_search`
3. 修改已有方法前 → 必须调用 `codegraph_callers` + `codegraph_impact` 检查调用方与影响范围
4. 跨模块、跨服务逻辑追踪 → 使用 `codegraph_trace`
5. 输出代码影响范围结论前 → 必须通过 `codegraph_impact` 验证

### 5.3 可豁免场景（可跳过 codeGraph）
满足以下任一情况，无需调用 codeGraph：
1. 使用者已提供**精确文件路径 + 具体行号**；
2. 单文件内少于 10 行的局部小修改；
3. 纯配置文件、文档、独立脚本编辑；
4. 全新空白文件创建。


## 六、AI Code 项目编码规范（高优先级）
### 0. 通用行为铁律（最高优先级）
1. 只做需求内功能，禁止私自新增特性、额外抽象、全局重构、无关优化code...。
2. 先确认再编码：模糊需求主动询问假设、边界、多方案对比，不自行脑补实现。
3. 最小实现原则：无一次性工具抽象、无无用容错、无过度配置；函数控制 50 行内。
4. 存量代码优先对齐：完全匹配项目现有格式、库、目录，不引入新项目技术栈GitHub。
5. 高危操作必须询问：删文件、删表、强制推送、重置 Git、安装依赖前先确认。
6. 增量输出：单次最多输出 200 行 / 单个文件，完成一段做校验总结再继续稀土掘金。

### 1. 行为约束
1. 仅实现明确需求，不额外新增功能与抽象
2. 需求模糊先确认假设与边界，不自行脑补
3. 单次输出≤200行/单个文件，分步校验推进
4. 删除文件、删库、强制推送、安装依赖前必须询问

### 2. 代码风格
1. 命名：变量camelCase，类PascalCase，常量UPPER_SNAKE_CASE
2. 导入顺序：第三方包 → 内部模块 → 相对路径，空行分隔
3. TypeScript 严格模式，禁用 any，复杂类型抽至types目录
4. 函数不超过50行，优先拆分

### 3. 注释规则
1. 仅注释业务特殊逻辑「为什么」，清晰代码不注释「做什么」
2. 公共API使用JSDoc，私有代码不冗余注释
3. 清除所有console.log、debugger、无绑定TODO

### 4. 安全与异常
1. 异常抛出标准Error对象，统一接口返回 {code,message,data}
2. 敏感配置使用环境变量，禁止硬编码密钥
3. 所有入参、外部接口做参数校验

### 5. 测试与工程
1. 新功能先编写单元测试，覆盖边界场景
2. 提交遵循 Conventional Commits，提交前执行lint修复
3. 禁止直接提交main分支，新建功能分支开发




## 构建命令

```bash
** maven构建 **
maven构建工具地址：D:\Java\apache-maven-3.9.8\bin\
maven仓库地址：D:\Java\.m2\repository\


# 全量构建（跳过测试）
mvn clean install -DskipTests

# 编译指定模块及其依赖（推荐用于增量开发）
mvn clean install -pl scrm-paas-runtime/scrm-paas-runtime-server -am -DskipTests

# 基础库本地自测通过后再提交
mvn clean install -pl paas-infrastructure -am

# 运行全部测试
mvn test

# 运行单个测试类/方法
mvn test -Dtest=ComponentInstanceMapperTest
mvn test -Dtest=ComponentInstanceMapperTest#testInsert
```

Maven profiles: `dev`, `test`, `pre`, `uat`, `prod` — 按环境选择。

## 项目架构速览

Java 11 + Spring Boot 2.6 + Spring Cloud 2021.0.9 的 Maven 多模块微服务仓库。

核心模式：**元数据驱动的低代码平台**。租户在元数据层定义业务对象/字段/布局/权限，运行时引擎按元数据配置执行 CRUD 和流程。

| 服务 | 子模块路径 | 端口 | 职责 |
|------|-----------|------|------|
| 元数据服务 | `scrm-paas-metadata/paas-metadata-server` | 8001 | 设计时：对象/字段/布局/视图/权限配置 |
| 运行时引擎 | `scrm-paas-runtime/scrm-paas-runtime-server` | 8002 | 执行 CRUD、业务流、动态数据 |
| 发布管道 | `scrm-paas-metadata/paas-publish-server` | 8003 | 元数据从 dev→prod 发布 |
| 业务流程 | `scrm-paas-runtime/scrm-paas-runtime-bizflow` | 8004 | 业务流引擎 |
| CRM 扩展 | `scrm-paas-runtime-extender-standard-crm` | 8006 | 标准 CRM 扩展 |
| 集成服务 | `scrm-paas-integration/integration-server` | 8010 | 外部系统集成 |
| 产品管理 | `scrm-paas-product-manager/product-server` | 8011 | 产品/订阅管理 |
| 消息中心 | `scrm-paas-message/message-server` | 8080 | 消息推送 |
| UCC 授权 | `scrm-paas-ucc/ucc-server` | 8080 | 用户认证授权 |
| 任务执行器 | `scrm-paas-job-executor` | 8081 | 定时任务执行 |
| BPM | `scrm-paas-bpm/bpm-server` | 8085 | 审批流程 |
| 运行时网关 | `scrm-paas-runtime-gateway` | 9010 | API 网关 |
| OpenAPI 网关 | `scrm-paas-openapi-gateway` | 9010 | 对外开放 API 网关 |

基础库：`paas-infrastructure`（所有服务继承其 starter-parent）。

## 必须知道的编码约定

### 依赖注入

使用 `@RequiredArgsConstructor`（Lombok）构造器注入，**禁止 `@Autowired` 字段注入**。可选依赖用 `@Resource`。

```java
@Service
@RequiredArgsConstructor
public class FooService {
    private final FooMapper fooMapper;
}
```

### 响应包装

所有 Controller 必须返回 `Rsp<T>`（`net.trueland.scrm.common.model.rpc.Rsp`）：

```java
@PostMapping("/list")
public Rsp<PageData<FooResult>> list(@RequestBody FooParam param) {
    return Rsp.success(service.pageList(param));
}
```

使用 **OpenAPI 3.0** 注解（`@Tag`、`@Operation`、`@Schema`），不是 Swagger 2。

### 多租户上下文（极易遗漏！）

任何数据库操作前**必须先设置租户上下文**：

```java
CrmUserInfoContext.setBase(new Base(accountId, userId, username, realname, roleId, status, timezone));
Long accountId = CrmUserInfoContext.getAccountId();
```

测试代码中同样必须设置，且测试大多标注了 `@Disabled`，运行时需要先去掉注释。

### 动态表名模式

业务数据表根据对象编码和租户 ID 动态拼接：`paasobj_{objectCode}_{accountId}`

Mapper 中通过 `${tableName}` 拼接 SQL，切勿硬编码表名。

### 实体基类继承

完整层级：`BaseModel` → `TenantBaseModel` → `ConfigBaseModel` → `PaasMetadataModel`

| 基类 | 关键字段 | 用途 |
|------|----------|------|
| `BaseModel` | `createBy/createAt/updateBy/updateAt/deleted` | 通用基类，`deleted` 映射 `is_delete` 列，`@TableLogic` |
| `TenantBaseModel` | + `id`（雪花ID）, `accountId` | 租户业务表 |
| `ConfigBaseModel` | + `sourceFlag` | 配置态数据，标记来源 |
| `PaasMetadataModel` | + `metadataVersion`, `applicationCode` | 元数据表（`paas_component_*`） |

### 跨模块调用

**禁止直接注入其他模块的 Mapper**。必须通过 Feign 调用：

```java
@FeignClient(value = "scrm-paas-runtime-server", contextId = "customObjectFeign")
public interface CustomObjectFeign { }
```

每个 Application 类需标注 `@EnableFeignClients` 和 `@MapperScan`：

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients({"net.trueland.scrm.paas", "net.trueland.scrm.common.web.config.i18n.feign"})
@MapperScan(basePackages = {"net.trueland.scrm.paas.**.mapper"})
public class PaasRuntimeApplication { }
```

### Service 模式

新代码直接使用 `@Service`（不创建接口），旧代码中仍有 interface+impl 模式并存。

### 日期类型

使用 `java.time.LocalDateTime/LocalDate/LocalTime`，禁止 `java.util.Date` 和 `java.sql.Timestamp`。

### 包名

唯一合法包前缀：`net.trueland.scrm`。`net.trueland.tcloud` 已完全移除。

### @author 注释

如果 `@author` 为空，统一填写 `@author Wujun`。

### code 字段

`code` 是实体的不可变业务标识（租户内唯一），通过 `CodeGenerator` 生成。`xxCode` 字段是对其他实体 code 的引用。

### 序列化 & JSON

- VO/PO/DTO **不实现** `Serializable`
- Jackson 为主（`JsonNode`、`ObjectMapper`），FastJSON 仅允许在局部作用域使用，**禁止出现在 VO/DTO 字段中**
- 禁止 API 直接返回数据库实体，必须转换为 VO

### GET 请求对象参数

GET 端点使用对象参数时加 `@ParameterObject` 注解（Swagger 3 兼容）：

```java
@GetMapping("/list")
public Rsp<List<FooResult>> list(@ParameterObject FooParam param) { }
```

### 循环依赖

所有服务的 `bootstrap.yml` 配置 `spring.main.allow-circular-references: true`，允许循环依赖（谨慎使用）。

## 元数据查询关键规则

- `paas_component_object` 和 `paas_component_instance` 查询时**必须过滤 `metadata_version = 'master'`** 获取当前生效版本
- `paas_component_instance.field_type`：`0`=系统字段，`1`=业务字段，`2`=主字段(PrimaryInstance)，`null`=扩展字段
- 元数据租户 ID：`CommonConsts.MANAGER_ACCOUNT_ID`（值为 `0L`）
- 同一个租户的不同 `application_code` 共享对象定义

## 缓存与数据库

- 缓存层级：Caffeine（本地 30min）→ Redis → MyBatis L2（`PaasMyBatisCache`）→ MySQL
- 数据视图缓存 TTL：100 小时
- 双库：MySQL（元数据/事务） + MongoDB（日志/高量数据）
- Nacos 配置组：`scrm-paas`

## 分支与 CI

- 在 `dev` 分支开发，稳定后合并到 `release`
- 分支命名：`dev-*`、`test*`、`pre*`、`uat*`、`hotfix*`、`prod*`、`release*`、`master`
- 基础库（`paas-infrastructure`）修改后必须先本地 `mvn install` 验证再加测试用例提交

## MCP 连接
说明：下面的两个mcp都可以连通并且可以使用，不要生成Python脚本取连接数据库了，浪费时间。
MCP 连接名称：`mysql` 是MySQL 5.7 本地开发调试的数据库
MCP 连接名称：`paas-mcp-mysql` 是MySQL 5.7 scrm-paas开发调试的数据库

配置在 `.mcp.json`，提供 MySQL/MongoDB/Redis 只读查询。

**MySQL 查询强制使用 MCP 工具**，不要建议用户手动执行查询类 SQL。禁止直接修改数据库数据，写操作生成 SQL 交给用户执行。

## CodeGraph

分析代码时优先使用 CodeGraph 工具（`codegraph_context`、`codegraph_search`、`codegraph_callers`、`codegraph_impact`）。

可跳过场景：用户已提供精确文件路径+行号、10 行以内的单文件修改、纯配置文件编辑、新建空白文件。

## MinIO 配置陷阱

`file-config.endpoint` 必须带 `http://` 前缀（如 `http://172.16.198.62:9000`），否则代码会自动拼接 `https://` 导致 HTTPS 连接 MinIO HTTP 端口失败。

## 订单中心同步开关

`order.center.sync-enabled` 默认 `false`，标品订单保存时不会自动调用订单中心下单。需在配置中设为 `true` 才会触发同步。

## 逗号分隔字段查询

查询逗号分隔字段（如 `connected_to_receivable_line`）时，禁止使用 `LIKE '%id%'`（会子串误匹配），必须使用 `FIND_IN_SET` 精确匹配：
```java
.apply("FIND_IN_SET({0}, connected_to_receivable_line) > 0", searchId)
```

## runtime-server 依赖

`/project/contractQuotationProducts` 和 `/project/products` 接口通过 Feign 调用 `scrm-paas-runtime-server`（端口8002），该服务未启动时会报 `UnknownHostException`。本地开发时需启动 runtime-server 或跳过这两个接口。

## 合同报价单双字段约定

`c_contract` 表有**两个**报价单关联字段，查询合同关联报价单时必须同时考虑：
- `c_quotation_id`：标准报价单版本 ID，指向 `paasobj_c_quotation.id`，需再经 `paasobj_ka_quotation.quotation_id` 查档位
- `ka_quotation`：KA 报价单档位 ID，直接指向 `paasobj_ka_quotation.id`，可作为 `tierId` 直接使用

两者都可能单独有值，逻辑上应做双兜底：`c_quotation_id` 优先走完整链路，为空时用 `ka_quotation` 直查档位。仅当两者都空才判定"未关联报价单"。

## KA 项目两种来源约定

`ka_project` 表项目分两种来源，查询关联产品时必须兼容：
- **主动创建项目**：`pre_contract_flag=false`，`contract_id` 有值 → 走合同链路（`contractQuotationProducts`）
- **无合同启动项目**：`pre_contract_flag=true`，`contract_id` 为空、`pre_contract_id` 有值 → 走预合同链路：`ka_pre_contract.associated_ka_quotations`（档位ID）直接调 `getTierDetail`

仅当 `contract_id` 与 `pre_contract_id` 都空才判定"项目未关联合同及无合同启动"。

## 参考

- 完整项目约定见 `CLAUDE.md`
- 代码生成模板见 `.claude/skills/crud/SKILL.md`
- 编码风格指南见 `.claude/skills/project-conventions/SKILL.md`