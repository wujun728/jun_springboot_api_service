# 长时运行智能体框架 v2 — 设计规格

> 基于 Anthropic 三智能体架构，Python 编排 + Claude Code CLI 执行

---

## 1. 背景与目标

### 问题

AI 智能体处理大型任务时面临四种失败模式：

| 失败模式 | 描述 |
|---------|------|
| 贪多嚼不烂 | 一次尝试做太多，上下文耗尽后留下半成品 |
| 过早宣布完成 | 看到部分进展就认为任务结束 |
| 环境损坏 | 遗留未提交的错误代码，下个会话不知从何开始 |
| 自我放水 | Worker 验证自己的工作，倾向于乐观评价 |

### 目标

构建一个三智能体框架（Planner + Worker + Evaluator），让用户只需输入一个大任务描述，框架自动拆分、逐个执行、独立验证，直到所有子任务完成。支持中断恢复和详细日志追踪。

### 范围

- **主要场景**: 软件开发任务（Java SpringBoot、Vue.js、全栈）
- **扩展场景**: 文档编写、数据分析等可拆分的大型任务
- **运行方式**: Python 编排器调用 `claude` CLI
- **依赖**: Python 3.8+、Claude Code CLI、Git

---

## 2. 架构

### 2.1 总体架构

```
用户: python main.py --task "构建一个用户管理系统"
        │
        ▼
┌──────────────────────────────────────────────────────────────┐
│                    main.py (Python 编排器)                     │
│                                                                │
│  ┌──────────┐     ┌──────────┐ ←─重试──┐  ┌──────────────┐  │
│  │ Planner  │────→│  Worker  │────────→│  │  Evaluator   │  │
│  │ (规划器)  │     │ (执行器)  │         │  │  (评估器)     │  │
│  └──────────┘     └──────────┘         │  └──────────────┘  │
│       │                │               │         │            │
│       ▼                ▼               │         ▼            │
│  task_list.json   Git commits         │   evaluation结果     │
│                                        │         │            │
│                                        └─失败────┘            │
│                                                                │
│  execution_log.jsonl  ←── 所有步骤的结构化日志                  │
│  claude-progress.txt  ←── 人可读的进度文件                      │
└──────────────────────────────────────────────────────────────┘
```

### 2.2 文件结构

```
long-running-agent/
├── main.py                    # 入口 + 主编排循环
├── config.py                  # 配置加载与验证
├── task_manager.py            # 任务生命周期管理
├── agent_runner.py            # Claude CLI 调用封装
├── logger.py                  # 结构化日志（JSONL + 人可读）
├── prompt_renderer.py         # 提示词模板渲染
├── config.json                # 默认配置模板
├── prompts/
│   ├── planner.md             # 规划器提示词
│   ├── worker.md              # 执行器提示词
│   └── evaluator.md           # 评估器提示词
└── examples/
    ├── java-springboot.json
    ├── vue-frontend.json
    └── fullstack.json
```

### 2.3 运行时生成的文件（在项目工作目录中）

```
<work_directory>/
├── task_list.json             # 任务清单（持久化状态）
├── claude-progress.txt        # 人可读进度文件
├── execution_log.jsonl        # 结构化执行日志
├── init.sh / init.bat         # 环境初始化脚本
└── logs/
    ├── planner.log            # Planner 智能体输出
    ├── worker-T001-s1.log     # Worker 每次执行的输出
    └── eval-T001-s1.log       # Evaluator 每次评估的输出
```

---

## 3. 数据模型

### 3.1 config.json

```json
{
  "project_name": "my-project",
  "project_type": "java-springboot",
  "project_description": "项目描述",
  "work_directory": ".",
  "user_task": "",

  "agent": {
    "model": "opus",
    "planner_max_turns": 200,
    "worker_max_turns": 150,
    "evaluator_max_turns": 50,
    "max_sessions": 50,
    "cooldown_seconds": 5,
    "task_timeout_seconds": 600
  },

  "retry": {
    "max_attempts": 3,
    "on_failure": "skip"
  },

  "git": {
    "auto_commit": true,
    "branch_prefix": "agent/"
  },

  "paths": {
    "task_list": "task_list.json",
    "progress_file": "claude-progress.txt",
    "execution_log": "execution_log.jsonl",
    "log_dir": "logs"
  },

  "build": {
    "compile_command": "mvn clean compile -DskipTests",
    "test_command": "mvn test",
    "run_command": "mvn spring-boot:run"
  }
}
```

