#!/bin/sh
#
# Author : tang
# Date :2024-03-06
#
#############################################

module=$1

APP_HOME="${BASH_SOURCE-$0}"
APP_HOME="$(dirname "${APP_HOME}")"
APP_HOME="$(cd "${APP_HOME}"; pwd)"
APP_HOME="$(cd "$(dirname ${APP_HOME})"; pwd)"
APP_BIN_PATH=$APP_HOME/bin
APP_CONF_PATH=$APP_HOME/conf
APP_LIB_COMMON_PATH=$APP_HOME/lib/common
APP_LIB_EXECUTOR_PATH=$APP_HOME/lib/executor
APP_LIB_GATEWAY_PATH=$APP_HOME/lib/gateway
APP_LIB_MANAGER_PATH=$APP_HOME/lib/manager
APP_PID_FILE="${APP_HOME}/run/${module}.pid"
APP_RUN_LOG="${APP_HOME}/run/run_${module}.log"

echo "Begin start $module......"
echo "Base Directory:${APP_HOME}"

export APP_DRIVERS_PATH=$APP_HOME/drivers

# JVM参数可以在这里设置
JVMFLAGS="-server -Xms1024m -Xmx1024m -Xmn1024m -XX:+DisableExplicitGC -Djava.awt.headless=true -Dfile.encoding=UTF-8 "

if [ "$JAVA_HOME" != "" ]; then
  JAVA="$JAVA_HOME/bin/java"
else
  JAVA=java
fi

# 配置classpath和启动类
CLASSPATH=$APP_CONF_PATH
APP_MAIN_CLASS='com.gitee.sqlrest.manager.ManagerApplication'
if [ "$module" = "manager" ]; then
  CLASSPATH="$APP_CONF_PATH/manager:$APP_LIB_COMMON_PATH/*:$APP_HOME/lib/webmvc/*:$APP_HOME/lib/manager/*"
  APP_MAIN_CLASS='com.gitee.sqlrest.manager.ManagerApplication'
elif [ "$module" = "executor" ]; then
  CLASSPATH="$APP_CONF_PATH/executor:$APP_LIB_COMMON_PATH/*:$APP_HOME/lib/webmvc/*:$APP_HOME/lib/executor/*"
  APP_MAIN_CLASS='com.gitee.sqlrest.executor.ExecutorApplication'
elif [ "$module" = "gateway" ]; then
  CLASSPATH="$APP_CONF_PATH/gateway:$APP_LIB_COMMON_PATH/*:$APP_HOME/lib/webflux/*:$APP_HOME/lib/gateway/*"
  APP_MAIN_CLASS='ccom.gitee.sqlrest.gateway.GatewayApplication'
else
  echo "Error: No module named '$module' was found."
  exit 1
fi

# 执行命令
[ -d "${APP_HOME}/run" ] || mkdir -p "${APP_HOME}/run"
echo "d ${APP_HOME} && $JAVA -cp $CLASSPATH $JVMFLAGS $APP_MAIN_CLASS"
cd ${APP_HOME} && $JAVA -cp $CLASSPATH $JVMFLAGS $APP_MAIN_CLASS

echo "Finish start $module !"
