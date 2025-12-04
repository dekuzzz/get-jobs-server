#!/bin/bash

# 一键设置和运行脚本
# PostgreSQL 15, 密码: WCwsad123456

set -e

echo "=================================="
echo "GetJob 后端服务设置脚本"
echo "=================================="
echo ""

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 步骤1: 检查并启动PostgreSQL
echo -e "${YELLOW}步骤1: 检查PostgreSQL服务...${NC}"
if python3 -c "
import psycopg2
try:
    conn = psycopg2.connect(host='localhost', port='5432', database='postgres', user='postgres', password='WCwsad123456')
    conn.close()
    exit(0)
except:
    exit(1)
" 2>/dev/null; then
    echo -e "${GREEN}✓ PostgreSQL服务已运行${NC}"
else
    echo -e "${YELLOW}⚠ PostgreSQL服务未运行，尝试启动...${NC}"
    
    # 尝试使用brew services启动
    if command -v brew &> /dev/null; then
        echo "使用Homebrew启动PostgreSQL..."
        brew services start postgresql@15 2>/dev/null || brew services start postgresql 2>/dev/null || true
        sleep 3
    fi
    
    # 再次检查
    if python3 -c "
import psycopg2
try:
    conn = psycopg2.connect(host='localhost', port='5432', database='postgres', user='postgres', password='WCwsad123456')
    conn.close()
    exit(0)
except:
    exit(1)
    " 2>/dev/null; then
        echo -e "${GREEN}✓ PostgreSQL服务已启动${NC}"
    else
        echo -e "${RED}✗ 无法连接到PostgreSQL${NC}"
        echo ""
        echo "请手动启动PostgreSQL服务："
        echo "  brew services start postgresql@15"
        echo "  或: pg_ctl -D /usr/local/var/postgresql@15 start"
        echo ""
        read -p "是否继续尝试执行迁移？(y/n) " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
fi

echo ""

# 步骤2: 执行数据库迁移
echo -e "${YELLOW}步骤2: 执行数据库迁移...${NC}"
if python3 migrate_database.py; then
    echo -e "${GREEN}✓ 数据库迁移完成${NC}"
else
    echo -e "${RED}✗ 数据库迁移失败${NC}"
    echo "请检查："
    echo "1. PostgreSQL服务是否运行"
    echo "2. 数据库连接信息是否正确"
    echo "3. 用户权限是否足够"
    exit 1
fi

echo ""

# 步骤3: 检查Java版本
echo -e "${YELLOW}步骤3: 检查Java版本...${NC}"
JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -ge 17 ] 2>/dev/null; then
    echo -e "${GREEN}✓ Java版本: $JAVA_VERSION (满足要求)${NC}"
    HAS_JAVA17=true
else
    echo -e "${YELLOW}⚠ Java版本: $JAVA_VERSION (需要Java 17)${NC}"
    HAS_JAVA17=false
fi

echo ""

# 步骤4: 选择启动方式
echo -e "${YELLOW}步骤4: 选择启动方式...${NC}"
if [ "$HAS_JAVA17" = true ]; then
    echo "检测到Java 17，可以选择："
    echo "1. 使用Maven直接运行 (推荐)"
    echo "2. 编译后运行JAR"
    echo "3. 使用Docker"
    read -p "请选择 (1/2/3，默认1): " choice
    choice=${choice:-1}
else
    echo "未检测到Java 17，建议使用Docker"
    read -p "是否使用Docker启动？(y/n，默认y): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]] || [ -z "$REPLY" ]; then
        choice=3
    else
        echo "需要Java 17才能编译运行，请先安装Java 17"
        exit 1
    fi
fi

echo ""

# 步骤5: 启动服务
echo -e "${YELLOW}步骤5: 启动服务...${NC}"
case $choice in
    1)
        echo "使用Maven启动服务..."
        mvn spring-boot:run &
        SERVER_PID=$!
        echo "服务正在启动，PID: $SERVER_PID"
        echo "等待服务启动..."
        sleep 10
        ;;
    2)
        echo "编译项目..."
        mvn clean package -DskipTests
        echo "启动服务..."
        java -jar target/backend-1.0.jar &
        SERVER_PID=$!
        echo "服务正在启动，PID: $SERVER_PID"
        echo "等待服务启动..."
        sleep 10
        ;;
    3)
        echo "使用Docker启动服务..."
        docker-compose up -d
        echo "等待服务启动..."
        sleep 15
        ;;
esac

echo ""

# 步骤6: 验证服务
echo -e "${YELLOW}步骤6: 验证服务...${NC}"
if curl -s http://localhost:8080/actuator/health > /dev/null 2>&1 || curl -s http://localhost:8080/api/job > /dev/null 2>&1; then
    echo -e "${GREEN}✓ 服务已启动 (http://localhost:8080)${NC}"
else
    echo -e "${YELLOW}⚠ 无法验证服务状态，但可能已启动${NC}"
    echo "请手动检查: http://localhost:8080"
fi

echo ""
echo "=================================="
echo -e "${GREEN}设置完成！${NC}"
echo "=================================="
echo ""
echo "服务地址: http://localhost:8080"
echo ""
echo "下一步："
echo "1. 运行测试脚本: ./test_api.sh"
echo "2. 或访问Swagger文档: http://localhost:8080/swagger-ui.html"
echo ""
echo "停止服务："
if [ "$choice" = "3" ]; then
    echo "  docker-compose down"
else
    echo "  按Ctrl+C或运行: kill $SERVER_PID"
fi

