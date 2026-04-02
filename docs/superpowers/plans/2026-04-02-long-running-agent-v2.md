# 长时运行智能体框架 v2 实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将现有的 Shell+双智能体框架升级为 Python 编排+三智能体（Planner/Worker/Evaluator）框架，支持任务拆分、独立验证、重试、中断恢复和结构化日志。

**Architecture:** Python 主编排器调用 `claude` CLI 驱动三个角色智能体。TaskManager 管理任务状态机（pending→in_progress→evaluating→passed/failed/skipped），所有状态实时持久化到 task_list.json，执行日志写入 JSONL 文件。

**Tech Stack:** Python 3.8+, Claude Code CLI, Git, JSON/JSONL

---

## 文件结构

```
long-running-agent/
├── main.py                    # 入口 + 主编排循环（新建）
├── config.py                  # 配置加载与验证（新建）
├── task_manager.py            # 任务生命周期管理（新建）
├── agent_runner.py            # Claude CLI 调用封装（新建）
├── logger.py                  # 结构化日志（新建）
├── prompt_renderer.py         # 提示词模板渲染（重写自 utils/render_prompt.py）
├── config.json                # 默认配置模板（修改）
├── prompts/
│   ├── planner.md             # 规划器提示词（重写自 init-agent.md）
│   ├── worker.md              # 执行器提示词（重写自 worker-agent.md）
│   └── evaluator.md           # 评估器提示词（新建）
└── examples/
    ├── java-springboot.json   # 保留
    ├── vue-frontend.json      # 保留
    └── fullstack.json         # 保留
```

---

### Task 1: config.py — 配置加载

**Files:**
- Create: `long-running-agent/config.py`

- [ ] **Step 1: 编写 config.py**

```python
#!/usr/bin/env python3
"""配置加载与验证"""
import json
import os
import sys


class Config:
    """框架配置，从 config.json 加载"""

    def __init__(self, data: dict, config_dir: str):
        self.project_name = data.get('project_name', 'my-project')
        self.project_type = data.get('project_type', 'java-springboot')
        self.project_description = data.get('project_description', '')
        self.user_task = data.get('user_task', '')

        # 工作目录
        work_dir = data.get('work_directory', '.')
        if work_dir == '.':
            self.work_directory = os.getcwd()
        else:
            self.work_directory = os.path.abspath(work_dir)

        # 智能体参数
        agent = data.get('agent', {})
        self.model = agent.get('model', 'opus')
        self.planner_max_turns = agent.get('planner_max_turns', 200)
        self.worker_max_turns = agent.get('worker_max_turns', 150)
        self.evaluator_max_turns = agent.get('evaluator_max_turns', 50)
        self.max_sessions = agent.get('max_sessions', 50)
        self.cooldown_seconds = agent.get('cooldown_seconds', 5)
        self.task_timeout_seconds = agent.get('task_timeout_seconds', 600)

        # 重试
        retry = data.get('retry', {})
        self.max_attempts = retry.get('max_attempts', 3)

        # Git
        git = data.get('git', {})
        self.git_auto_commit = git.get('auto_commit', True)

        # 路径
        paths = data.get('paths', {})
        self.task_list_file = paths.get('task_list', 'task_list.json')
        self.progress_file = paths.get('progress_file', 'claude-progress.txt')
        self.execution_log_file = paths.get('execution_log', 'execution_log.jsonl')
        self.log_dir = paths.get('log_dir', 'logs')

        # 构建命令
        build = data.get('build', {})
        self.compile_command = build.get('compile_command', '')
        self.test_command = build.get('test_command', '')
        self.run_command = build.get('run_command', '')

        # 框架自身目录（prompts/ 所在位置）
        self.framework_dir = config_dir

    @classmethod
    def load(cls, config_path: str) -> 'Config':
        config_path = os.path.abspath(config_path)
        if not os.path.exists(config_path):
            print(f"错误: 配置文件不存在: {config_path}", file=sys.stderr)
            sys.exit(1)
        with open(config_path, 'r', encoding='utf-8') as f:
            data = json.load(f)
        config_dir = os.path.dirname(config_path)
        return cls(data, config_dir)

    @property
    def task_list_path(self) -> str:
        return os.path.join(self.work_directory, self.task_list_file)

    @property
    def progress_path(self) -> str:
        return os.path.join(self.work_directory, self.progress_file)

    @property
    def execution_log_path(self) -> str:
        return os.path.join(self.work_directory, self.execution_log_file)

    @property
    def log_dir_path(self) -> str:
        return os.path.join(self.work_directory, self.log_dir)
```

- [ ] **Step 2: 验证**

```bash
cd long-running-agent
python -c "from config import Config; c = Config.load('config.json'); print(c.project_name, c.model)"
```

Expected: 输出 `my-project opus`

- [ ] **Step 3: 提交**

```bash
git add long-running-agent/config.py
git commit -m "feat: add config.py for framework v2"
```

---

### Task 2: logger.py — 结构化日志

**Files:**
- Create: `long-running-agent/logger.py`

- [ ] **Step 1: 编写 logger.py**

