#!/bin/bash
# =====================================================
# ruoyi-api 模块 curl 测试脚本
# 前提: RuoYi-MageApi-Amis 已启动 (http://localhost:8080)
#       数据库已导入 sql/ruoyi-api.sql
# =====================================================

BASE_URL="http://localhost:8080"
COOKIE_FILE="/tmp/ruoyi_cookie.txt"

echo "===== 1. 登录获取 Token ====="
curl -s -c $COOKIE_FILE -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123","code":"","uuid":""}' | python -m json.tool 2>/dev/null || echo "login response received"

echo ""
echo "===== 2. OpenAPI: 注册应用 ====="
curl -s -b $COOKIE_FILE -X POST "$BASE_URL/openapi/register" \
  -d "appKey=myapp&appSecret=mysecret&name=MyApp" | python -m json.tool 2>/dev/null

echo ""
echo "===== 3. OpenAPI: 调用执行 ====="
curl -s -X POST "$BASE_URL/openapi/execute" \
  -H "Content-Type: application/json" \
  -H "AppKey: myapp" \
  -H "AppSecret: mysecret" \
  -d '{"test":"hello"}' | python -m json.tool 2>/dev/null

echo ""
echo "===== 4. OpenAPI: 签名调用 ====="
TIMESTAMP=$(date +%s%3N)
SIGN=$(echo -n "${TIMESTAMP}test_body" | openssl dgst -sha256 -hmac "mysecret" -binary | base64)
curl -s -X POST "$BASE_URL/openapi/execute" \
  -H "Content-Type: application/json" \
  -H "AppKey: myapp" \
  -H "AppSecret: mysecret" \
  -H "Timestamp: $TIMESTAMP" \
  -H "Sign: $SIGN" \
  -d '{"test":"signed_call"}' | python -m json.tool 2>/dev/null

echo ""
echo "===== 5. 版本控制: 保存版本 ====="
curl -s -b $COOKIE_FILE -X POST "$BASE_URL/api/version/save" \
  -H "Content-Type: application/json" \
  -d '{"apiId":"my-api","apiPath":"/my/api","apiName":"My API","content":"version 1 content","operateType":"create","operateBy":"admin"}' | python -m json.tool 2>/dev/null

echo ""
echo "===== 6. 版本控制: 查询历史 ====="
curl -s -b $COOKIE_FILE "$BASE_URL/api/version/history/my-api" | python -m json.tool 2>/dev/null

echo ""
echo "===== 7. 版本控制: 回滚 ====="
curl -s -b $COOKIE_FILE -X POST "$BASE_URL/api/version/rollback/my-api/1" | python -m json.tool 2>/dev/null

echo ""
echo "===== 8. 聚合API: GET 请求 ====="
curl -s -b $COOKIE_FILE "$BASE_URL/reverse/demo/test" | python -m json.tool 2>/dev/null

echo ""
echo "===== 9. 聚合API: POST 请求 ====="
curl -s -b $COOKIE_FILE -X POST "$BASE_URL/reverse/demo/test" \
  -H "Content-Type: application/json" \
  -d '{}' | python -m json.tool 2>/dev/null

echo ""
echo "===== 10. MCP SSE 端点 ====="
curl -s -N -H "Accept: text/event-stream" "$BASE_URL/mcp/sse" --max-time 3 2>&1 | head -5

echo ""
echo "===== 11. MCP Message (Initialize) ====="
curl -s -X POST "$BASE_URL/mcp/message" \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","id":"1","method":"initialize","params":{"protocolVersion":"2024-11-05","capabilities":{},"clientInfo":"test"}}' | python -m json.tool 2>/dev/null

echo ""
echo "===== 12. 插件管理: 获取缓存插件列表 ====="
curl -s -b $COOKIE_FILE "$BASE_URL/plugin/list?type=cache" 2>/dev/null || echo "(plugin management endpoint may need additional config)"

echo ""
echo "===== 测试完成 ====="
echo "注意: 部分接口(如聚合API)需要数据库中有对应的配置数据才能返回正常结果。"
echo "请确保已执行: source sql/ruoyi-api.sql"