#!/usr/bin/env bash
#==============================================================================
# 长时运行智能体框架 - 主控制循环
#
# 用法:
#   ./run.sh                     # 使用默认 config.json
#   ./run.sh --config my.json    # 指定配置文件
#   ./run.sh --resume            # 跳过初始化，直接从 worker 开始
#   ./run.sh --max-sessions 20   # 限制最大会话数
#
# 核心原理:
#   1. 首次运行: 启动 Init Agent 创建 feature_list.json 等脚手架
#   2. 后续循环: 启动 Worker Agent 每次实现一个功能
#   3. 主循环持续直到所有功能标记为 passes:true 或达到最大会话数
#==============================================================================

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
CONFIG_FILE="${SCRIPT_DIR}/config.json"
RESUME=false
MAX_SESSIONS=50
SESSION_COUNT=0

#---------- 参数解析 ----------
while [[ $# -gt 0 ]]; do
    case $1 in
        --config)    CONFIG_FILE="$2";  shift 2 ;;
        --resume)    RESUME=true;       shift   ;;
        --max-sessions) MAX_SESSIONS="$2"; shift 2 ;;
        -h|--help)
            echo "用法: $0 [--config <file>] [--resume] [--max-sessions <n>]"
            exit 0 ;;
        *) echo "未知参数: $1"; exit 1 ;;
    esac
done

#---------- 工具函数 ----------
log()  { echo "[$(date '+%Y-%m-%d %H:%M:%S')] $*"; }
die()  { log "ERROR: $*" >&2; exit 1; }

# 从 config.json 读取字段 (需要 jq 或 python)
read_config() {
    local key="$1"
    if command -v jq &>/dev/null; then
        jq -r "$key" "$CONFIG_FILE"
    else
        python3 -c "import json,sys; d=json.load(open('$CONFIG_FILE','r',encoding='utf-8')); print(eval(\"d$(echo $key | sed 's/\\./'\\'']/['\\''/g' | sed \"s/^/['/;s/$/']/ \")\"))" 2>/dev/null \
        || python -c "import json; d=json.load(open('$CONFIG_FILE')); print(d$(echo $key | sed 's/\./\"][\"/g; s/^/[\"/; s/$/\"]/'))"
    fi
}

# 检查 feature_list.json 中剩余未完成的功能数量
count_pending_features() {
    local fl="$WORK_DIR/$FEATURE_LIST"
    if [[ ! -f "$fl" ]]; then
        echo "-1"
        return
    fi
    if command -v jq &>/dev/null; then
        jq '[.[] | select(.passes == false)] | length' "$fl"
    else
        python3 -c "
import json
with open('$fl','r',encoding='utf-8') as f:
    data = json.load(f)
print(len([x for x in data if not x.get('passes', False)]))"
    fi
}

# 检查是否所有功能都已完成
all_features_done() {
    local pending
    pending=$(count_pending_features)
    [[ "$pending" == "0" ]]
}

#---------- 读取配置 ----------
[[ -f "$CONFIG_FILE" ]] || die "配置文件不存在: $CONFIG_FILE"

PROJECT_NAME=$(read_config '.project_name')
WORK_DIR=$(read_config '.work_directory')
FEATURE_LIST=$(read_config '.paths.feature_list')
PROGRESS_FILE=$(read_config '.paths.progress_file')
MODEL=$(read_config '.agent.model')
MAX_TURNS=$(read_config '.agent.max_turns')
ALLOWED_TOOLS=$(read_config '.agent.allowed_tools')
CFG_MAX_SESSIONS=$(read_config '.agent.max_sessions')

# 配置覆盖命令行参数
[[ "$MAX_SESSIONS" == "50" ]] && MAX_SESSIONS="${CFG_MAX_SESSIONS:-50}"

# 解析为绝对路径
if [[ "$WORK_DIR" == "." ]]; then
    WORK_DIR="$(pwd)"
fi

log "=========================================="
log " 长时运行智能体框架"
log " 项目: $PROJECT_NAME"
log " 工作目录: $WORK_DIR"
log " 模型: $MODEL"
log " 最大会话数: $MAX_SESSIONS"
log "=========================================="