```python
#!/usr/bin/env python3
"""结构化日志 — JSONL 文件 + 控制台输出"""
import json
import os
from datetime import datetime


class ExecutionLogger:
    """双输出日志：JSONL 文件（机器可读）+ 控制台（人可读）"""

    def __init__(self, log_path: str):
        self.log_path = log_path
        os.makedirs(os.path.dirname(log_path) or '.', exist_ok=True)

    def event(self, event_type: str, data: dict = None):
        """记录一个事件"""
        entry = {
            'ts': datetime.now().isoformat(timespec='seconds'),
            'event': event_type,
            'data': data or {}
        }
        # 写入 JSONL
        with open(self.log_path, 'a', encoding='utf-8') as f:
            f.write(json.dumps(entry, ensure_ascii=False) + '\n')
        # 控制台
        ts = entry['ts'][:19]
        data_str = json.dumps(data, ensure_ascii=False) if data else ''
        print(f"[{ts}] {event_type} {data_str}")

    def info(self, msg: str):
        self.event('info', {'message': msg})

    def warn(self, msg: str):
        self.event('warn', {'message': msg})

    def error(self, msg: str):
        self.event('error', {'message': msg})
```

- [ ] **Step 2: 验证**

```bash
python -c "
from logger import ExecutionLogger
log = ExecutionLogger('test_log.jsonl')
log.event('test', {'a': 1})
log.info('hello')
import os; os.remove('test_log.jsonl')
print('OK')
"
```

Expected: 控制台输出两行日志 + `OK`

- [ ] **Step 3: 提交**

```bash
git add long-running-agent/logger.py
git commit -m "feat: add logger.py structured execution logging"
```

---

### Task 3: task_manager.py — 任务生命周期管理

**Files:**
- Create: `long-running-agent/task_manager.py`

- [ ] **Step 1: 编写 task_manager.py**

```python
#!/usr/bin/env python3
"""任务生命周期管理 — 状态机 + 持久化"""
import json
import os
from datetime import datetime


class TaskManager:
    """
    管理 task_list.json 的读写和任务状态流转。
    状态: pending → in_progress → evaluating → passed / failed → skipped
    """

    def __init__(self, task_list_path: str, max_attempts: int = 3):
        self.path = task_list_path
        self.max_attempts = max_attempts
        self.meta = {}
        self.tasks = []

    def has_existing_tasks(self) -> bool:
        return os.path.exists(self.path)

    def load(self):
        with open(self.path, 'r', encoding='utf-8') as f:
            data = json.load(f)
        # 兼容两种格式: 纯数组（v1）或 {meta, tasks}（v2）
        if isinstance(data, list):
            self.meta = {}
            self.tasks = data
            # 迁移 v1 字段名
            for t in self.tasks:
                if 'passes' in t and 'status' not in t:
                    t['status'] = 'passed' if t['passes'] else 'pending'
                if 'steps' in t and 'verification_steps' not in t:
                    t['verification_steps'] = t['steps']
                t.setdefault('attempts', 0)
                t.setdefault('max_attempts', self.max_attempts)
                t.setdefault('result', None)
                t.setdefault('evaluation', None)
                t.setdefault('error_history', [])
                t.setdefault('started_at', None)
                t.setdefault('completed_at', None)
        else:
            self.meta = data.get('meta', {})
            self.tasks = data.get('tasks', [])

    def save(self):
        data = {'meta': self.meta, 'tasks': self.tasks}
        with open(self.path, 'w', encoding='utf-8') as f:
            json.dump(data, f, ensure_ascii=False, indent=2)

    def next_task(self):
        """返回下一个可执行的任务，或 None"""
        candidates = []
        for t in self.tasks:
            if t['status'] in ('passed', 'skipped'):
                continue
            if t['status'] == 'failed' and t['attempts'] >= t.get('max_attempts', self.max_attempts):
                t['status'] = 'skipped'
                continue
            if not self._dependencies_met(t):
                continue
            candidates.append(t)
        if not candidates:
            return None
        candidates.sort(key=lambda x: (x.get('priority', 99), x.get('id', '')))
        return candidates[0]

    def _dependencies_met(self, task) -> bool:
        deps = task.get('dependencies', [])
        if not deps:
            return True
        status_map = {t['id']: t['status'] for t in self.tasks}
        for dep_id in deps:
            if status_map.get(dep_id) != 'passed':
                return False
        return True

    def update_status(self, task_id: str, status: str, reason: str = ''):
        task = self._find(task_id)
        task['status'] = status
        if status == 'in_progress':
            task['started_at'] = datetime.now().isoformat(timespec='seconds')
            task['attempts'] = task.get('attempts', 0) + 1

    def mark_passed(self, task_id: str, eval_result: dict):
        task = self._find(task_id)
        task['status'] = 'passed'
        task['evaluation'] = eval_result
        task['completed_at'] = datetime.now().isoformat(timespec='seconds')

    def mark_failed(self, task_id: str, eval_result: dict):
        task = self._find(task_id)
        if task['attempts'] >= task.get('max_attempts', self.max_attempts):
            task['status'] = 'skipped'
        else:
            task['status'] = 'failed'
        task['evaluation'] = eval_result
        task['error_history'] = task.get('error_history', [])
        task['error_history'].append({
            'attempt': task['attempts'],
            'feedback': eval_result.get('feedback', ''),
            'ts': datetime.now().isoformat(timespec='seconds')
        })

    def _find(self, task_id: str) -> dict:
        for t in self.tasks:
            if t['id'] == task_id:
                return t
        raise ValueError(f"任务 {task_id} 不存在")

    # ---- 统计 ----

    def total_count(self) -> int:
        return len(self.tasks)

    def pending_count(self) -> int:
        return len([t for t in self.tasks if t['status'] in ('pending', 'failed')])

    def passed_count(self) -> int:
        return len([t for t in self.tasks if t['status'] == 'passed'])

    def skipped_count(self) -> int:
        return len([t for t in self.tasks if t['status'] == 'skipped'])

    def summary(self) -> str:
        total = self.total_count()
        passed = self.passed_count()
        skipped = self.skipped_count()
        pending = self.pending_count()
        pct = (passed / total * 100) if total else 0

        lines = [
            f"进度: {passed}/{total} ({pct:.1f}%)",
            f"  已完成: {passed}  失败/跳过: {skipped}  待处理: {pending}",
        ]

        # 按类别
        cats = {}
        for t in self.tasks:
            c = t.get('category', 'other')
            cats.setdefault(c, [0, 0])
            cats[c][0] += 1
            if t['status'] == 'passed':
                cats[c][1] += 1
        for c, (tot, done) in sorted(cats.items()):
            bar_len = int(done / tot * 20) if tot else 0
            bar = '█' * bar_len + '░' * (20 - bar_len)
            lines.append(f"  {c:<18} {done}/{tot:<4} {bar}")

        return '\n'.join(lines)

    def generate_report(self) -> dict:
        return {
            'total': self.total_count(),
            'passed': self.passed_count(),
            'skipped': self.skipped_count(),
            'pending': self.pending_count(),
            'tasks': [
                {
                    'id': t['id'],
                    'title': t.get('title', t.get('description', '')),
                    'status': t['status'],
                    'attempts': t.get('attempts', 0),
                }
                for t in self.tasks
            ]
        }
```