### 3.2 task_list.json

```json
{
  "meta": {
    "project_name": "user-management-system",
    "created_at": "2026-04-02T10:00:00",
    "user_task": "构建一个用户管理系统...",
    "total_tasks": 35
  },
  "tasks": [
    {
      "id": "T001",
      "category": "setup",
      "title": "Maven 项目结构搭建",
      "description": "创建标准 SpringBoot 项目结构，包含 pom.xml 和基本目录",
      "verification_steps": [
        "pom.xml 存在且包含 spring-boot-starter-web 依赖",
        "运行 mvn compile 成功无错误",
        "src/main/java 和 src/main/resources 目录存在"
      ],
      "status": "pending",
      "priority": 1,
      "dependencies": [],
      "attempts": 0,
      "max_attempts": 3,
      "result": null,
      "evaluation": null,
      "error_history": [],
      "started_at": null,
      "completed_at": null
    }
  ]
}
```

### 3.3 任务状态机

```
pending ──→ in_progress ──→ evaluating ──→ passed ✓
                                        ──→ failed ──→ in_progress (重试)
                                                    ──→ skipped (超过最大重试)
```

### 3.4 execution_log.jsonl

每行一条 JSON 日志：

```json
{"ts":"2026-04-02T10:05:00","event":"session_start","data":{"task":"构建用户管理系统"}}
{"ts":"2026-04-02T10:05:01","event":"planner_start","data":{}}
{"ts":"2026-04-02T10:15:00","event":"planner_done","data":{"tasks_count":35}}
{"ts":"2026-04-02T10:15:05","event":"worker_start","data":{"task_id":"T001","session":1,"attempt":1}}
{"ts":"2026-04-02T10:20:00","event":"worker_done","data":{"task_id":"T001","session":1}}
{"ts":"2026-04-02T10:20:05","event":"eval_start","data":{"task_id":"T001"}}
{"ts":"2026-04-02T10:21:00","event":"eval_done","data":{"task_id":"T001","passed":true,"score":95}}
{"ts":"2026-04-02T10:21:01","event":"task_status","data":{"task_id":"T001","status":"passed"}}
```

---

## 4. 组件设计

### 4.1 main.py — 入口与编排

**命令行接口：**

```
python main.py --task "构建一个用户管理系统"         # 新任务
python main.py --resume                              # 从断点恢复
python main.py --resume --config custom.json         # 指定配置恢复
python main.py --status                              # 查看当前进度
python main.py --report                              # 生成最终报告
```

**主循环伪代码：**

```python
def main():
    args = parse_args()
    config = Config.load(args.config)
    task_mgr = TaskManager(config)
    runner = AgentRunner(config)
    log = ExecutionLogger(config)

    if args.status:
        print(task_mgr.load().summary())
        return

    if args.report:
        print(task_mgr.load().generate_report())
        return

    # 阶段一：规划
    if args.resume and task_mgr.has_existing_tasks():
        task_mgr.load()
        log.event('session_resume', {'pending': task_mgr.pending_count()})
    else:
        log.event('planner_start')
        runner.run_planner(args.task or config.user_task)
        task_mgr.load()
        log.event('planner_done', {'tasks_count': task_mgr.total_count()})

    # 阶段二：执行循环
    session = 0
    try:
        while True:
            task = task_mgr.next_task()
            if task is None:
                break
            if session >= config.agent.max_sessions:
                log.warn(f"达到最大会话数 {config.agent.max_sessions}")
                break

            session += 1
            attempt = task['attempts'] + 1

            # Worker
            log.event('worker_start', {
                'task_id': task['id'], 'session': session, 'attempt': attempt
            })
            task_mgr.update_status(task['id'], 'in_progress')
            worker_output = runner.run_worker(task, session)
            log.event('worker_done', {'task_id': task['id'], 'session': session})

            # Evaluator
            log.event('eval_start', {'task_id': task['id']})
            task_mgr.update_status(task['id'], 'evaluating')
            eval_result = runner.run_evaluator(task)
            log.event('eval_done', {
                'task_id': task['id'],
                'passed': eval_result['passed'],
                'score': eval_result.get('score', 0)
            })

            # 更新状态
            if eval_result['passed']:
                task_mgr.mark_passed(task['id'], eval_result)
            else:
                task_mgr.mark_failed(task['id'], eval_result)

            task_mgr.save()
            time.sleep(config.agent.cooldown_seconds)

    except KeyboardInterrupt:
        log.event('interrupted')
        task_mgr.save()
        print("\n中断。使用 --resume 恢复执行。")

    # 阶段三：报告
    report = task_mgr.generate_report()
    log.event('session_end', report)
    print(format_report(report))
```

