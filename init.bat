@echo off
echo === my-project 环境检查 ===

echo.
echo --- Java ---
java -version 2>&1 | findstr /i "version"
if errorlevel 1 echo Java 未安装

echo.
echo --- Maven ---
mvn -version 2>&1 | findstr /i "Apache Maven"
if errorlevel 1 echo Maven 未安装

echo.
echo --- Node.js ---
node -v 2>nul
if errorlevel 1 echo Node.js 未安装

echo.
echo --- npm ---
npm -v 2>nul
if errorlevel 1 echo npm 未安装

echo.
echo --- 项目文件检查 ---
if exist pom.xml (echo √ pom.xml 存在) else (echo × pom.xml 缺失)
if exist task_list.json (echo √ task_list.json 存在) else (echo × task_list.json 缺失)
if exist jun_dbapi (echo √ jun_dbapi\ 目录存在) else (echo × jun_dbapi\ 缺失)
if exist dbapi (echo √ dbapi\ 目录存在) else (echo × dbapi\ 缺失)
if exist long-running-agent (echo √ long-running-agent\ 目录存在) else (echo × long-running-agent\ 缺失)

echo.
echo --- 文档输出目录 ---
if exist docs\project-description (
    echo √ docs\project-description\ 已存在
    dir /b docs\project-description\
) else (
    echo ○ docs\project-description\ 待创建
)

echo.
echo === 检查完成 ===
pause