- [ ] **Step 2: 验证**

```bash
python -c "
from task_manager import TaskManager
import json, tempfile, os

# 创建临时 task_list
data = {'meta': {}, 'tasks': [
    {'id': 'T1', 'status': 'pending', 'priority': 1, 'dependencies': [], 'attempts': 0, 'max_attempts': 3,
     'category': 'test', 'title': 'test task', 'verification_steps': ['check'], 'result': None, 'evaluation': None, 'error_history': [], 'started_at': None, 'completed_at': None},
]}
path = os.path.join(tempfile.gettempdir(), 'test_tasks.json')
with open(path, 'w') as f: json.dump(data, f)

mgr = TaskManager(path)
mgr.load()
t = mgr.next_task()
assert t['id'] == 'T1'
mgr.update_status('T1', 'in_progress')
mgr.mark_passed('T1', {'passed': True})
mgr.save()
assert mgr.next_task() is None
os.remove(path)
print('OK')
"
```

Expected: `OK`

- [ ] **Step 3: 提交**

```bash
git add long-running-agent/task_manager.py
git commit -m "feat: add task_manager.py with state machine and persistence"
```

---

### Task 4: prompt_renderer.py — 提示词渲染

**Files:**
- Create: `long-running-agent/prompt_renderer.py`

- [ ] **Step 1: 编写 prompt_renderer.py**

```python
#!/usr/bin/env python3
"""提示词模板渲染 — 将 {{VAR}} 替换为配置值"""
import json
import os


def render_prompt(template_path: str, variables: dict) -> str:
    """读取模板文件并替换所有 {{KEY}} 占位符"""
    with open(template_path, 'r', encoding='utf-8') as f:
        template = f.read()
    for key, value in variables.items():
        template = template.replace('{{' + key + '}}', str(value))
    return template


def build_variables(config, extra: dict = None) -> dict:
    """从 Config 对象构建模板变量字典"""
    var_map = {
        'PROJECT_NAME': config.project_name,
        'PROJECT_TYPE': config.project_type,
        'PROJECT_DESCRIPTION': config.project_description,
        'WORK_DIRECTORY': config.work_directory,
        'USER_TASK': config.user_task,
        'TASK_LIST_FILE': config.task_list_file,
        'PROGRESS_FILE': config.progress_file,
        'COMPILE_COMMAND': config.compile_command,
        'TEST_COMMAND': config.test_command,
        'RUN_COMMAND': config.run_command,
    }
    if extra:
        var_map.update(extra)
    return var_map
```

- [ ] **Step 2: 提交**

```bash
git add long-running-agent/prompt_renderer.py
git commit -m "feat: add prompt_renderer.py template engine"
```

---

### Task 5: agent_runner.py — Claude CLI 封装

**Files:**
- Create: `long-running-agent/agent_runner.py`

- [ ] **Step 1: 编写 agent_runner.py**

