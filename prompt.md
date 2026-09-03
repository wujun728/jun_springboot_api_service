
我想构建一个长时运行智能体的有效框架，请给出一个完整的代码示例，下面是我的需求文档跟目前实现的功能代码。
根据下面的文档及代码，请给出一个完整的代码实现，优化并实现一个长时运行智能体的有效框架。
并生成一个对应的文档，描述这个框架的实现原理，以及如何使用这个框架。Anthropic长时运行智能体操作手册.md
D:\workspace_github_v2\20平台能力层\jun_api_service\Anthropic长时运行智能体的有效框架案例及其代码解析.md
D:\workspace_github_v2\20平台能力层\jun_api_service\long-running-agent

执行流程：用户输入一个大的任务，智能体根据任务进行拆分，并生成多个子任务，然后进行执行，并逐个返回结果，执行成果的任务标记为成功，失败的任务标记为失败。
    同时需要验证每个任务的执行成果，并返回。所有的执行步骤都保存在执行日志中，方便后面检阅及追踪。一直到所有的子任务都执行完毕，或者失败，或者超时，
    或者用户取消，或者用户要求结束。
用户只需要输入一个任务，然后等待结果，结果会返回给用户。中断执行后，用户可以重新开始执行。




● Bash(cd "D:\workspace_github_v2\20平台能力层\jun_api_service" && rm -f task_list.json claude-progress.txt execution_log.jsonl && rm -rf logs/)
⎿  Done

 cd "D:\workspace_github_v2\20平台能力层\jun_api_service" && python long-running-agent/main.py --task "生成项目描述的md文档" --max-sessions 2 2>&1)    