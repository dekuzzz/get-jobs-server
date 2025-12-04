#!/bin/bash

set -e  # 遇到错误立即退出

echo "=================================="
echo "开始构建系统"
echo "=================================="

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# 构建后端
echo ""
echo ">>> 1. 构建后端项目..."
echo "清理之前的构建..."
mvn clean

echo "开始Maven打包..."
mvn package -DskipTests

if [ $? -eq 0 ]; then
    echo "✓ 后端构建成功！"
    echo "  JAR包位置: target/backend-1.0.jar"
else
    echo "✗ 后端构建失败！"
    exit 1
fi

# 创建部署包目录
echo ""
echo ">>> 2. 准备部署包..."
cd "$SCRIPT_DIR"
DEPLOY_DIR="deploy-package"
rm -rf "$DEPLOY_DIR"
mkdir -p "$DEPLOY_DIR"

# 复制后端JAR
mkdir -p "$DEPLOY_DIR/backend"
cp target/backend-1.0.jar "$DEPLOY_DIR/backend/"

echo "✓ 部署包准备完成！"
echo "  部署包位置: $DEPLOY_DIR/"

echo ""
echo "=================================="
echo "构建完成！"
echo "=================================="
echo "后端JAR: $DEPLOY_DIR/backend/backend-1.0.jar"
echo ""
echo "接下来可以使用 deploy.sh 脚本部署到云服务器"

