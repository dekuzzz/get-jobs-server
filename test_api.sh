#!/bin/bash

# API接口测试脚本
# 使用方法: ./test_api.sh [base_url]
# 默认base_url: http://localhost:8080

BASE_URL=${1:-http://localhost:8080}
TEST_EMAIL="test_$(date +%s)@example.com"
TEST_PASSWORD="password123"
TEST_NAME="测试用户"

echo "=================================="
echo "API接口测试脚本"
echo "=================================="
echo "测试地址: $BASE_URL"
echo "测试邮箱: $TEST_EMAIL"
echo ""

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

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
echo "步骤1: 发送验证码"
response=$(curl -s -X POST "$BASE_URL/api/auth/user/send-verification-code" \
    -H "Content-Type: application/json" \
    -d "{\"email\": \"$TEST_EMAIL\"}")

echo "$response" | jq . 2>/dev/null || echo "$response"
echo ""

# 2. 验证验证码（需要手动输入验证码）
echo "步骤2: 验证验证码"
echo "请输入收到的验证码: "
read VERIFICATION_CODE

response=$(curl -s -X POST "$BASE_URL/api/auth/user/verify-code" \
    -H "Content-Type: application/json" \
    -d "{\"email\": \"$TEST_EMAIL\", \"code\": \"$VERIFICATION_CODE\"}")

VERIFICATION_TOKEN=$(echo "$response" | jq -r '.data.verification_token' 2>/dev/null)
echo "$response" | jq . 2>/dev/null || echo "$response"
echo ""

if [ -z "$VERIFICATION_TOKEN" ] || [ "$VERIFICATION_TOKEN" = "null" ]; then
    echo -e "${RED}错误: 无法获取验证token，请检查验证码是否正确${NC}"
    exit 1
fi

# 3. 注册用户
echo "步骤3: 注册用户（求职者）"
response=$(curl -s -X POST "$BASE_URL/api/auth/register" \
    -H "Content-Type: application/json" \
    -d "{
        \"email\": \"$TEST_EMAIL\",
        \"password\": \"$TEST_PASSWORD\",
        \"user_type\": \"job_seeker\",
        \"name\": \"$TEST_NAME\",
        \"verification_token\": \"$VERIFICATION_TOKEN\"
    }")

echo "$response" | jq . 2>/dev/null || echo "$response"
echo ""

# 4. 登录
echo "步骤4: 用户登录"
response=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d "{
        \"email\": \"$TEST_EMAIL\",
        \"password\": \"$TEST_PASSWORD\"
    }")

TOKEN=$(echo "$response" | jq -r '.data.access_token' 2>/dev/null)
echo "$response" | jq . 2>/dev/null || echo "$response"
echo ""

if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
    echo -e "${RED}错误: 无法获取访问token${NC}"
    exit 1
fi

# 检查响应中是否包含role字段
ROLE_IN_LOGIN=$(echo "$response" | jq -r '.data.user_info.role' 2>/dev/null)
if [ -n "$ROLE_IN_LOGIN" ] && [ "$ROLE_IN_LOGIN" != "null" ]; then
    echo -e "${GREEN}✓ 登录响应包含role字段: $ROLE_IN_LOGIN${NC}"
else
    echo -e "${RED}✗ 登录响应缺少role字段${NC}"
fi
echo ""

# 5. 获取用户身份
echo "步骤5: 获取用户身份"
test_api "GET /api/user/profile" "GET" "$BASE_URL/api/user/profile" "" "-H \"Authorization: Bearer $TOKEN\""

# 6. 切换身份
echo "步骤6: 切换身份为招聘者"
test_api "POST /api/switch-role" "POST" "$BASE_URL/api/switch-role" \
    "{\"target_role\": \"recruiters\"}" \
    "-H \"Authorization: Bearer $TOKEN\""

# 7. 验证身份已切换
echo "步骤7: 验证身份已切换"
test_api "GET /api/user/profile (验证)" "GET" "$BASE_URL/api/user/profile" "" "-H \"Authorization: Bearer $TOKEN\""

# 8. 切换回求职者
echo "步骤8: 切换回求职者"
test_api "POST /api/switch-role (切换回)" "POST" "$BASE_URL/api/switch-role" \
    "{\"target_role\": \"job_seeker\"}" \
    "-H \"Authorization: Bearer $TOKEN\""

# 9. 测试其他接口
echo "步骤9: 测试其他接口"
test_api "GET /api/job (职位列表)" "GET" "$BASE_URL/api/job?page=1&size=12" "" ""

echo "=================================="
echo "测试完成！"
echo "=================================="

