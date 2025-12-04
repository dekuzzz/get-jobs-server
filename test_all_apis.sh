#!/bin/bash

# 测试所有接口的脚本
BASE_URL="http://localhost:8080"

echo "=================================="
echo "测试所有接口"
echo "=================================="
echo ""

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 测试函数
test_api() {
    local name=$1
    local method=$2
    local url=$3
    local data=$4
    local headers=$5
    
    echo -e "${YELLOW}测试: $name${NC}"
    echo "请求: $method $url"
    
    if [ -n "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method "$url" \
            -H "Content-Type: application/json" \
            $headers \
            -d "$data")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$url" \
            -H "Content-Type: application/json" \
            $headers)
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" -ge 200 ] && [ "$http_code" -lt 300 ]; then
        echo -e "${GREEN}✓ 成功 (HTTP $http_code)${NC}"
        echo "响应: $body" | jq . 2>/dev/null || echo "响应: $body"
    else
        echo -e "${RED}✗ 失败 (HTTP $http_code)${NC}"
        echo "响应: $body"
    fi
    echo ""
}

# 1. 发送验证码
test_api "1. 发送验证码" "POST" "$BASE_URL/api/auth/user/send-verification-code" \
    '{"email": "test@example.com"}'

# 2. 获取验证码（从数据库）
VERIFY_CODE=$(docker exec -i getjob-postgres psql -U postgres -d postgres -t -c \
    "SELECT code FROM verification_codes WHERE email = 'test@example.com' AND is_used = false ORDER BY created_at DESC LIMIT 1;" | tr -d ' \n')

if [ -z "$VERIFY_CODE" ]; then
    echo -e "${RED}✗ 无法获取验证码${NC}"
    exit 1
fi

echo "使用验证码: $VERIFY_CODE"
echo ""

# 3. 验证验证码
VERIFY_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/user/verify-code" \
    -H "Content-Type: application/json" \
    -d "{\"email\": \"test@example.com\", \"code\": \"$VERIFY_CODE\"}")

VERIFY_TOKEN=$(echo "$VERIFY_RESPONSE" | jq -r '.data.verificationToken // empty')

if [ -z "$VERIFY_TOKEN" ] || [ "$VERIFY_TOKEN" = "null" ]; then
    echo -e "${RED}✗ 验证码验证失败${NC}"
    echo "$VERIFY_RESPONSE" | jq .
    exit 1
fi

echo -e "${GREEN}✓ 验证码验证成功${NC}"
echo "Verification Token: ${VERIFY_TOKEN:0:30}..."
echo ""

# 4. 注册用户（求职者）
test_api "4. 注册用户（求职者）" "POST" "$BASE_URL/api/auth/register" \
    "{\"email\": \"test@example.com\", \"password\": \"password123\", \"userType\": \"job_seeker\", \"name\": \"测试用户\", \"verificationToken\": \"$VERIFY_TOKEN\"}"

# 5. 登录
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{"email": "test@example.com", "password": "password123"}')

TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.accessToken // empty')
ROLE=$(echo "$LOGIN_RESPONSE" | jq -r '.data.userInfo.role // empty')

if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
    echo -e "${RED}✗ 登录失败${NC}"
    echo "$LOGIN_RESPONSE" | jq .
    exit 1
fi

echo -e "${GREEN}✓ 登录成功${NC}"
echo "Token: ${TOKEN:0:50}..."
echo "Role: $ROLE"
echo ""

# 6. 获取用户信息（使用token）
echo -e "${YELLOW}测试: 6. 获取用户信息${NC}"
echo "请求: GET $BASE_URL/api/user/profile"
RESPONSE=$(curl -s -X GET "$BASE_URL/api/user/profile" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN")
HTTP_CODE=$(echo "$RESPONSE" | jq -r '.code // 401')
if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ 成功${NC}"
    echo "$RESPONSE" | jq .
else
    echo -e "${RED}✗ 失败 (Code: $HTTP_CODE)${NC}"
    echo "$RESPONSE" | jq .
fi
echo ""

# 7. 切换角色
echo -e "${YELLOW}测试: 7. 切换角色${NC}"
echo "请求: POST $BASE_URL/api/switch-role"
RESPONSE=$(curl -s -X POST "$BASE_URL/api/switch-role" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN" \
    -d '{"targetRole": "recruiters"}')
HTTP_CODE=$(echo "$RESPONSE" | jq -r '.code // 401')
if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ 成功${NC}"
    echo "$RESPONSE" | jq .
else
    echo -e "${RED}✗ 失败 (Code: $HTTP_CODE)${NC}"
    echo "$RESPONSE" | jq .
fi
echo ""

# 8. 再次获取用户信息（验证切换是否成功）
echo -e "${YELLOW}测试: 8. 再次获取用户信息（验证切换）${NC}"
echo "请求: GET $BASE_URL/api/user/profile"
RESPONSE=$(curl -s -X GET "$BASE_URL/api/user/profile" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN")
HTTP_CODE=$(echo "$RESPONSE" | jq -r '.code // 401')
if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ 成功${NC}"
    echo "$RESPONSE" | jq .
    NEW_ROLE=$(echo "$RESPONSE" | jq -r '.data.role')
    if [ "$NEW_ROLE" = "recruiters" ]; then
        echo -e "${GREEN}✓ 角色切换成功：$NEW_ROLE${NC}"
    else
        echo -e "${RED}✗ 角色切换失败，当前角色：$NEW_ROLE${NC}"
    fi
else
    echo -e "${RED}✗ 失败 (Code: $HTTP_CODE)${NC}"
    echo "$RESPONSE" | jq .
fi
echo ""

# 9. 获取职位列表（不需要认证）
test_api "9. 获取职位列表" "GET" "$BASE_URL/api/job" ""

# 10. 获取人才列表（不需要认证，需要language参数）
test_api "10. 获取人才列表" "GET" "$BASE_URL/api/talents?language=zh" ""

echo "=================================="
echo -e "${GREEN}所有接口测试完成！${NC}"
echo "=================================="