```python
#!/usr/bin/env python3
"""Claude CLI 调用封装 — 驱动 Planner/Worker/Evaluator"""
import json
import os
import subprocess
import re

from config import Config
from prompt_renderer import render_prompt, build_variables
from logger import ExecutionLogger


class AgentRunner:
    """封装对 claude CLI 的调用"""

    def __init__(self, config: Config, logger: ExecutionLogger):
        self.config = config
        self.logger = logger
        self.prompts_dir = os.path.join(config.framework_dir, 'prompts')
        os.makedirs(config.log_dir_path, exist_ok=True)

    # ---- Planner ----

    def run_planner(self, user_task: str):
        """运行规划器智能体，生成 task_list.json"""
        variables = build_variables(self.config, {'USER_TASK': user_task})
        prompt = render_prompt(
            os.path.join(self.prompts_dir, 'planner.md'), variables
        )
        log_file = os.path.join(self.config.log_dir_path, 'planner.log')
        self._run_claude(prompt, self.config.planner_max_turns, log_file)

    # ---- Worker ----

    def run_worker(self, task: dict, session: int) -> dict:
        """运行执行器智能体，实现一个任务"""
        error_context = ''
        if task.get('error_history'):
            last_err = task['error_history'][-1]
            error_context = (
                f"\n\n## 上次失败信息（第 {last_err['attempt']} 次尝试）\n"
                f"{last_err['feedback']}\n"
                f"请根据以上反馈修复问题。\n"
            )

        variables = build_variables(self.config, {
            'SESSION_NUMBER': str(session),
            'TASK_ID': task['id'],
            'TASK_TITLE': task.get('title', task.get('description', '')),
            'TASK_DESCRIPTION': task.get('description', ''),
            'VERIFICATION_STEPS': json.dumps(
                task.get('verification_steps', []), ensure_ascii=False
            ),
            'ERROR_CONTEXT': error_context,
        })
        prompt = render_prompt(
            os.path.join(self.prompts_dir, 'worker.md'), variables
        )
        log_file = os.path.join(
            self.config.log_dir_path,
            f"worker-{task['id']}-s{session}.log"
        )
        return self._run_claude(prompt, self.config.worker_max_turns, log_file)

    # ---- Evaluator ----

    def run_evaluator(self, task: dict) -> dict:
        """运行评估器智能体，验证任务完成质量"""
        variables = build_variables(self.config, {
            'TASK_ID': task['id'],
            'TASK_TITLE': task.get('title', task.get('description', '')),
            'TASK_DESCRIPTION': task.get('description', ''),
            'VERIFICATION_STEPS': json.dumps(
                task.get('verification_steps', []), ensure_ascii=False
            ),
        })
        prompt = render_prompt(
            os.path.join(self.prompts_dir, 'evaluator.md'), variables
        )
        log_file = os.path.join(
            self.config.log_dir_path, f"eval-{task['id']}.log"
        )
        result = self._run_claude(prompt, self.config.evaluator_max_turns, log_file)
        return self._parse_evaluation(result, log_file)

    def _parse_evaluation(self, result: dict, log_file: str) -> dict:
        """从 Evaluator 输出中解析评估结果 JSON"""
        output = result.get('output', '')

        # 尝试从输出中提取 JSON 块
        json_match = re.search(r'\{[^{}]*"passed"\s*:', output, re.DOTALL)
        if json_match:
            # 找到包含 "passed" 的 JSON，尝试提取完整 JSON
            start = json_match.start()
            brace_count = 0
            end = start
            for i in range(start, len(output)):
                if output[i] == '{':
                    brace_count += 1
                elif output[i] == '}':
                    brace_count -= 1
                    if brace_count == 0:
                        end = i + 1
                        break
            try:
                return json.loads(output[start:end])
            except json.JSONDecodeError:
                pass

        # 如果无法解析，尝试从日志文件读取
        if os.path.exists(log_file):
            with open(log_file, 'r', encoding='utf-8') as f:
                content = f.read()
            # 同样的 JSON 提取逻辑
            json_match = re.search(r'\{[^{}]*"passed"\s*:', content, re.DOTALL)
            if json_match:
                start = json_match.start()
                brace_count = 0
                end = start
                for i in range(start, len(content)):
                    if content[i] == '{':
                        brace_count += 1
                    elif content[i] == '}':
                        brace_count -= 1
                        if brace_count == 0:
                            end = i + 1
                            break
                try:
                    return json.loads(content[start:end])
                except json.JSONDecodeError:
                    pass

        # 兜底：无法解析则视为失败
        self.logger.warn(f"无法解析 Evaluator 输出，默认为失败")
        return {
            'passed': False,
            'score': 0,
            'feedback': '无法解析评估结果，请检查 Evaluator 输出日志',
            'checks': []
        }

    # ---- 底层调用 ----

    def _run_claude(self, prompt: str, max_turns: int, log_file: str) -> dict:
        """调用 claude CLI 并返回结果"""
        cmd = [
            'claude', '-p', prompt,
            '--model', self.config.model,
            '--max-turns', str(max_turns),
            '--output-format', 'json',
        ]
        try:
            proc = subprocess.run(
                cmd,
                capture_output=True,
                text=True,
                timeout=self.config.task_timeout_seconds,
                cwd=self.config.work_directory,
            )
            # 保存完整输出
            with open(log_file, 'w', encoding='utf-8') as f:
                f.write(proc.stdout or '')
                if proc.stderr:
                    f.write('\n--- STDERR ---\n')
                    f.write(proc.stderr)

            return {
                'success': proc.returncode == 0,
                'output': proc.stdout or '',
                'exit_code': proc.returncode,
            }
        except subprocess.TimeoutExpired:
            self.logger.error(f"Claude CLI 超时 ({self.config.task_timeout_seconds}s)")
            return {'success': False, 'output': '', 'error': 'timeout'}
        except FileNotFoundError:
            self.logger.error("claude 命令未找到，请确认 Claude Code CLI 已安装")
            return {'success': False, 'output': '', 'error': 'claude_not_found'}
        except Exception as e:
            self.logger.error(f"调用 Claude CLI 异常: {e}")
            return {'success': False, 'output': '', 'error': str(e)}
```

- [ ] **Step 2: 提交**

```bash
git add long-running-agent/agent_runner.py
git commit -m "feat: add agent_runner.py Claude CLI wrapper"
```

---

### Task 6: 三个提示词模板

**Files:**
- Create: `long-running-agent/prompts/planner.md`
- Create: `long-running-agent/prompts/worker.md`
- Create: `long-running-agent/prompts/evaluator.md`

- [ ] **Step 1: 编写 planner.md**

