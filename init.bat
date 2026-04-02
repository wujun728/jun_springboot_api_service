@echo off
echo === my-project (jun_dbapi Bug修复) 环境检查 ===

:: 检查 Java
java -version >nul 2>&1
if %errorlevel%==0 (
    echo [OK] Java 已安装
) else (
    echo [FAIL] Java 未安装
    exit /b 1
)

:: 检查 Maven
mvn -version >nul 2>&1
if %errorlevel%==0 (
    echo [OK] Maven 已安装
) else (
    echo [FAIL] Maven 未安装
    exit /b 1
)

:: 编译检查
echo.
echo --- 编译检查 ---
cd jun_dbapi && mvn clean compile -q
if %errorlevel%==0 (
    echo [OK] jun_dbapi 编译成功
) else (
    echo [FAIL] jun_dbapi 编译失败
    exit /b 1
)

echo.
echo === 检查完成 ===