### 4.2 task_manager.py — 任务生命周期

**核心方法：**

- `load()` — 从 `task_list.json` 加载任务
- `save()` — 持久化到 `task_list.json`
- `next_task()` — 返回下一个可执行任务（按优先级，依赖已满足，未超过重试上限）
- `update_status(task_id, status)` — 更新状态
- `mark_passed(task_id, eval_result)` — 标记通过，记录评估结果和完成时间
- `mark_failed(task_id, eval_result)` — 标记失败，增加 attempts，记录错误历史
- `summary()` — 返回进度摘要字符串
- `generate_report()` — 生成最终报告（passed/failed/pending 统计）

**next_task() 逻辑：**

```python
def next_task(self):
    candidates = []
    for task in self.tasks:
        if task['status'] == 'passed' or task['status'] == 'skipped':
            continue
        if task['status'] == 'failed' and task['attempts'] >= task['max_attempts']:
            self.update_status(task['id'], 'skipped')
            continue
        if not self._dependencies_met(task):
            continue
        candidates.append(task)

    if not candidates:
        return None
    candidates.sort(key=lambda t: (t['priority'], t['id']))
    return candidates[0]
```

### 4.3 agent_runner.py — Claude CLI 封装

**核心职责：**
- 渲染提示词模板
- 调用 `claude -p` 命令
- 捕获输出，保存日志文件
- 处理超时和错误

```python
class AgentRunner:
    def _run_claude(self, prompt, max_turns, log_file):
        cmd = [
            'claude', '-p', prompt,
            '--model', self.config.agent.model,
            '--max-turns', str(max_turns),
            '--output-format', 'json'
        ]
        try:
            proc = subprocess.run(
                cmd,
                capture_output=True,
                text=True,
                timeout=self.config.agent.task_timeout_seconds,
                cwd=self.config.work_directory
            )
            # 保存输出到日志文件
            with open(log_file, 'w', encoding='utf-8') as f:
                f.write(proc.stdout)
            return {'success': True, 'output': proc.stdout, 'exit_code': proc.returncode}
        except subprocess.TimeoutExpired:
            return {'success': False, 'output': '', 'error': 'timeout'}
        except Exception as e:
            return {'success': False, 'output': '', 'error': str(e)}
```

### 4.4 logger.py — 结构化日志

双输出：
1. `execution_log.jsonl` — 机器可读，用于追踪和分析
2. 控制台输出 — 人可读，带时间戳和颜色

```python
class ExecutionLogger:
    def event(self, event_type, data=None):
        entry = {
            'ts': datetime.now().isoformat(),
            'event': event_type,
            'data': data or {}
        }
        # 写入 JSONL
        with open(self.log_path, 'a', encoding='utf-8') as f:
            f.write(json.dumps(entry, ensure_ascii=False) + '\n')
        # 控制台输出
        print(f"[{entry['ts'][:19]}] {event_type} {json.dumps(data or {}, ensure_ascii=False)}")
```

---

## 5. 提示词设计

### 5.1 Planner（规划器）

**核心指令摘要：**

- 分析用户任务，生成 `task_list.json`
- 要求 30-80 个细粒度任务，每个独立可验证
- 每个任务必须有明确的 `verification_steps`
- 创建 `init.sh`（环境初始化脚本）
- 创建 `claude-progress.txt`（进度文件）
- Git 初始提交
- 严禁实现任何功能代码

