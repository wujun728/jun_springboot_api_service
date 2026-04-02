@echo off
chcp 65001 >nul 2>&1
setlocal enabledelayedexpansion

REM ==============================================================================
REM  长时运行智能体框架 - 主控制循环 (Windows 版本)
REM
REM  用法:
REM    run.bat                          使用默认 config.json
REM    run.bat --config my.json         指定配置文件
REM    run.bat --resume                 跳过初始化
REM    run.bat --max-sessions 20        限制最大会话数
REM ==============================================================================

set SCRIPT_DIR=%~dp0
set CONFIG_FILE=%SCRIPT_DIR%config.json
set RESUME=false
set MAX_SESSIONS=50
set SESSION_COUNT=0

REM ---------- 参数解析 ----------
:parse_args
if "%~1"=="" goto args_done
if "%~1"=="--config"       ( set CONFIG_FILE=%~2& shift& shift& goto parse_args )
if "%~1"=="--resume"       ( set RESUME=true& shift& goto parse_args )
if "%~1"=="--max-sessions" ( set MAX_SESSIONS=%~2& shift& shift& goto parse_args )
if "%~1"=="-h"             ( goto show_help )
if "%~1"=="--help"         ( goto show_help )
echo 未知参数: %~1
exit /b 1

:show_help
echo 用法: %~nx0 [--config ^<file^>] [--resume] [--max-sessions ^<n^>]
exit /b 0

:args_done

REM ---------- 工具函数 ----------
if not exist "%CONFIG_FILE%" (
    echo ERROR: 配置文件不存在: %CONFIG_FILE%
    exit /b 1
)

REM 使用 python 读取 config.json
for /f "delims=" %%i in ('python -c "import json; d=json.load(open(r'%CONFIG_FILE%',encoding='utf-8')); print(d['project_name'])"') do set PROJECT_NAME=%%i
for /f "delims=" %%i in ('python -c "import json; d=json.load(open(r'%CONFIG_FILE%',encoding='utf-8')); print(d['work_directory'])"') do set WORK_DIR=%%i
for /f "delims=" %%i in ('python -c "import json; d=json.load(open(r'%CONFIG_FILE%',encoding='utf-8')); print(d['paths']['feature_list'])"') do set FEATURE_LIST=%%i
for /f "delims=" %%i in ('python -c "import json; d=json.load(open(r'%CONFIG_FILE%',encoding='utf-8')); print(d['paths']['progress_file'])"') do set PROGRESS_FILE=%%i
for /f "delims=" %%i in ('python -c "import json; d=json.load(open(r'%CONFIG_FILE%',encoding='utf-8')); print(d['agent']['model'])"') do set MODEL=%%i
for /f "delims=" %%i in ('python -c "import json; d=json.load(open(r'%CONFIG_FILE%',encoding='utf-8')); print(d['agent']['max_turns'])"') do set MAX_TURNS=%%i
for /f "delims=" %%i in ('python -c "import json; d=json.load(open(r'%CONFIG_FILE%',encoding='utf-8')); print(d['agent']['allowed_tools'])"') do set ALLOWED_TOOLS=%%i

if "%WORK_DIR%"=="." set WORK_DIR=%CD%

echo ==========================================
echo  长时运行智能体框架
echo  项目: %PROJECT_NAME%
echo  工作目录: %WORK_DIR%
echo  模型: %MODEL%
echo  最大会话数: %MAX_SESSIONS%
echo ==========================================

REM ---------- 阶段一: 初始化 ----------
if "%RESUME%"=="true" goto check_feature_list
if exist "%WORK_DIR%\%FEATURE_LIST%" goto skip_init

echo --- 阶段一: 运行初始化智能体 ---

REM 使用 python 生成初始化提示词
python "%SCRIPT_DIR%utils\render_prompt.py" "%SCRIPT_DIR%prompts\init-agent.md" "%CONFIG_FILE%" > "%TEMP%\init_prompt.txt"

echo 启动初始化智能体...
cd /d "%WORK_DIR%"

claude -p "@%TEMP%\init_prompt.txt" --model %MODEL% --max-turns %MAX_TURNS% --allowedTools "%ALLOWED_TOOLS%" 2>&1 | tee "%WORK_DIR%\init-agent.log"

echo 初始化智能体完成。
set SESSION_COUNT=1

if not exist "%WORK_DIR%\%FEATURE_LIST%" (
    echo ERROR: 初始化失败, %FEATURE_LIST% 未生成
    exit /b 1
)
goto start_worker

:check_feature_list
if not exist "%WORK_DIR%\%FEATURE_LIST%" (
    echo ERROR: 使用 --resume 但 %FEATURE_LIST% 不存在
    exit /b 1
)

:skip_init
echo 检测到已有 %FEATURE_LIST%，跳过初始化。

REM ---------- 阶段二: 工作循环 ----------
:start_worker
echo.
echo --- 阶段二: 开始工作循环 ---

:worker_loop
set /a SESSION_COUNT+=1

REM 检查是否所有功能已完成
for /f "delims=" %%i in ('python -c "import json; d=json.load(open(r'%WORK_DIR%\%FEATURE_LIST%',encoding='utf-8')); print(len([x for x in d if not x.get('passes',False)]))"') do set PENDING=%%i

if "%PENDING%"=="0" (
    echo 所有功能已完成！总会话数: %SESSION_COUNT%
    goto done
)

if %SESSION_COUNT% gtr %MAX_SESSIONS% (
    echo 达到最大会话数限制 (%MAX_SESSIONS%)。停止。
    echo 仍有 %PENDING% 个功能未完成。
    goto done
)

echo.
echo ====== 工作会话 #%SESSION_COUNT% (剩余 %PENDING% 个功能) ======

REM 生成工作提示词
python "%SCRIPT_DIR%utils\render_prompt.py" "%SCRIPT_DIR%prompts\worker-agent.md" "%CONFIG_FILE%" --session %SESSION_COUNT% > "%TEMP%\worker_prompt.txt"

cd /d "%WORK_DIR%"

claude -p "@%TEMP%\worker_prompt.txt" --model %MODEL% --max-turns %MAX_TURNS% --allowedTools "%ALLOWED_TOOLS%" 2>&1 | tee "%WORK_DIR%\worker-session-%SESSION_COUNT%.log"

echo 工作会话 #%SESSION_COUNT% 完成。

REM 会话间暂停
timeout /t 5 /nobreak >nul

goto worker_loop

:done
echo.
echo ==========================================
echo  智能体运行结束
echo  总会话数: %SESSION_COUNT%
echo  未完成功能: %PENDING%
echo ==========================================

endlocal
