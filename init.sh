#!/bin/bash
echo "=== my-project (jun_dbapi Bug修复) 环境检查 ==="

# 检查 Java
if command -v java &> /dev/null; then
    echo "[OK] Java: $(java -version 2>&1 | head -1)"
else
    echo "[FAIL] Java 未安装"
    exit 1
fi

# 检查 Maven
if command -v mvn &> /dev/null; then
    echo "[OK] Maven: $(mvn -version 2>&1 | head -1)"
else
    echo "[FAIL] Maven 未安装"
    exit 1
fi

# 编译检查
echo ""
echo "--- 编译检查 ---"
cd jun_dbapi && mvn clean compile -q
if [ $? -eq 0 ]; then
    echo "[OK] jun_dbapi 编译成功"
else
    echo "[FAIL] jun_dbapi 编译失败"
    exit 1
fi

echo ""
echo "=== 检查完成 ==="