**分类体系：** setup, database, functional, api, frontend, security, testing, integration, deployment

### 5.2 Worker（执行器）

**8 步流程：**

1. 了解状态 — 读 `claude-progress.txt` + `git log`
2. 验证环境 — 运行编译命令，先修后建
3. 读取当前任务详情 — 从 `task_list.json` 中读取指定任务
4. 实现任务 — 编写代码
5. 自测 — 按 `verification_steps` 初步验证
6. Git 提交 — `feat(T0XX): 描述`
7. 更新 `claude-progress.txt` — 追加会话记录
8. 输出结构化结果

**关键约束：**
- Worker 不修改 `task_list.json` 的状态字段
- 一次只做一个任务
- 会话结束时代码必须可编译
- 如果是重试，先读取 `error_history` 了解之前失败的原因

### 5.3 Evaluator（评估器）

**核心指令摘要：**

- 你是质量检查员，不是开发者，不要修改任何代码
- 逐条验证 `verification_steps`
- 输出结构化评估报告（JSON）
- 评分要严格：不确定的不能标记为通过
- 如果失败，必须给出具体修复建议

**输出格式：**

```json
{
  "task_id": "T001",
  "passed": true,
  "score": 95,
  "checks": [
    {"step": "pom.xml 存在且包含 spring-boot-starter-web", "passed": true, "detail": "确认"},
    {"step": "mvn compile 成功", "passed": true, "detail": "编译通过，0 错误"}
  ],
  "feedback": "",
  "suggestions": []
}
```

---

## 6. 错误处理

| 场景 | 处理方式 |
|------|---------|
| Worker 超时 | 标记为 failed，记录 timeout 原因，触发重试 |
| Worker 编译失败 | Worker 自行修复；如仍失败，Evaluator 标记 failed |
| Evaluator 判定失败 | 记录反馈到 `error_history`，Worker 重试时会读取 |
| 超过最大重试次数 | 标记为 skipped，继续下一个任务 |
| 依赖任务被 skipped | 依赖它的任务也自动标记为 skipped |
| 用户 Ctrl+C | 保存当前状态，可用 `--resume` 恢复 |
| Claude CLI 崩溃 | 捕获异常，标记为 failed，触发重试 |

---

## 7. 使用方式

### 新任务

```bash
cd /path/to/your/project
python /path/to/long-running-agent/main.py --task "构建一个用户管理系统，包含JWT认证、CRUD接口、Swagger文档"
```

### 查看进度

```bash
python main.py --status
# 输出:
# 进度: 12/35 (34.3%)
#   setup          3/3  ████████████████████ 100%
#   database       4/5  ████████████████     80%
#   functional     3/10 ██████               30%
#   api            2/8  █████                25%
#   ...
```

### 从断点恢复

```bash
python main.py --resume
```

### 生成报告

```bash
python main.py --report
```

---

## 8. 与现有框架的对比

| 维度 | 现有框架 (v1) | 新框架 (v2) |
|------|-------------|-------------|
| 编排语言 | Shell (run.sh/run.bat) | Python (main.py) |
| 智能体数量 | 2 (Init + Worker) | 3 (Planner + Worker + Evaluator) |
| 验证方式 | Worker 自我验证 | 独立 Evaluator 验证 |
| 失败处理 | 无重试机制 | 可配置重试 + 跳过 |
| 状态管理 | Worker 直接修改 JSON | Python 编排器统一管理 |
| 日志 | 纯文本 | 结构化 JSONL + 人可读 |
| 中断恢复 | 基本支持 (--resume) | 完整支持，状态实时持久化 |
| CLI 接口 | --config/--resume | --task/--resume/--status/--report |

---

## 9. 测试验证方式

1. **单元测试**: 测试 TaskManager 的状态流转逻辑
2. **集成测试**: 用一个小任务（如"创建一个 Hello World 项目"）端到端运行
3. **中断恢复测试**: 运行中 Ctrl+C，然后 `--resume` 确认能继续
4. **失败重试测试**: 人为制造一个会失败的任务，确认重试和跳过机制正常
