#!/bin/bash

# 云服务器部署脚本
# 用途：将本地构建的包部署到云服务器，使用Docker容器运行
# 用法：./deploy.sh <服务器IP> <root密码>

set -e  # 遇到错误立即退出

# 检查参数
if [ $# -ne 2 ]; then
    echo "用法: $0 <服务器IP> <root密码>"
    echo "示例: $0 192.168.1.100 your_password"
    exit 1
fi

SERVER_IP=$1
ROOT_PASSWORD=$2

echo "=================================="
echo "开始部署系统"
echo "=================================="
echo "目标服务器: $SERVER_IP"
echo ""

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

# 检查部署包是否存在
DEPLOY_DIR="deploy-package"
if [ ! -d "$DEPLOY_DIR" ]; then
    echo "✗ 错误：找不到部署包目录 $DEPLOY_DIR"
    echo "请先运行 ./build.sh 构建项目"
    exit 1
fi

# 检查是否安装了sshpass
if ! command -v sshpass &> /dev/null; then
    echo ">>> 安装 sshpass 工具..."
    if [[ "$OSTYPE" == "darwin"* ]]; then
        # macOS
        if command -v brew &> /dev/null; then
            brew install hudochenkov/sshpass/sshpass
        else
            echo "✗ 错误：请先安装 Homebrew 或手动安装 sshpass"
            exit 1
        fi
    elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
        # Linux
        sudo apt-get update && sudo apt-get install -y sshpass
    fi
fi

# 定义SSH命令函数
ssh_exec() {
    sshpass -p "$ROOT_PASSWORD" ssh -o StrictHostKeyChecking=no root@$SERVER_IP "$1"
}

scp_copy() {
    sshpass -p "$ROOT_PASSWORD" scp -o StrictHostKeyChecking=no -r "$1" root@$SERVER_IP:"$2"
}

echo ">>> 1. 测试服务器连接..."
if ssh_exec "echo '连接成功'"; then
    echo "✓ 服务器连接正常"
else
    echo "✗ 无法连接到服务器，请检查IP地址和密码"
    exit 1
fi

echo ""
echo ">>> 2. 安装Docker（如果未安装）..."
ssh_exec "
    if ! command -v docker &> /dev/null; then
        echo '正在安装Docker...'
        curl -fsSL https://get.docker.com | sh
        systemctl start docker
        systemctl enable docker
    else
        echo 'Docker已安装'
    fi

    if ! command -v docker-compose &> /dev/null; then
        echo '正在安装Docker Compose...'
        curl -L \"https://github.com/docker/compose/releases/latest/download/docker-compose-\$(uname -s)-\$(uname -m)\" -o /usr/local/bin/docker-compose
        chmod +x /usr/local/bin/docker-compose
    else
        echo 'Docker Compose已安装'
    fi
"

echo ""
echo ">>> 3. 创建服务器目录..."
ssh_exec "
    mkdir -p /opt/getjob/backend
"

echo ""
echo ">>> 4. 上传部署文件..."
echo "上传后端JAR包..."
scp_copy "$DEPLOY_DIR/backend/backend-1.0.jar" "/opt/getjob/backend/"

echo "上传Docker配置文件..."
scp_copy "Dockerfile.backend" "/opt/getjob/"
scp_copy "docker-compose.yml" "/opt/getjob/"

echo ""
echo ">>> 5. 检查并释放端口..."
ssh_exec "
    # 检查并杀死占用8080端口的进程
    if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
        echo '释放8080端口...'
        kill -9 \$(lsof -t -i:8080) 2>/dev/null || true
    fi

    echo '端口检查完成'
"

echo ""
echo ">>> 6. 停止并清理旧容器..."
ssh_exec "
    cd /opt/getjob
    docker-compose down 2>/dev/null || true
    docker rm -f getjob-backend 2>/dev/null || true
"

echo ""
echo ">>> 7. 启动Docker容器..."
ssh_exec "
    cd /opt/getjob
    docker-compose up -d --build
"

echo ""
echo ">>> 8. 等待服务启动..."
sleep 10

echo ""
echo ">>> 9. 检查容器状态..."
ssh_exec "
    cd /opt/getjob
    docker-compose ps
"

echo ""
echo "=================================="
echo "部署完成！"
echo "=================================="
echo ""
echo "查看日志："
echo "  docker-compose logs -f backend"
echo ""

