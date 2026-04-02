#!/bin/bash
echo "=== my-project 环境检查 ==="

# 检查 Java
echo -n "Java: "
if command -v java &>/dev/null; then
    java -version 2>&1 | head -1
else
    echo "未安装"
    exit 1
fi

# 检查 Maven
echo -n "Maven: "
if command -v mvn &>/dev/null; then
    mvn -version 2>&1 | head -1
else
    echo "未安装"
    exit 1
fi

# 检查 Node.js
echo -n "Node.js: "
if command -v node &>/dev/null; then
    node -v
else
    echo "未安装（前端构建需要）"
fi

# 检查 npm
echo -n "npm: "
if command -v npm &>/dev/null; then
    npm -v
else
    echo "未安装（前端构建需要）"
fi

# 检查项目文件
echo ""
echo "--- 项目文件检查 ---"
[ -f "pom.xml" ] && echo "✓ pom.xml 存在" || echo "✗ pom.xml 缺失"
[ -f "task_list.json" ] && echo "✓ task_list.json 存在" || echo "✗ task_list.json 缺失"
[ -d "jun_dbapi" ] && echo "✓ jun_dbapi/ 目录存在" || echo "✗ jun_dbapi/ 缺失"
[ -d "dbapi" ] && echo "✓ dbapi/ 目录存在" || echo "✗ dbapi/ 缺失"
[ -d "long-running-agent" ] && echo "✓ long-running-agent/ 目录存在" || echo "✗ long-running-agent/ 缺失"

# 检查文档输出目录
echo ""
echo "--- 文档输出目录 ---"
if [ -d "docs/project-description" ]; then
    echo "✓ docs/project-description/ 已存在"
    ls docs/project-description/
else
    echo "○ docs/project-description/ 待创建"
fi

echo ""
echo "=== 检查完成 ==="
