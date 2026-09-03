# 知录API平台 - AI开发规则

## 项目概述
知录API（zapi-allin）是一个集成了自研工作流引擎、电子签章、表单设计器、文档编辑等功能的现代化低代码平台。

## 技术栈

### 后端 (zapi-allin-sys)
- **框架**: Spring Boot 2.7.18 + MyBatis Plus
- **数据库**: MySQL 8
- **架构**: 多模块Maven项目
  - `allin-common`: 公共工具类和基础组件
  - `allin-model`: 业务模型层（工作流、表单、合同等）
  - `allin-support`: 支撑服务（文件、消息等）
  - `allin-starter`: 启动模块
- **核心功能**: 
  - 自研工作流引擎（非BPMN，JSON配置）
  - 电子签章（支持RSA/SM2数字证书）
  - 印章生成与管理
  - PDF电子盖章

### 前端 (zapi-allin-web)
- **框架**: Vue 3 + Vite 4
- **UI库**: Element Plus 2.3.8
- **状态管理**: Pinia 2.1.6
- **路由**: Vue Router 4.2.4
- **核心组件**:
  - 表单设计器（拖拽式）
  - 流程设计器（JSON配置）
  - 列表设计器
  - PDF预览与盖章
  - OnlyOffice文档编辑器集成

## AI开发规则

### 1. 代码风格与规范

#### 后端 (Java)
- **命名规范**:
  - 实体类: `XxxEntity` 或直接业务名称（如 `WfProcessDefinition`）
  - DTO类: `XxxDTO`（如 `ProcessDefinitionDTO`）
  - Service接口: `XxxService`
  - Service实现: `XxxServiceImpl`
  - Controller: `XxxController`
  - Mapper: `XxxMapper`

- **包结构**:
  ```
  com.zapi.{module}.{feature}
  ├── controller/     # 控制器层
  ├── service/        # 服务接口
  │   └── impl/       # 服务实现
  ├── mapper/         # MyBatis Mapper
  ├── model/          # 数据模型
  │   ├── entity/     # 实体类
  │   ├── dto/        # 数据传输对象
  │   └── vo/         # 视图对象
  └── listener/       # 事件监听器
  ```

- **注解使用**:
  - 控制器必须使用 `@RestController` 和 `@RequestMapping`
  - 服务层使用 `@Service` 和 `@Transactional`（需要事务时）
  - API文档使用 `@ApiOperation` 和 `@ApiParam`
  - 日志使用 `@Slf4j`（Lombok）

#### 前端 (Vue 3)
- **组件规范**:
  - 使用 Composition API (`<script setup lang="ts">`)
  - 组件文件名: PascalCase（如 `FormDesigner.vue`）
  - 组件内部变量: camelCase
  - 常量: UPPER_SNAKE_CASE

- **目录结构**:
  ```
  src/
  ├── views/              # 页面组件
  │   ├── workflow/       # 工作流相关
  │   ├── form/           # 表单相关
  │   ├── contract/       # 合同相关
  │   └── taskcenter/     # 任务中心
  ├── components/         # 公共组件
  │   ├── form-designer/  # 表单设计器
  │   └── workflow-designer/ # 流程设计器
  ├── api/                # API接口
  ├── stores/             # Pinia状态管理
  ├── config/             # 配置文件
  └── utils/              # 工具函数
  ```

- **状态管理**:
  - 使用 Pinia 管理全局状态
  - Store文件命名: `useXxxStore`
  - 复杂组件状态使用 `reactive` 或 `ref`

### 2. 业务逻辑规范

#### 工作流引擎
- **流程定义**:
  - 使用JSON配置，避免BPMN复杂性
  - 支持审批节点、条件分支节点
  - 流程变量通过 `variables` 字段管理
  - 业务表单类型 (`businessType`):
    - `1`: 系统表单（手动配置视图路径）
    - `2`: 业务表单（自动加载formConfig）

- **业务视图 (businessView)**:
  - 系统表单: 存储视图路径字符串（如 `/views/system/leave-form`）
  - 业务表单: 存储formConfig JSON字符串
  - 前后端必须保持一致传递和存储

#### 表单设计器
- **组件系统**:
  - 使用12列内部系统，渲染时转换为Element Plus 24列
  - 组件默认 `span=12`（满宽）
  - 配置文件: `/src/config/form/components.ts`
  - 组件类型映射到流程变量类型

- **布局规范**:
  - 设计画布和预览必须使用相同的 `el-row/el-col` 布局
  - 确保"所见即所得"

#### 电子签章
- **证书支持**:
  - RSA证书
  - SM2国密证书
  - 证书密码验证

- **盖章流程**:
  1. 选择印章和证书
  2. 点击PDF位置
  3. 输入证书密码
  4. 调用后端盖章接口
  5. 保存签署记录

### 3. API设计规范

#### 后端接口
- **RESTful风格**:
  - GET: 查询
  - POST: 创建
  - PUT: 更新
  - DELETE: 删除

- **统一响应格式**:
  ```java
  Result.success(message, data)
  Result.error(message)
  ```

