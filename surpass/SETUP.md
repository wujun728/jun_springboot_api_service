# Surpass 部署文档

本文档介绍 Surpass API 权限开放平台的完整部署流程。

## 目录

- [环境要求](#环境要求)
- [快速开始](#快速开始)
- [数据库准备](#数据库准备)
- [后端部署](#后端部署)
- [前端部署](#前端部署)
- [Docker 部署](#docker-部署)
- [配置说明](#配置说明)
- [启动验证](#启动验证)
- [常见问题](#常见问题)
- [技术支持](#技术支持)

---

## 环境要求

### 必需环境

| 环境      | 版本     | 说明                                            |
|---------|--------|-----------------------------------------------|
| JDK     | 17+    | 推荐使用 [Eclipse Temurin](https://adoptium.net/) 或 Oracle JDK |
| MySQL   | 8.4.2+ | 或其他兼容的数据库（PostgreSQL、达梦等）                     |
| Node.js | 16+    | 前端构建所需，建议使用 LTS 版本                         |
| Gradle  | 8.0+   | 后端构建工具（项目已包含 Gradle Wrapper，无需单独安装）        |

### 版本验证

安装完成后，执行以下命令验证环境：

```bash
# 验证 Java 版本
java -version

# 验证 Node.js 版本
node -v

# 验证 npm 版本
npm -v
```

### 可选环境

| 环境     | 版本   | 说明      |
|--------|------|---------|
| Redis  | 6.0+ | 缓存支持，可选 |
| Docker | 最新   | 容器化部署所需 |

---

## 数据库准备

### 1. 创建数据库

使用 MySQL 命令行工具或其他客户端工具（如 Navicat、DBeaver）连接 MySQL，执行以下 SQL：

```sql
-- 创建数据库（使用 utf8mb4 字符集）
CREATE DATABASE IF NOT EXISTS surpass DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建数据库用户（生产环境请修改密码）
CREATE USER 'surpass'@'%' IDENTIFIED BY 'Surpass321!';

-- 授权用户访问数据库
GRANT ALL PRIVILEGES ON surpass.* TO 'surpass'@'%';

-- 刷新权限
FLUSH PRIVILEGES;

-- 可选：限制用户只能从本地访问（更安全）
-- CREATE USER 'surpass'@'localhost' IDENTIFIED BY 'Surpass321!';
-- GRANT ALL PRIVILEGES ON surpass.* TO 'surpass'@'localhost';
```

**注意**：
- 生产环境请务必修改默认密码 `Surpass321!`
- 如果 MySQL 服务器在远程主机，需要确保防火墙允许 3306 端口访问

### 2. 导入表结构

**方式一：使用 MySQL 命令行工具**

```bash
# Windows
mysql -u surpass -p Surpass321! surpass < db\1.0.0\schema.sql

# Linux/Mac
mysql -u surpass -p Surpass321! surpass < db/1.0.0/schema.sql
```

**方式二：使用 MySQL 客户端工具**

在 MySQL Workbench、Navicat、DBeaver 等工具中：
1. 连接到 MySQL 数据库
2. 选择 `surpass` 数据库
3. 打开并执行 `db/1.0.0/schema.sql` 文件内容

### 3. 导入初始化数据

**方式一：使用 MySQL 命令行工具**

```bash
# Windows
mysql -u surpass -p Surpass321! surpass < db\1.0.0\data.sql

# Linux/Mac
mysql -u surpass -p Surpass321! surpass < db/1.0.0/data.sql
```

**方式二：使用 MySQL 客户端工具**

在 MySQL Workbench、Navicat、DBeaver 等工具中：
1. 连接到 MySQL 数据库
2. 选择 `surpass` 数据库
3. 打开并执行 `db/1.0.0/data.sql` 文件内容

### 4. 验证数据库导入

执行以下 SQL 验证数据是否正确导入：

```sql
-- 查看表是否创建成功
USE surpass;
SHOW TABLES;

-- 查看预置用户
SELECT id, username FROM auth_user;
```

预期结果应显示以下表：
- `auth_resource`
- `auth_role`
- `auth_role_resource_bind`
- `auth_user`
- `auth_user_role_bind`
- `auth_account_log`
- `auth_operation_log`

### 默认账户

系统预置了以下测试账户：

| 用户名   | 密码    | 角色  |
|-------|-------|-----|
| admin | admin | 管理员 |

---

## 快速开始

**熟悉环境的用户可以按以下步骤快速部署**：

```bash
# 1. 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS surpass DEFAULT CHARACTER SET utf8mb4;"
mysql -u root -p -e "CREATE USER 'surpass'@'%' IDENTIFIED BY 'Surpass321!'; GRANT ALL ON surpass.* TO 'surpass'@'%';"

# 2. 导入数据（Windows 使用反斜杠）
mysql -u surpass -p Surpass321! surpass < db/1.0.0/schema.sql
mysql -u surpass -p Surpass321! surpass < db/1.0.0/data.sql

# 3. 构建后端
cd surpass
gradlew.bat build -x test  # Windows
# ./gradlew build -x test  # Linux/Mac

# 4. 运行后端（管理后台）
java -jar build/libs/surpass-admin-boot-2.0.0-ga.jar

# 5. 运行前端（新终端窗口）
cd ../surpass-ui
npm install
npm run dev

# 6. 访问
# 前端开发服务器：http://localhost:3155
# 管理后台：http://localhost:2154/surpass
# 登录账户：admin / admin
```

---

## 后端部署

### 1. 进入后端目录

```bash
# Windows
cd surpass

# Linux/Mac
cd surpass
```

**注意**：后端代码位于项目根目录的 `surpass` 子目录中。

### 2. 配置数据库连接

编辑 `surpass/surpass-web/src/main/resources/application-admin.properties` 文件，修改数据库配置：

```properties
# MySQL 配置
# 方式一：直接修改配置（开发环境）
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/surpass?allowPublicKeyRetrieval=true&autoReconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8
spring.datasource.username=surpass
spring.datasource.password=Surpass321!

# 方式二：使用环境变量（推荐，生产环境）
# spring.datasource.url=jdbc:mysql://${DATABASE_HOST:localhost}:${DATABASE_PORT:3306}/${DATABASE_NAME:surpass}?allowPublicKeyRetrieval=true&autoReconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8
# spring.datasource.username=${DATABASE_USER:surpass}
# spring.datasource.password=${DATABASE_PWD:Surpass321!}
```

**重要说明**：
- 默认配置中 `DATABASE_HOST` 的默认值是 `192.168.31.233`，请根据实际情况修改为 `localhost` 或你的数据库地址
- 默认端口是 `2155`（API 服务）和 `2154`（管理后台），可通过环境变量修改

### 2. 配置数据库连接

编辑 `surpass-web/src/main/resources/application-admin.properties` 文件，修改数据库配置：

```properties
# MySQL 配置
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://${DATABASE_HOST:localhost}:${DATABASE_PORT:3306}/${DATABASE_NAME:surpass}?allowPublicKeyRetrieval=true&autoReconnect=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8
spring.datasource.username=${DATABASE_USER:surpass}
spring.datasource.password=${DATABASE_PWD:Surpass321!}
```

也可以使用环境变量：

```bash
export DATABASE_HOST=localhost
export DATABASE_PORT=3306
export DATABASE_NAME=surpass
export DATABASE_USER=surpass
export DATABASE_PWD=Surpass321!
```

### 3. 设置环境变量（可选）

如果需要使用环境变量配置，可在构建前设置：

**Windows（CMD）**：
```cmd
set DATABASE_HOST=localhost
set DATABASE_PORT=3306
set DATABASE_NAME=surpass
set DATABASE_USER=surpass
set DATABASE_PWD=Surpass321!
set SERVER_PORT=2154
```

**Windows（PowerShell）**：
```powershell
$env:DATABASE_HOST="localhost"
$env:DATABASE_PORT="3306"
$env:DATABASE_NAME="surpass"
$env:DATABASE_USER="surpass"
$env:DATABASE_PWD="Surpass321!"
$env:SERVER_PORT="2154"
```

**Linux/Mac**：
```bash
export DATABASE_HOST=localhost
export DATABASE_PORT=3306
export DATABASE_NAME=surpass
export DATABASE_USER=surpass
export DATABASE_PWD=Surpass321!
export SERVER_PORT=2154
```

### 4. 构建项目

使用 Gradle Wrapper 构建（推荐）：

**Windows**：
```cmd
cd surpass
gradlew.bat build -x test
```

**Linux/Mac**：
```bash
cd surpass
chmod +x gradlew
./gradlew build -x test
```

**说明**：
- `-x test` 参数用于跳过测试（如果测试失败会影响构建）
- 首次构建时 Gradle 会下载依赖，可能需要较长时间
- 构建成功后会在 `surpass/build/` 目录生成 JAR 文件

### 5. 运行应用

构建完成后，运行生成的 JAR 文件：

**方式一：直接运行（管理后台）**
```bash
java -jar surpass/build/libs/surpass-admin-boot-2.0.0-ga.jar
```

**方式二：指定环境变量运行**
```bash
# Windows
set DATABASE_HOST=localhost & java -jar surpass/build/libs/surpass-admin-boot-2.0.0-ga.jar

# Linux/Mac
DATABASE_HOST=localhost java -jar surpass/build/libs/surpass-admin-boot-2.0.0-ga.jar
```

**方式三：运行 API 服务**
```bash
java -jar surpass/build/libs/surpass-boot-2.0.0-ga.jar
```

### 6. 服务端口说明

Surpass 包含两个独立的服务：

| 服务 | 默认端口 | 上下文路径 | 访问地址 | 说明 |
|-----|--------|-----------|---------|------|
| 管理后台 | 2154 | /surpass | http://localhost:2154/surpass | 后台管理系统 |
| API 服务 | 2155 | /surpass-api | http://localhost:2155/surpass-api | REST API 服务 |
| Swagger 文档 | 2155 | /surpass-api/swagger-ui.html | http://localhost:2155/surpass-api/swagger-ui.html | API 文档 |

**注意**：
- 管理后台（surpass-admin-boot）使用 `application-admin.properties` 配置
- API 服务（surpass-boot）使用 `application-surpass.properties` 配置
- 两个服务可以独立部署和运行

---

## 前端部署

### 1. 进入前端目录

```bash
cd surpass-ui
```

### 2. 安装依赖

```bash
npm install
```

**如果下载缓慢**，可使用淘宝镜像加速：

```bash
# 设置淘宝镜像
npm config set registry https://registry.npmmirror.com

# 清除缓存并重新安装
npm cache clean --force
npm install
```

**或者使用 nrm 管理镜像源**：
```bash
# 安装 nrm
npm install -g nrm

# 查看可用镜像源
nrm ls

# 切换到淘宝镜像
nrm use taobao

# 安装依赖
npm install
```

### 3. 配置前端 API 代理

编辑 `surpass-ui/vite.config.ts` 文件，确认 API 代理配置：

```typescript
server: {
    port: 3155,  // 前端开发服务器端口
    proxy: {
        '/surpass-api': {
            target: 'http://localhost:2155',  // 后端 API 服务地址
            changeOrigin: true,
            rewrite: (p) => p
        }
    }
}
```

**注意**：
- 前端开发端口是 `3155`（不是 3154）
- 代理目标地址是后端 API 服务的地址（默认 `http://localhost:2155`）

### 4. 开发模式

启动开发服务器：

```bash
npm run dev
```

启动成功后，访问：**http://localhost:3155**

**说明**：
- 开发模式下会自动代理 API 请求到后端服务
- 确保后端 API 服务（2155 端口）已启动
- 前端热更新，修改代码后自动刷新

### 5. 生产构建

```bash
npm run build
```

构建产物位于 `dist/` 目录，可部署到 Nginx 或其他 Web 服务器。

### 6. Nginx 配置示例

创建 Nginx 配置文件（如 `/etc/nginx/conf.d/surpass.conf`）：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端静态文件
    root /path/to/surpass-ui/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    # 代理管理后台 API
    location /surpass/ {
        proxy_pass http://localhost:2154/surpass/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # 代理开放平台 API
    location /surpass-api/ {
        proxy_pass http://localhost:2155/surpass-api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

重新加载 Nginx 配置：
```bash
nginx -t && nginx -s reload
```

---

## Docker 部署

### 1. 构建后端镜像

**前提条件**：先在 `surpass` 目录下构建 JAR 文件

```bash
# 进入后端目录并构建
cd surpass
gradlew.bat build -x test  # Windows
# ./gradlew build -x test  # Linux/Mac
```

**方式一：构建管理后台镜像**

```bash
# 在项目根目录执行
docker build -t surpass:admin -f surpass/surpass-web/Dockerfile surpass
```

**方式二：构建 API 服务镜像**

```bash
# 在项目根目录执行
docker build -t surpass:api -f surpass/surpass-web-api/Dockerfile surpass
```

**方式三：手动指定 JAR 文件**

```bash
# 构建管理后台镜像（指定 JAR 文件）
docker build -t surpass:admin \
  --build-arg JAR_FILE=build/libs/surpass-admin-boot-2.0.0-ga.jar \
  -f surpass/surpass-web/Dockerfile \
  surpass
```

### 2. 运行容器

**运行管理后台**：
```bash
docker run -d \
  --name surpass-admin \
  -p 2154:2154 \
  -e DATABASE_HOST=host.docker.internal \
  -e DATABASE_PORT=3306 \
  -e DATABASE_NAME=surpass \
  -e DATABASE_USER=surpass \
  -e DATABASE_PWD=Surpass321! \
  surpass:latest
```

**运行 API 服务**：
```bash
docker run -d \
  --name surpass-api \
  -p 2155:2155 \
  -e DATABASE_HOST=host.docker.internal \
  -e DATABASE_PORT=3306 \
  -e DATABASE_NAME=surpass \
  -e DATABASE_USER=surpass \
  -e DATABASE_PWD=Surpass321! \
  surpass:latest
```

**注意**：
- `host.docker.internal` 用于访问宿主机上的数据库
- Linux 用户可能需要使用 `--network host` 或配置正确的网络

### 3. Docker Compose（完整部署）

在项目根目录创建 `docker-compose.yml`：

```yaml
version: '3.8'

services:
  # MySQL 数据库
  mysql:
    image: mysql:8.4
    container_name: surpass-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: surpass
      MYSQL_USER: surpass
      MYSQL_PASSWORD: Surpass321!
    volumes:
      - mysql_data:/var/lib/mysql
      - ./db/1.0.0/schema.sql:/docker-entrypoint-initdb.d/1-schema.sql
      - ./db/1.0.0/data.sql:/docker-entrypoint-initdb.d/2-data.sql
    ports:
      - "3306:3306"
    networks:
      - surpass-network
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 3

  # 管理后台服务
  surpass-admin:
    image: surpass:admin
    container_name: surpass-admin
    build:
      context: ./surpass
      dockerfile: surpass-web/Dockerfile
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      DATABASE_HOST: mysql
      DATABASE_PORT: 3306
      DATABASE_NAME: surpass
      DATABASE_USER: surpass
      DATABASE_PWD: Surpass321!
      SERVER_PORT: 2154
    ports:
      - "2154:2154"
    networks:
      - surpass-network

  # API 服务
  surpass-api:
    image: surpass:api
    container_name: surpass-api
    build:
      context: ./surpass
      dockerfile: surpass-web-api/Dockerfile
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      DATABASE_HOST: mysql
      DATABASE_PORT: 3306
      DATABASE_NAME: surpass
      DATABASE_USER: surpass
      DATABASE_PWD: Surpass321!
      SERVER_PORT: 2155
    ports:
      - "2155:2155"
    networks:
      - surpass-network

networks:
  surpass-network:
    driver: bridge

volumes:
  mysql_data:
```

**启动服务**：
```bash
# 构建并启动所有服务
docker-compose up -d --build

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

---

## 配置说明

### 环境变量列表

以下是 Surpass 支持的所有环境变量：

#### 数据库配置（必需）

| 变量名 | 默认值 | 说明 | 示例 |
|-------|--------|------|------|
| `DATABASE_HOST` | 192.168.31.233 | 数据库主机地址 | localhost |
| `DATABASE_PORT` | 3306 | 数据库端口 | 3306 |
| `DATABASE_NAME` | surpass | 数据库名称 | surpass |
| `DATABASE_USER` | surpass | 数据库用户名 | surpass |
| `DATABASE_PWD` | Surpass321! | 数据库密码 | YourPassword123! |

#### 服务配置（可选）

| 变量名 | 默认值 | 说明 | 示例 |
|-------|--------|------|------|
| `SERVER_PORT` | 2154/2155 | 服务端口（admin:2154, api:2155） | 2154 |
| `SERVER_PROFILES` | admin/surpass | Spring Profile（admin 或 surpass） | admin |
| `SERVER_CACHED` | 0 | 缓存模式（0=内存，2=Redis） | 0 |

#### Redis 配置（可选，使用 Redis 缓存时）

| 变量名 | 默认值 | 说明 | 示例 |
|-------|--------|------|------|
| `REDIS_HOST` | localhost | Redis 主机地址 | localhost |
| `REDIS_PORT` | 6379 | Redis 端口 | 6379 |
| `REDIS_PWD` | Hlh967484. | Redis 密码 | YourRedisPassword |

#### 登录配置（可选）

| 变量名 | 默认值 | 说明 | 示例 |
|-------|--------|------|------|
| `LOGIN_IPLOCATION` | true | 是否启用 IP 归属地 | true |
| `LOGIN_IPLOCATION_PROVIDER` | Ip2Region | IP 归属地提供商 | Ip2Region |

### 配置文件位置

| 服务 | 配置文件 | 说明 |
|-----|---------|------|
| 管理后台 | `surpass/surpass-web/src/main/resources/application-admin.properties` | 管理后台配置 |
| API 服务 | `surpass/surpass-web-api/src/main/resources/application-surpass.properties` | API 服务配置 |
| 通用配置 | `surpass/surpass-web/src/main/resources/application.properties` | Spring Boot 基础配置 |

### JWT 配置

JWT 密钥已在配置文件中预设，生产环境建议自定义：

```properties
# 访问令牌密钥（建议修改为自定义密钥）
surpass.auth.jwt.secret=<your-secret-key>

# 刷新令牌密钥（建议修改为自定义密钥）
surpass.auth.jwt.refresh.secret=<your-refresh-secret>
```

**生成随机密钥**：
```bash
# Linux/Mac
openssl rand -base64 64

# Windows PowerShell
[System.Web.Security.Membership]::GeneratePassword(64, 4)
```

---

## 启动验证

### 1. 检查服务状态

**检查进程**：
```bash
# Windows
tasklist | findstr java

# Linux/Mac
ps aux | grep java
```

**检查端口占用**：
```bash
# Windows
netstat -ano | findstr 2154
netstat -ano | findstr 2155

# Linux/Mac
netstat -tlnp | grep 2154
netstat -tlnp | grep 2155

# 或使用 lsof（Linux/Mac）
lsof -i :2154
lsof -i :2155
```

### 2. 查看应用日志

启动后，观察控制台输出，确保无 ERROR 级别错误。关键日志：

```
  ____            _                  
 / ___|    __ _  | |__     ___   _ __ 
 \___ \   / _` | | '_ \   / _ \ | '__|
  ___) | | (_| | | |_) | |  __/ | |   
 |____/   \__,_| |_.__/   \___| |_|   
                                      
Surpass v2.0.0

Tomcat started on port(s): 2154 (http) with context path '/surpass'
Started Surpass in X.XXX seconds
```

### 3. 访问测试

**浏览器访问**：
- 管理后台：http://localhost:2154/surpass
- API 服务：http://localhost:2155/surpass-api
- Swagger 文档：http://localhost:2155/surpass-api/swagger-ui.html

**使用 curl 测试 API**：
```bash
# 测试健康检查端点
curl http://localhost:2155/surpass-api/actuator/health

# 测试登录接口
curl -X POST http://localhost:2155/surpass-api/auth/token \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
```

### 4. 登录测试

使用默认账户登录管理后台：

| 用户名 | 密码  | 角色  |
|-------|------|------|
| admin | admin | 管理员 |
| user  | user  | 普通用户 |
| guest | guest | 访客   |

**推荐操作步骤**：
1. 使用 admin/admin 登录管理后台
2. 检查仪表盘数据是否正常显示
3. 进入系统管理 -> 用户管理，查看用户列表
4. 进入系统管理 -> 角色管理，查看角色列表

### 5. 数据库验证

连接数据库，验证数据是否正确：

```sql
-- 查看表数量
USE surpass;
SHOW TABLES;

-- 查看用户数据
SELECT id, username, status FROM auth_user;

-- 查看角色数据
SELECT id, name, code FROM auth_role;

-- 查看资源数据
SELECT id, name, uri FROM auth_resource;
```

### 6. 前端验证

如果前端已部署，访问：

- 开发模式：http://localhost:3155
- 生产模式：http://your-domain.com（通过 Nginx）

**验证项目**：
1. 页面能正常加载，无 404 错误
2. 能正常登录（无跨域错误）
3. 能查看数据列表（API 调用正常）

---

## 常见问题

### 1. Java 版本不匹配

**错误信息**：
```
This build must be run with Java version 17 or greater
```

**解决方案**：
```bash
# 查看当前 Java 版本
java -version

# 确保安装 JDK 17 或更高版本
# 下载地址：https://adoptium.net/

# 设置 JAVA_HOME 环境变量
# Windows
set JAVA_HOME=C:\IDE\jdk-17.0.9+9

# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

### 2. 数据库连接失败

**错误信息**：
```
Communications link failure
Access denied for user 'surpass'@'localhost'
```

**排查步骤**：
1. 检查 MySQL 服务是否启动
   ```bash
   # Windows
   net start MySQL
   
   # Linux
   systemctl status mysql
   ```

2. 检查数据库用户名密码是否正确
   ```sql
   -- 验证用户是否存在
   SELECT user, host FROM mysql.user WHERE user='surpass';
   ```

3. 检查数据库是否存在
   ```sql
   SHOW DATABASES LIKE 'surpass';
   ```

4. 检查防火墙设置
   ```bash
   # 开放 3306 端口（Linux）
   firewall-cmd --add-port=3306/tcp --permanent
   firewall-cmd --reload
   ```

5. 测试数据库连接
   ```bash
   mysql -u surpass -p -h localhost surpass
   ```

### 3. 端口被占用

**错误信息**：
```
Port 2154 was already in use
```

**解决方案**：

**方式一：停止占用端口的进程**
```bash
# Windows
netstat -ano | findstr :2154
taskkill /F /PID <PID>

# Linux/Mac
lsof -i :2154
kill -9 <PID>
```

**方式二：修改服务端口**
```bash
# 设置环境变量修改端口
export SERVER_PORT=2156

# 或直接修改配置文件
# surpass/surpass-web/src/main/resources/application-admin.properties
server.port=2156
```

### 4. 前端跨域问题

**现象**：前端能访问但 API 调用报 CORS 错误

**解决方案**：
1. 确认前端 `vite.config.ts` 中的代理配置正确
2. 确认后端服务已启动且端口正确
3. 检查代理目标地址（默认 `http://localhost:2155`）

**开发模式代理配置**（vite.config.ts）：
```typescript
server: {
    port: 3155,
    proxy: {
        '/surpass-api': {
            target: 'http://localhost:2155',
            changeOrigin: true
        }
    }
}
```

### 5. 构建失败（依赖下载慢）

**问题**：Gradle 依赖下载缓慢或失败

**解决方案**：
1. 配置阿里云 Maven 镜像

在 `surpass/build.gradle` 中已配置阿里云镜像，如果没有，可添加：
```gradle
repositories {
    maven { url = "https://maven.aliyun.com/repository/central"}
    maven { url = "https://maven.aliyun.com/repository/public"}
    mavenCentral()
}
```

2. 使用 Gradle 国内镜像

在 `surpass/gradle/wrapper/gradle-wrapper.properties` 中修改：
```properties
distributionUrl=https\://mirrors.cloud.tencent.com/gradle/gradle-8.0-bin.zip
```

### 6. 内存不足

**错误信息**：
```
Java heap space
OutOfMemoryError
```

**解决方案**：
```bash
# 调整 JVM 内存参数
java -Xms1024m -Xmx4096m -jar surpass/build/libs/surpass-admin-boot-2.0.0-ga.jar
```

或在 Docker 中：
```yaml
environment:
  JAVA_OPTS: "-Xms1024m -Xmx4096m"
```

### 7. 前端 npm install 失败

**问题**：npm install 时报错或卡住

**解决方案**：
```bash
# 1. 清除缓存
npm cache clean --force

# 2. 使用淘宝镜像
npm config set registry https://registry.npmmirror.com

# 3. 删除 node_modules 和 package-lock.json
rm -rf node_modules package-lock.json

# 4. 重新安装
npm install
```

### 8. 登录后提示"用户名或密码错误"

**可能原因**：
1. 数据库未正确导入初始化数据
2. 密码加密方式不匹配

**解决方案**：
1. 重新导入 `db/1.0.0/data.sql`
2. 验证数据库中的用户数据：
   ```sql
   SELECT id, username, password FROM auth_user WHERE username='admin';
   ```
   默认 admin 用户密码为明文 `admin`

### 9. Swagger 文档无法访问

**可能原因**：
1. API 服务未启动
2. 端口配置错误

**解决方案**：
1. 确认 API 服务（surpass-boot）已启动
2. 检查端口是否为 2155
3. 访问正确地址：http://localhost:2155/surpass-api/swagger-ui.html

### 10. Gradle 构建命令不识别

**Windows 系统**：
- 使用 `gradlew.bat` 而不是 `gradlew`
- 确保在 `surpass` 目录下执行

**Linux/Mac 系统**：
```bash
# 添加执行权限
chmod +x gradlew

# 执行构建
./gradlew build
```

---

## 技术支持

- 官方文档：http://surpass.maxkey.top
- GitHub：https://github.com/dromara/surpass
- Gitee：https://gitee.com/dromara/surpass
- 问题反馈：请在 GitHub/Gitee 提交 Issue

---
