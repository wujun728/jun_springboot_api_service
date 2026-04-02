# 长时运行智能体框架 v2

基于 Anthropic 三智能体架构（Planner + Worker + Evaluator），让 AI 智能体能够自动拆分、执行和验证大型任务。

## 架构

```
用户输入任务 → Planner(拆分) → Worker(逐个执行) → Evaluator(独立验证) → 完成
                                    ↑                       │
                                    └───── 失败时重试 ──────┘
```

**三个智能体角色：**

| 角色 | 职责 | 提示词 |
|------|------|--------|
| **Planner** | 分析任务，生成细粒度子任务清单 | `prompts/planner.md` |
| **Worker** | 每次完成一个子任务，提交代码 | `prompts/worker.md` |
| **Evaluator** | 独立验证 Worker 的成果质量 | `prompts/evaluator.md` |

## 前置条件

- Python 3.8+
- Claude Code CLI（`claude` 命令可用）
- Git

## 快速开始

### 1. 新任务

```bash
cd /path/to/your/project
python /path/to/long-running-agent/main.py --task "构建一个用户管理系统，包含JWT认证和CRUD接口"
```

### 2. 查看进度

```bash
python main.py --status
```

输出示例：
```
进度: 12/35 (34.3%)
  已完成: 12  失败/跳过: 1  待处理: 22
  setup              3/3    [####################]
  database           4/5    [################....]
  functional         3/10   [######..............]
  api                2/8    [#####...............]
```

### 3. 中断后恢复

```bash
# Ctrl+C 中断后，随时恢复
python main.py --resume
```

### 4. 生成报告

```bash
python main.py --report
```

## 配置

编辑 `config.json`：

```json
{
  "project_name": "my-project",
  "project_type": "java-springboot",
  "user_task": "你的任务描述...",
  "agent": {
    "model": "opus",
    "max_sessions": 50
  },
  "retry": {
    "max_attempts": 3
  },
  "build": {
    "compile_command": "mvn clean compile -DskipTests",
    "test_command": "mvn test"
  }
}
```

### 配置说明

| 字段 | 说明 | 默认值 |
|------|------|--------|
| `project_name` | 项目名称 | `my-project` |
| `project_type` | 项目类型 (java-springboot, vue-frontend, fullstack) | `java-springboot` |
| `user_task` | 任务描述（也可通过 --task 参数指定） | - |
| `agent.model` | Claude 模型 | `opus` |
| `agent.planner_max_turns` | 规划器最大轮数 | `200` |
| `agent.worker_max_turns` | 执行器最大轮数 | `150` |
| `agent.evaluator_max_turns` | 评估器最大轮数 | `50` |
| `agent.max_sessions` | 最大会话数 | `50` |
| `agent.cooldown_seconds` | 会话间冷却时间 | `5` |
| `agent.task_timeout_seconds` | 单次 CLI 调用超时 | `600` |
| `retry.max_attempts` | 失败任务最大重试次数 | `3` |
| `build.compile_command` | 编译命令 | - |
| `build.test_command` | 测试命令 | - |

## 执行流程

```
1. Planner 将任务拆分为 30-80 个子任务 → task_list.json
2. 主循环：
   a. 选下一个 pending 任务（按优先级，依赖已满足）
   b. Worker 执行任务，Git 提交
   c. Evaluator 独立验证质量
   d. 通过 → 标记 passed
   e. 失败 → 记录原因，Worker 重试（最多 N 次）
   f. 超过重试上限 → 标记 skipped，继续下一个
3. 所有任务完成或达到会话上限 → 输出最终报告
```

## 任务状态

```
pending → in_progress → evaluating → passed
                                   → failed → in_progress (重试)
                                            → skipped (超过重试上限)
```

## 生成的文件

| 文件 | 说明 |
|------|------|
| `task_list.json` | 任务清单和状态（核心持久化文件） |
| `claude-progress.txt` | 人可读的进度记录 |
| `execution_log.jsonl` | 结构化执行日志（每行一条 JSON） |
| `logs/` | 每次智能体调用的完整输出 |
| `init.sh` | 环境初始化脚本 |

## 命令行参数

| 参数 | 说明 |
|------|------|
| `--task "描述"` | 指定任务描述 |
| `--resume` | 从断点恢复执行 |
| `--config file.json` | 指定配置文件 |
| `--status` | 查看当前进度 |
| `--report` | 生成最终报告 |
| `--max-sessions N` | 覆盖最大会话数 |

## 框架文件结构

```
long-running-agent/
├── main.py                    # 入口 + 主编排循环
├── config.py                  # 配置加载
├── task_manager.py            # 任务生命周期管理
├── agent_runner.py            # Claude CLI 调用封装
├── logger.py                  # 结构化日志
├── prompt_renderer.py         # 提示词模板渲染
├── config.json                # 默认配置
├── prompts/
│   ├── planner.md             # 规划器提示词
│   ├── worker.md              # 执行器提示词
│   └── evaluator.md           # 评估器提示词
└── examples/
    ├── java-springboot.json
    ├── vue-frontend.json
    └── fullstack.json
```

## 设计原理

### 为什么三个智能体？

- **Planner** 解决"贪多嚼不烂"：将大任务拆分为小步骤
- **Worker** 解决"上下文断裂"：每次会话从持久化文件重建上下文
- **Evaluator** 解决"自我放水"：独立验证避免 Worker 对自己的工作过于宽容

### 为什么每次创建新会话？

长会话的上下文压缩会丢失关键信息。新会话从外部文件（task_list.json + claude-progress.txt + git log）重建上下文更可靠。

### 为什么用 JSON 格式的任务清单？

Anthropic 实验发现 JSON 比 Markdown 更不容易被模型误修改或覆盖。结构化数据也更方便程序化检查。

### 为什么一次只做一个任务？

"小步快跑"策略。每个会话结束时代码处于干净、可编译、已提交的状态。即使下个会话完全丢失上下文，也能通过 Git 历史快速恢复。

## 参考

- [Anthropic: Effective frameworks for long-running agents](https://www.anthropic.com/engineering/effective-frameworks-for-long-running-agents)
- [Anthropic: Harness design for long-running apps](https://www.anthropic.com/engineering/harness-design-long-running-apps)
