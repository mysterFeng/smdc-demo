#!/bin/bash

echo "🚀 启动智能点餐系统..."

# 检查 Docker 是否安装
if ! command -v docker &> /dev/null; then
    echo "❌ Docker 未安装，请先安装 Docker"
    exit 1
fi

# 检查 Docker Compose 是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose 未安装，请先安装 Docker Compose"
    exit 1
fi

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven 未安装，请先安装 Maven"
    exit 1
fi

# 检查 Node.js 是否安装
if ! command -v node &> /dev/null; then
    echo "❌ Node.js 未安装，请先安装 Node.js"
    exit 1
fi

echo "📦 启动 MySQL 和 Redis..."
./docker-dev.sh

echo "⏳ 等待数据库启动..."
sleep 10

echo "🔨 启动后端服务..."
# 在后台启动 Spring Boot 应用
mvn spring-boot:run > backend.log 2>&1 &
BACKEND_PID=$!

echo "⏳ 等待后端服务启动..."
sleep 15

echo "🎨 启动商家管理后台..."
cd admin
npm install > /dev/null 2>&1
npm run dev > ../admin.log 2>&1 &
ADMIN_PID=$!
cd ..

echo "✅ 所有服务启动完成！"
echo ""
echo "📱 访问地址："
echo "   - 后端API: http://localhost:8080"
echo "   - 商家后台: http://localhost:3000"
echo "   - 微信小程序: 使用微信开发者工具打开 miniprogram 目录"
echo ""
echo "📋 服务状态："
echo "   - MySQL: localhost:3306"
echo "   - Redis: localhost:6379"
echo "   - 后端服务: PID $BACKEND_PID"
echo "   - 商家后台: PID $ADMIN_PID"
echo ""
echo "📝 日志文件："
echo "   - 后端日志: backend.log"
echo "   - 商家后台日志: admin.log"
echo ""
echo "🛑 停止所有服务: ./stop-all.sh"

# 保存进程ID到文件
echo $BACKEND_PID > backend.pid
echo $ADMIN_PID > admin.pid 