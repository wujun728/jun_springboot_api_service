README_AI.md
claude --dangerously-skip-permissions

说明：使用使用中文与我对话，输入中文，返回中文。
原先的前端页面在下面的路径下面，
D:\workspace_github\jun_api_service\jun_dbapi\dbapi-ui
现在需要更换一个不依赖nodejs的前端，将上面的前端页面搬迁到下面的路径下面，
D:\workspace_github\jun_api_service\jun_dbapi\jun_ui_sa_admin
使用新的技术栈 jun_ui_sa_admin 将前端页面进行改造适配，同时需要考虑原有的接口跟后端对接。
新增的前端将会在Nginx中运行。

说明：使用使用中文与我对话，输入中文，返回中文。
任务：组件搬迁及优化适配。
任务说明：下面是反编译后的代码，请勿直接使用，需要搬迁到 D:\workspace_github\jun_api_service\jun_dbapi\jun-dbapi-plugin 目录下面，并作为插件进行使用。需要兼容平台当前代码，需考虑兼容性，需要保持插件正常运行。
D:\workspace_github\jun_api_service\jun_dbapi
D:\workspace_github\jun_api_service\dbapi\advanced-tree-format-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\aes-param-decrypt-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\amis-global-trandform-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\camel-rename-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\data-extract-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\date-format-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\dingtalk-alarm-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\email-alarm-plugin-1.0.0\
D:\workspace_github\jun_api_service\dbapi\email-alarm-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\encrypt-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\feishu-alarm-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\memory-cache-plugin-1.0.0\
D:\workspace_github\jun_api_service\dbapi\pagination-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\sign-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\single-value-extract-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\string-concat-plugin-1.0.0.src\
D:\workspace_github\jun_api_service\dbapi\tree-format-plugin-1.0.0.src\

说明：使用使用中文与我对话，输入中文，返回中文。
任务：老版本优化，首先看下下面的新版本，然后看下老版本，然后进行对比，然后进行优化。尽量少的改动，尽量多的功能。
先给出新版本，然后给出老版本，然后给出对比，然后给出优化。先输出方案到md文件，然后给出代码，然后给出执行结果，然后给出对比，然后给出优化。
任务说明：
新版本：D:\workspace_github\jun_api_service\dbapi\db-api-4.0
老版本：D:\workspace_github\jun_api_service\jun_dbapi



❯ 前端项目sa_admin报错了，你能自动化测试前端项目吗？ 使用 Playwright 测试 调用我在Nginx中部署好的页面 http://dbapi.localhost/，
nginx在目录  D:\Java\nginx-1.24.0  下面运行，nginx.conf在目录 D:\Java\nginx-1.24.0\conf 下面，nginx.exe在目录 D:\Java\nginx-1.24.0 下面，
GET http://dbapi.localhost/sa-frame/login/style.css net::ERR_ABORTED 404 (Not Found)
dbapi.localhost/:10     GET http://dbapi.localhost/static/sa.css net::ERR_ABORTED 404 (Not Found)
dbapi.localhost/:63     GET http://dbapi.localhost/sa-frame/login/particles.min.js net::ERR_ABORTED 404 (Not Found)
dbapi.localhost/:64     GET http://dbapi.localhost/sa-frame/login/app.js net::ERR_ABORTED 404 (Not Found)
dbapi.localhost/:67     GET http://dbapi.localhost/static/sa.js net::ERR_ABORTED 404 (Not Found)
dbapi.localhost/:68     GET http://dbapi.localhost/static/dbapi.js net::ERR_ABORTED 404 (Not Found)
dbapi.localhost/:43     GET http://dbapi.localhost/sa-frame/login/name.png 404 (Not Found)
dbapi.localhost/:50     GET http://dbapi.localhost/sa-frame/login/password.png 404 (Not Found)
dbapi.localhost/:63     GET http://dbapi.localhost/sa-frame/login/bg.jpg 404 (Not Found)
（索引）:123 本页面参考于jq22，原作者：http://www.jq22.com/jquery-info20074
admin-logo.png:1     GET http://dbapi.localhost/sa-frame/admin-logo.png 404 (Not Found)



任务：使用最强大的智能体设计一个，构建一个长时运行智能体的有效框架，使claude可以长时间运行，拆分任务，并执行任务，确保任务成功完成，继续执行任务等等，具体参考下面的这篇文档。
D:\workspace_github\jun_api_service\Anthropic长时运行智能体的有效框架案例及其代码解析.md
根据当前的文档及述求输出执行方案及代码脚本及交付件，并输出文档到输出目录 D:\workspace_github\jun_api_service\jun_dbapi\dbapi-ui\docs\Anthropic长时运行智能体的有效框架.md 中。
当前构建的框架需要能在其他项目正常的运行，支持不同的项目，主要支持以java为主的项目，包括前后端，其他的也可以一起考虑。

---

### 2026-04-02 长时运行智能体框架 v2

**新增文件（long-running-agent/）：**
- `main.py` — 入口 + 主编排循环（Planner → Worker → Evaluator）
- `config.py` — 配置加载（Config 类）
- `task_manager.py` — 任务生命周期管理（状态机、依赖、重试、v1迁移）
- `agent_runner.py` — Claude CLI 调用封装（planner/worker/evaluator）
- `logger.py` — JSONL 结构化日志
- `prompt_renderer.py` — 提示词模板渲染
- `prompts/planner.md` — 规划器提示词
- `prompts/worker.md` — 执行器提示词
- `prompts/evaluator.md` — 评估器提示词（全新）
- `config.json` — 更新为 v2 格式
- `README.md` — 框架文档

**架构：** 基于 Anthropic 三智能体架构（Planner + Worker + Evaluator），Python 编排器通过 `claude -p` CLI 调用各智能体，支持任务拆分、执行、独立验证、重试跳过、中断恢复。