- **分页参数**:
  - `pageNum`: 页码
  - `pageSize`: 每页大小
  - 返回: `{ records: [], total: 0 }`

#### 前端API
- **文件位置**: `/src/api/`
- **命名**: `xxxApi.js`
- **导出方式**:
  ```javascript
  export default {
    getList: (params) => api.get('/xxx/list', { params }),
    getDetail: (id) => api.get(`/xxx/${id}`),
    create: (data) => api.post('/xxx', data),
    update: (id, data) => api.put(`/xxx/${id}`, data),
    delete: (id) => api.delete(`/xxx/${id}`)
  }
  ```

### 4. 数据流规范

#### 工作流数据流
```
前端表单 → URL参数 → 设计器页面 → 保存接口 → 后端Service → 数据库
                                                    ↓
                                              任务中心回显
```

#### 表单配置数据流
```
表单设计器 → formConfig (JSON) → businessView字段 → 工作流定义
                                                    ↓
                                              任务中心表单渲染
```

### 5. 错误处理规范

#### 后端
- 使用统一异常处理器
- 业务异常抛出自定义异常
- 记录详细日志（使用 `log.error`）
- 返回友好的错误信息

#### 前端
- 使用 `try-catch` 包裹异步操作
- 使用 `ElMessage` 显示错误提示
- 记录控制台日志便于调试
- 加载状态使用 `loading` 变量控制

### 6. 性能优化规范

#### 前端
- 大列表使用虚拟滚动
- 图片懒加载
- 路由懒加载
- 防抖节流（表单输入、搜索）
- 合理使用 `computed` 和 `watch`

#### 后端
- 数据库查询使用索引
- 分页查询避免全表扫描
- 使用缓存（Redis）
- 异步处理耗时操作
- 文件流处理及时关闭

### 7. 安全规范

- **后端**:
  - 接口权限验证
  - SQL注入防护（使用MyBatis参数化）
  - XSS防护
  - 敏感信息加密存储（证书密码）

- **前端**:
  - 路由守卫验证登录状态
  - 敏感操作二次确认
  - 文件上传类型和大小限制

### 8. 测试规范

- **功能测试**:
  - 创建、编辑、删除的完整流程
  - 边界条件测试
  - 异常情况处理

- **集成测试**:
  - 工作流端到端测试（创建→发起→审批→完成）
  - 表单设计→预览→提交
  - 电子签章完整流程

### 9. 文档规范

- **代码注释**:
  - 类和方法必须有JavaDoc/JSDoc
  - 复杂逻辑添加行内注释
  - TODO标记待完成功能

- **变更记录**:
  - 重要功能修改创建 `.md` 文档
  - 记录问题、原因、解决方案
  - 包含代码示例和数据流图

### 10. Git提交规范

- **提交信息格式**:
  ```
  <type>(<scope>): <subject>
  
  <body>
  ```

- **Type类型**:
  - `feat`: 新功能
  - `fix`: 修复bug
  - `refactor`: 重构
  - `style`: 样式调整
  - `docs`: 文档更新
  - `perf`: 性能优化
  - `test`: 测试相关

- **示例**:
  ```
  feat(workflow): 添加业务视图刷新按钮
  
  - 在业务表单配置区域添加刷新按钮
  - 支持手动获取最新表单配置
  - 自动更新流程变量列表
  ```

## 特殊注意事项

### 1. 工作流引擎特性
- 本项目使用自研工作流引擎，**不是**Flowable或Activiti
- 流程配置使用JSON，不使用BPMN XML
- 流程节点类型有限，专注于审批和条件分支

### 2. 表单系统特性
- 表单设计器使用12列内部系统
- 渲染时需要 `span * 2` 转换为24列
- 组件配置在 `/src/config/form/components.ts`

### 3. 电子签章特性
- 支持国密SM2和RSA双算法
- 证书文件需要密码验证
- 签署记录需要完整保存（前后文件、坐标、算法等）

### 4. 文件处理
- 后端使用策略模式处理文件存储
- 前端文件预览使用 `/zapi-allin-sys/file/download/{fileId}`
- PDF处理使用 pdfjs-dist 2.2.228

### 5. 数据一致性
- `businessView` 字段在整个工作流生命周期必须保持一致
- 表单配置变更后需要手动刷新工作流定义
- 流程变量与表单字段保持映射关系

## AI开发建议

1. **理解业务上下文**: 优先理解工作流、表单、签章的业务逻辑
2. **保持数据一致性**: 特别注意 `businessView` 等关键字段的传递
3. **遵循现有模式**: 参考已有代码的实现模式
4. **完整的错误处理**: 前后端都要有完善的异常处理
5. **详细的日志记录**: 便于问题排查和调试
6. **测试驱动**: 修改后进行完整的功能测试
7. **文档先行**: 重要功能先写文档再实现
8. **渐进式开发**: 复杂功能分步实现，每步都可运行

---

**版本**: 1.0.0  
**更新时间**: 2025-11-21  
**维护者**: 知录API开发团队