```markdown
# 规划器智能体指令

你是一个任务规划专家。你的唯一职责是分析用户的任务描述，将其拆解为细粒度的子任务清单，并创建项目脚手架文件。

**严禁实现任何功能代码。你只负责规划和创建脚手架。**

## 项目信息
- **项目名称**: {{PROJECT_NAME}}
- **项目类型**: {{PROJECT_TYPE}}
- **工作目录**: {{WORK_DIRECTORY}}

## 用户任务
{{USER_TASK}}

---

## 你必须按顺序执行以下步骤：

### STEP 1: 了解环境
1. 运行 `pwd` 确认当前工作目录
2. 运行 `ls -la` 查看目录结构
3. 如果已有代码，阅读关键文件（pom.xml, package.json, README 等）
4. 确认可用的开发工具（java, mvn, node, npm, python 等）

### STEP 2: 分析任务并生成 task_list.json

这是**最关键的步骤**。将用户任务拆解为一个**全面的、细粒度的子任务清单**。

**生成格式（必须严格遵守）：**

```json
{
  "meta": {
    "project_name": "{{PROJECT_NAME}}",
    "created_at": "当前时间ISO格式",
    "user_task": "用户的原始任务描述",
    "total_tasks": 任务总数
  },
  "tasks": [
    {
      "id": "T001",
      "category": "setup",
      "title": "简短标题",
      "description": "详细描述这个任务要做什么",
      "verification_steps": [
        "验证步骤1：具体的、可执行的检查项",
        "验证步骤2：具体的、可执行的检查项"
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

**规则：**
- 每个任务必须是**可独立实现和验证**的最小单元
- 任务之间的依赖关系必须明确标注在 `dependencies` 中
- 所有任务的 `status` 必须为 `"pending"`
- 优先级 1（最高）到 5（最低）
- 一个中型项目应有 30-80 个任务
- `verification_steps` 必须是具体的、可执行的检查项（如"运行 mvn compile 无错误"，而不是"代码正确"）

**分类：** setup, database, functional, api, frontend, security, testing, integration, deployment

**对于软件项目的典型拆分：**
1. (setup, priority=1) 项目结构、依赖管理、基础配置
2. (database, priority=2) 数据模型、建表、Entity/Model
3. (functional, priority=2) Service 层核心业务逻辑
4. (api, priority=3) Controller / API 接口
5. (frontend, priority=3) 前端页面和组件
6. (security, priority=3) 认证、授权、安全
7. (testing, priority=4) 单元测试、集成测试
8. (integration, priority=4) 前后端联调
9. (deployment, priority=5) 打包、部署配置

### STEP 3: 创建 init.sh 脚本

编写 `init.sh`，后续执行器智能体可以一键检查和启动开发环境：

```bash
#!/bin/bash
echo "=== {{PROJECT_NAME}} 环境检查 ==="
# 检查前置条件
# 安装依赖
# 编译检查
# 冒烟测试
echo "=== 检查完成 ==="
```

同时创建 `init.bat`（Windows 版本）。

### STEP 4: 创建 claude-progress.txt

```
# {{PROJECT_NAME}} - 进度跟踪

## 项目概述
[简要描述]

## 架构决策
[技术选型和架构决策]

## 会话记录

### Session #1 - 规划 (时间)
- 创建了 task_list.json (共 N 个任务)
- 创建了 init.sh
- 状态: 规划完成
```

### STEP 5: Git 初始提交

```bash
git add task_list.json claude-progress.txt init.sh
git commit -m "plan: 项目规划 - N 个任务"
```

### STEP 6: 输出摘要

输出创建的文件清单、任务统计（总数、各类别数量）、下一步建议。

---

## 重要约束

- **不要实现任何功能代码**
- **任务清单要全面**，后续智能体只能处理清单中的任务
- **verification_steps 要具体可执行**，这是评估器验证的依据
- 提交后工作目录应无未跟踪文件
```

- [ ] **Step 2: 编写 worker.md**

```markdown
# 执行器智能体指令 — 会话 #{{SESSION_NUMBER}}

你是项目 **{{PROJECT_NAME}}** 的编码智能体。你的任务是在本次会话中**完成一个指定任务**，并在结束时将环境保持在干净状态。

## 当前任务
- **任务 ID**: {{TASK_ID}}
- **任务标题**: {{TASK_TITLE}}
- **任务描述**: {{TASK_DESCRIPTION}}
- **验证步骤**: {{VERIFICATION_STEPS}}

## 项目信息
- **项目名称**: {{PROJECT_NAME}}
- **工作目录**: {{WORK_DIRECTORY}}
- **当前会话**: #{{SESSION_NUMBER}}

{{ERROR_CONTEXT}}

---

## 你必须严格按顺序执行以下步骤：

### STEP 1: 了解当前状态（必做！）

在做任何事情之前，先了解情况：

1. 运行 `pwd` 确认工作目录
2. 读取 `{{PROGRESS_FILE}}`，了解之前完成了什么工作
3. 运行 `git log --oneline -10`，查看最近的提交记录
4. 运行 `git status`，确认工作目录是否干净

### STEP 2: 验证环境健康

1. 如果编译命令可用，运行: `{{COMPILE_COMMAND}}`
2. 如果编译失败，**优先修复编译错误**，不要开始新任务
3. 如果有运行中的服务，确认能正常响应

**关键原则**: 如果环境有问题，先修复问题。在损坏的基础上添加新功能只会让情况更糟。

### STEP 3: 阅读任务详情

仔细阅读当前任务的描述和验证步骤。理解这个任务需要做什么，以及如何验证。

### STEP 4: 实现任务

针对当前任务：

1. 规划实现方案
2. 编写代码
3. 保持代码质量：遵循项目已有代码风格，处理异常情况
4. **一次只做当前这一个任务**，不要触碰其他任务

### STEP 5: 自测

按照验证步骤逐条检查：

1. 运行编译确认通过
2. 如果有测试命令，运行测试
3. 按 verification_steps 中的每一步手动验证
4. 如果验证失败，修复代码并重新验证

### STEP 6: Git 提交

```bash
git add .
git commit -m "feat({{TASK_ID}}): {{TASK_TITLE}}"
```

### STEP 7: 更新进度文件

在 `{{PROGRESS_FILE}}` **末尾追加**（不要覆盖已有内容）：

```
### Session #{{SESSION_NUMBER}} - {{TASK_ID}} (时间)
- 实现了: {{TASK_TITLE}}
- 修改的文件: [文件列表]
- 自测结果: [通过/失败]
- Git 提交: [commit hash]
- 下一步建议: [给下一个会话的建议]
```

### STEP 8: 最终验证

1. `git status` — 确认工作目录干净
2. 运行编译命令 — 确认整个项目仍可编译
3. 确认没有引入新的编译警告或错误

---

## 核心原则

1. **专注当前任务**: 只做 {{TASK_ID}}，不要触碰其他任务
2. **干净状态**: 离开时环境必须可编译，代码已提交
3. **先修后建**: 发现已有问题先修复，再实现新功能
4. **留下痕迹**: 通过 Git 提交和进度文件让下一个会话能快速了解状况
```

- [ ] **Step 3: 编写 evaluator.md**

```markdown
# 评估器智能体指令

你是项目 **{{PROJECT_NAME}}** 的**质量检查员**。你的唯一职责是验证一个任务是否被正确完成。

**严禁修改任何代码。你只负责检查和评估。**

## 待评估任务
- **任务 ID**: {{TASK_ID}}
- **任务标题**: {{TASK_TITLE}}
- **任务描述**: {{TASK_DESCRIPTION}}
- **验证步骤**: {{VERIFICATION_STEPS}}

## 项目信息
- **项目名称**: {{PROJECT_NAME}}
- **工作目录**: {{WORK_DIRECTORY}}

---

## 你必须严格按顺序执行以下步骤：

### STEP 1: 了解项目状态

1. 运行 `pwd` 确认工作目录
2. 运行 `git log --oneline -5` 查看最近提交
3. 运行 `git diff HEAD~1 --stat` 查看最近一次提交改了什么文件

### STEP 2: 逐条验证

逐条执行验证步骤。对于每一条：

1. **执行验证操作**（运行命令、检查文件、测试接口等）
2. **记录实际结果**
3. **判定通过或失败**

**验证原则：**
- 每一条验证步骤都必须实际执行，不能跳过
- 不确定的不能标记为通过
- 如果某条验证步骤描述模糊，用最合理的方式验证
- 编译必须真正运行编译命令，不能只看文件是否存在

### STEP 3: 输出评估报告

在你的回复中，输出一个**严格的 JSON 格式**评估报告。这个 JSON 必须可以被程序解析：

```json
{
  "task_id": "{{TASK_ID}}",
  "passed": true或false,
  "score": 0到100的整数,
  "checks": [
    {
      "step": "验证步骤的原文",
      "passed": true或false,
      "detail": "实际检查结果的说明"
    }
  ],
  "feedback": "如果失败，说明具体原因和修复建议。如果通过，留空字符串。",
  "suggestions": ["改进建议1", "改进建议2"]
}
```

**评分标准：**
- 所有验证步骤都通过: 80-100 分
- 大部分通过但有小问题: 60-79 分
- 核心功能未实现或有严重问题: 0-59 分
- `passed` 为 true 的条件: score >= 70 且所有关键验证步骤通过

**评估态度：**
- 你是严格的质量检查员，不是友善的同事
- 功能必须真正可用，不是"看起来差不多"
- 编译警告虽然不致命，但应记录在 suggestions 中
- 如果失败，feedback 中必须给出**具体的修复建议**，而不是笼统的"请修复"

---

## 重要约束

- **绝对不要修改任何代码文件**
- **绝对不要运行 git commit**
- **绝对不要修改 task_list.json**
- 你只能运行只读命令（cat, ls, mvn compile, npm run build, curl 等）
- 输出的 JSON 必须是合法的、可解析的 JSON
```

- [ ] **Step 4: 提交**

```bash
git add long-running-agent/prompts/planner.md long-running-agent/prompts/worker.md long-running-agent/prompts/evaluator.md
git commit -m "feat: add three-agent prompt templates (planner/worker/evaluator)"
```

---

### Task 7: main.py — 入口与主编排循环

**Files:**
- Create: `long-running-agent/main.py`

- [ ] **Step 1: 编写 main.py**

```python
#!/usr/bin/env python3
"""
长时运行智能体框架 v2 — 主编排器

用法:
    python main.py --task "构建一个用户管理系统"       # 新任务
    python main.py --resume                            # 从断点恢复
    python main.py --resume --config custom.json       # 指定配置恢复
    python main.py --status                            # 查看进度
    python main.py --report                            # 生成报告
"""
import argparse
import json
import os
import sys
import time

from config import Config
from task_manager import TaskManager
from agent_runner import AgentRunner
from logger import ExecutionLogger


def parse_args():
    parser = argparse.ArgumentParser(description='长时运行智能体框架 v2')
    parser.add_argument('--task', type=str, help='任务描述')
    parser.add_argument('--resume', action='store_true', help='从断点恢复')
    parser.add_argument('--config', type=str, default=None, help='配置文件路径')
    parser.add_argument('--status', action='store_true', help='查看当前进度')
    parser.add_argument('--report', action='store_true', help='生成最终报告')
    parser.add_argument('--max-sessions', type=int, default=None, help='覆盖最大会话数')
    return parser.parse_args()


def find_config(explicit_path: str = None) -> str:
    """查找配置文件"""
    if explicit_path:
        return explicit_path
    # 默认查找当前目录或框架目录
    candidates = [
        'config.json',
        os.path.join(os.path.dirname(__file__), 'config.json'),
    ]
    for c in candidates:
        if os.path.exists(c):
            return c
    print("错误: 找不到 config.json，请用 --config 指定", file=sys.stderr)
    sys.exit(1)


def format_report(report: dict) -> str:
    """格式化最终报告"""
    lines = [
        '',
        '=' * 60,
        '  最终报告',
        '=' * 60,
        f"  总任务数:  {report['total']}",
        f"  已完成:    {report['passed']}",
        f"  已跳过:    {report['skipped']}",
        f"  待处理:    {report['pending']}",
        '',
    ]
    if report['passed'] > 0:
        lines.append('  已完成的任务:')
        for t in report['tasks']:
            if t['status'] == 'passed':
                lines.append(f"    ✓ [{t['id']}] {t['title']}")
    if report['skipped'] > 0:
        lines.append('  已跳过的任务 (失败超过重试上限):')
        for t in report['tasks']:
            if t['status'] == 'skipped':
                lines.append(f"    ✗ [{t['id']}] {t['title']} (尝试 {t['attempts']} 次)")
    if report['pending'] > 0:
        lines.append('  未完成的任务:')
        for t in report['tasks']:
            if t['status'] in ('pending', 'failed'):
                lines.append(f"    - [{t['id']}] {t['title']}")
    lines.append('=' * 60)
    return '\n'.join(lines)


def main():
    args = parse_args()
    config_path = find_config(args.config)
    config = Config.load(config_path)

    if args.max_sessions:
        config.max_sessions = args.max_sessions

    task_mgr = TaskManager(config.task_list_path, config.max_attempts)

    # --status: 只看进度
    if args.status:
        if not task_mgr.has_existing_tasks():
            print("尚未开始任何任务。")
            return
        task_mgr.load()
        print(task_mgr.summary())
        return

    # --report: 生成报告
    if args.report:
        if not task_mgr.has_existing_tasks():
            print("尚未开始任何任务。")
            return
        task_mgr.load()
        print(format_report(task_mgr.generate_report()))
        return

    # 需要任务描述或恢复
    if not args.resume and not args.task and not config.user_task:
        print("错误: 请用 --task 指定任务，或在 config.json 中设置 user_task", file=sys.stderr)
        sys.exit(1)

    user_task = args.task or config.user_task
    logger = ExecutionLogger(config.execution_log_path)
    runner = AgentRunner(config, logger)

    # ---- 阶段一: 规划 ----
    if args.resume and task_mgr.has_existing_tasks():
        task_mgr.load()
        logger.event('session_resume', {
            'pending': task_mgr.pending_count(),
            'passed': task_mgr.passed_count(),
        })
        print(f"\n从断点恢复。{task_mgr.summary()}\n")
    else:
        logger.event('planner_start', {'task': user_task})
        print(f"\n启动规划器智能体...\n任务: {user_task}\n")
        runner.run_planner(user_task)

        if not task_mgr.has_existing_tasks():
            logger.error("规划器未生成 task_list.json")
            print("错误: 规划器未生成 task_list.json", file=sys.stderr)
            sys.exit(1)

        task_mgr.load()
        logger.event('planner_done', {'tasks_count': task_mgr.total_count()})
        print(f"\n规划完成。共 {task_mgr.total_count()} 个任务。\n")

    # ---- 阶段二: 执行循环 ----
    session = 0
    try:
        while True:
            task = task_mgr.next_task()
            if task is None:
                logger.event('all_done', {
                    'passed': task_mgr.passed_count(),
                    'skipped': task_mgr.skipped_count(),
                })
                print("\n没有更多可执行的任务。")
                break

            if session >= config.max_sessions:
                logger.warn(f"达到最大会话数 {config.max_sessions}")
                break

            session += 1
            attempt = task.get('attempts', 0) + 1
            task_id = task['id']
            task_title = task.get('title', task.get('description', ''))

            print(f"\n{'='*60}")
            print(f"  会话 #{session} | 任务 {task_id}: {task_title}")
            print(f"  尝试: {attempt}/{task.get('max_attempts', config.max_attempts)}")
            print(f"  剩余: {task_mgr.pending_count()} 个任务")
            print(f"{'='*60}\n")

            # Worker 执行
            logger.event('worker_start', {
                'task_id': task_id, 'session': session, 'attempt': attempt
            })
            task_mgr.update_status(task_id, 'in_progress')
            task_mgr.save()

            worker_result = runner.run_worker(task, session)
            logger.event('worker_done', {
                'task_id': task_id,
                'success': worker_result.get('success', False),
            })

            # 如果 Worker 本身就失败了（超时等）
            if not worker_result.get('success', False):
                logger.event('worker_failed', {
                    'task_id': task_id,
                    'error': worker_result.get('error', 'unknown'),
                })
                task_mgr.mark_failed(task_id, {
                    'passed': False,
                    'score': 0,
                    'feedback': f"Worker 执行失败: {worker_result.get('error', 'unknown')}",
                    'checks': [],
                })
                task_mgr.save()
                time.sleep(config.cooldown_seconds)
                continue

            # Evaluator 验证
            logger.event('eval_start', {'task_id': task_id})
            task_mgr.update_status(task_id, 'evaluating')
            task_mgr.save()

            eval_result = runner.run_evaluator(task)
            passed = eval_result.get('passed', False)
            score = eval_result.get('score', 0)

            logger.event('eval_done', {
                'task_id': task_id,
                'passed': passed,
                'score': score,
            })

            if passed:
                task_mgr.mark_passed(task_id, eval_result)
                print(f"  ✓ 任务 {task_id} 通过 (得分: {score})")
            else:
                task_mgr.mark_failed(task_id, eval_result)
                feedback = eval_result.get('feedback', '')
                remaining = task.get('max_attempts', config.max_attempts) - task['attempts']
                if remaining > 0:
                    print(f"  ✗ 任务 {task_id} 未通过 (得分: {score})，剩余重试: {remaining}")
                    print(f"    原因: {feedback[:200]}")
                else:
                    print(f"  ✗ 任务 {task_id} 已跳过 (超过最大重试次数)")

            task_mgr.save()
            time.sleep(config.cooldown_seconds)

    except KeyboardInterrupt:
        logger.event('interrupted', {
            'session': session,
            'pending': task_mgr.pending_count(),
        })
        task_mgr.save()
        print(f"\n\n中断。已保存进度。使用 --resume 恢复执行。\n")
        print(task_mgr.summary())
        return

    # ---- 阶段三: 报告 ----
    report = task_mgr.generate_report()
    logger.event('session_end', report)
    print(format_report(report))


if __name__ == '__main__':
    main()
```

- [ ] **Step 2: 验证语法**

```bash
cd long-running-agent
python -c "import py_compile; py_compile.compile('main.py', doraise=True); print('OK')"
```

Expected: `OK`

- [ ] **Step 3: 提交**

```bash
git add long-running-agent/main.py
git commit -m "feat: add main.py orchestrator with three-agent loop"
```

---

### Task 8: 更新 config.json 为 v2 格式

**Files:**
- Modify: `long-running-agent/config.json`

- [ ] **Step 1: 更新 config.json**

```json
{
  "project_name": "my-project",
  "project_type": "java-springboot",
  "project_description": "项目描述，用于智能体理解项目上下文",
  "work_directory": ".",
  "user_task": "在此填写你的完整任务描述...",

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
    "max_attempts": 3
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

- [ ] **Step 2: 提交**

```bash
git add long-running-agent/config.json
git commit -m "feat: update config.json to v2 format"
```

---

### Task 9: 编写框架使用文档

**Files:**
- Create: `long-running-agent/README.md`

- [ ] **Step 1: 编写 README.md**

```markdown
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
| Planner | 分析任务，生成细粒度子任务清单 | prompts/planner.md |
| Worker | 每次完成一个子任务，提交代码 | prompts/worker.md |
| Evaluator | 独立验证 Worker 的成果质量 | prompts/evaluator.md |

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

## 执行流程

1. **Planner** 将你的任务拆分为 30-80 个子任务，保存到 `task_list.json`
2. **Worker** 按优先级逐个执行子任务，每次完成后 Git 提交
3. **Evaluator** 独立验证每个子任务的完成质量
4. 通过 → 标记 passed，继续下一个
5. 失败 → 记录原因，Worker 重试（最多 N 次）
6. 超过重试上限 → 跳过，继续下一个
7. 所有任务完成或达到会话上限 → 输出最终报告

## 生成的文件

| 文件 | 说明 |
|------|------|
| `task_list.json` | 任务清单和状态（核心持久化文件） |
| `claude-progress.txt` | 人可读的进度记录 |
| `execution_log.jsonl` | 结构化执行日志 |
| `logs/` | 每次智能体调用的完整输出 |

## 任务状态

```
pending → in_progress → evaluating → passed ✓
                                   → failed → in_progress (重试)
                                            → skipped (超过重试上限)
```

## 命令行参数

| 参数 | 说明 |
|------|------|
| `--task "描述"` | 指定任务描述 |
| `--resume` | 从断点恢复执行 |
| `--config file.json` | 指定配置文件 |
| `--status` | 查看当前进度 |
| `--report` | 生成最终报告 |
| `--max-sessions N` | 覆盖最大会话数 |
```

- [ ] **Step 2: 提交**

```bash
git add long-running-agent/README.md
git commit -m "docs: add README.md for framework v2"
```

---

### Task 10: 端到端验证

- [ ] **Step 1: 验证所有模块可导入**

```bash
cd long-running-agent
python -c "
from config import Config
from task_manager import TaskManager
from agent_runner import AgentRunner
from logger import ExecutionLogger
from prompt_renderer import render_prompt, build_variables
print('所有模块导入成功')
"
```

Expected: `所有模块导入成功`

- [ ] **Step 2: 验证 --status 命令（无任务时）**

```bash
python main.py --status
```

Expected: `尚未开始任何任务。`

- [ ] **Step 3: 验证 --help**

```bash
python main.py --help
```

Expected: 显示帮助信息

- [ ] **Step 4: 验证提示词模板渲染**

```bash
python -c "
from config import Config
from prompt_renderer import render_prompt, build_variables
config = Config.load('config.json')
variables = build_variables(config, {'USER_TASK': 'test task', 'SESSION_NUMBER': '1', 'TASK_ID': 'T001', 'TASK_TITLE': 'test', 'TASK_DESCRIPTION': 'desc', 'VERIFICATION_STEPS': '[]', 'ERROR_CONTEXT': ''})
for name in ['planner', 'worker', 'evaluator']:
    import os
    path = os.path.join(config.framework_dir, 'prompts', f'{name}.md')
    content = render_prompt(path, variables)
    assert '{{' not in content, f'{name}.md 中有未替换的变量'
    print(f'{name}.md 渲染成功 ({len(content)} 字符)')
"
```

Expected: 三个模板都渲染成功，无未替换变量

- [ ] **Step 5: 最终提交**

```bash
git add -A long-running-agent/
git commit -m "feat: long-running-agent framework v2 complete"
```