#---------- 阶段一: 初始化 ----------
if [[ "$RESUME" == false ]] && [[ ! -f "$WORK_DIR/$FEATURE_LIST" ]]; then
    log "--- 阶段一: 运行初始化智能体 ---"

    INIT_PROMPT=$(cat "$SCRIPT_DIR/prompts/init-agent.md")

    # 将配置注入提示词
    INIT_PROMPT="${INIT_PROMPT//\{\{PROJECT_NAME\}\}/$PROJECT_NAME}"
    INIT_PROMPT="${INIT_PROMPT//\{\{WORK_DIRECTORY\}\}/$WORK_DIR}"

    # 读取用户需求
    USER_REQ=$(read_config '.user_requirements')
    INIT_PROMPT="${INIT_PROMPT//\{\{USER_REQUIREMENTS\}\}/$USER_REQ}"

    # 读取项目类型相关配置
    PROJECT_TYPE=$(read_config '.project_type')
    INIT_PROMPT="${INIT_PROMPT//\{\{PROJECT_TYPE\}\}/$PROJECT_TYPE}"

    log "启动初始化智能体..."
    cd "$WORK_DIR"

    claude -p "$INIT_PROMPT" \
        --model "$MODEL" \
        --max-turns "$MAX_TURNS" \
        --allowedTools "$ALLOWED_TOOLS" \
        2>&1 | tee "${WORK_DIR}/init-agent.log"

    log "初始化智能体完成。"

    # 验证脚手架文件
    [[ -f "$WORK_DIR/$FEATURE_LIST" ]] || die "初始化失败: $FEATURE_LIST 未生成"
    [[ -f "$WORK_DIR/$PROGRESS_FILE" ]] || log "警告: $PROGRESS_FILE 未生成"

    SESSION_COUNT=1
    log "初始化完成。未完成功能数: $(count_pending_features)"
else
    if [[ -f "$WORK_DIR/$FEATURE_LIST" ]]; then
        log "检测到已有 $FEATURE_LIST，跳过初始化。"
        log "未完成功能数: $(count_pending_features)"
    else
        die "使用 --resume 但 $FEATURE_LIST 不存在"
    fi
fi

#---------- 阶段二: 工作循环 ----------
log ""
log "--- 阶段二: 开始工作循环 ---"

WORKER_PROMPT_TEMPLATE=$(cat "$SCRIPT_DIR/prompts/worker-agent.md")

while true; do
    SESSION_COUNT=$((SESSION_COUNT + 1))

    # 检查退出条件
    if all_features_done; then
        log "所有功能已完成！总会话数: $SESSION_COUNT"
        break
    fi

    if [[ $SESSION_COUNT -gt $MAX_SESSIONS ]]; then
        log "达到最大会话数限制 ($MAX_SESSIONS)。停止。"
        pending=$(count_pending_features)
        log "仍有 $pending 个功能未完成。"
        break
    fi

    pending=$(count_pending_features)
    log ""
    log "====== 工作会话 #$SESSION_COUNT (剩余 $pending 个功能) ======"

    # 注入变量到提示词
    WORKER_PROMPT="${WORKER_PROMPT_TEMPLATE//\{\{PROJECT_NAME\}\}/$PROJECT_NAME}"
    WORKER_PROMPT="${WORKER_PROMPT//\{\{WORK_DIRECTORY\}\}/$WORK_DIR}"
    WORKER_PROMPT="${WORKER_PROMPT//\{\{SESSION_NUMBER\}\}/$SESSION_COUNT}"

    cd "$WORK_DIR"

    # 启动工作智能体
    claude -p "$WORKER_PROMPT" \
        --model "$MODEL" \
        --max-turns "$MAX_TURNS" \
        --allowedTools "$ALLOWED_TOOLS" \
        2>&1 | tee "${WORK_DIR}/worker-session-${SESSION_COUNT}.log"

    log "工作会话 #$SESSION_COUNT 完成。"

    # 会话间暂停 (防止 API 限流)
    sleep 5
done

#---------- 结束 ----------
log ""
log "=========================================="
log " 智能体运行结束"
log " 总会话数: $SESSION_COUNT"
log " 未完成功能: $(count_pending_features)"
log "=========================================="
