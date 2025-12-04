#!/bin/bash

# 数据库迁移执行脚本
# 使用PostgreSQL 15，密码: WCwsad123456

echo "=================================="
echo "数据库迁移脚本"
echo "=================================="
echo ""

# 检查PostgreSQL是否运行
if ! pg_isready -h localhost -p 5432 > /dev/null 2>&1; then
    echo "⚠️  PostgreSQL服务未运行或无法连接"
    echo "请先启动PostgreSQL服务："
    echo "  macOS: brew services start postgresql@15"
    echo "  或: pg_ctl -D /usr/local/var/postgres start"
    echo ""
    read -p "是否继续尝试连接？(y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# 执行Python迁移脚本
echo "开始执行数据库迁移..."
python3 migrate_database.py

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 数据库迁移完成！"
    echo ""
    echo "下一步："
    echo "1. 编译项目: mvn clean package -DskipTests"
    echo "2. 启动服务: java -jar target/backend-1.0.jar"
    echo "3. 测试接口: ./test_api.sh"
else
    echo ""
    echo "❌ 数据库迁移失败，请检查："
    echo "1. PostgreSQL服务是否运行"
    echo "2. 数据库连接信息是否正确"
    echo "3. 用户权限是否足够"
    exit 1
fi

