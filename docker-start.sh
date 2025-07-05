#!/bin/bash

echo "🚀 启动点餐系统 Docker 环境..."

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

# 停止并删除现有容器
echo "🔄 清理现有容器..."
docker-compose down

# 构建并启动服务
echo "🔨 构建并启动服务..."
docker-compose up -d

# 等待服务启动
echo "⏳ 等待服务启动..."
sleep 10

# 检查服务状态
echo "📊 检查服务状态..."
docker-compose ps

# 检查 MySQL 连接
echo "🔍 检查 MySQL 连接..."
docker-compose exec mysql mysql -u ordering_user -pordering_pass -e "SELECT 1;" 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✅ MySQL 连接正常"
else
    echo "❌ MySQL 连接失败"
fi

# 检查 Redis 连接
echo "🔍 检查 Redis 连接..."
docker-compose exec redis redis-cli ping 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✅ Redis 连接正常"
else
    echo "❌ Redis 连接失败"
fi

echo ""
echo "🎉 Docker 环境启动完成！"
echo ""
echo "📋 服务信息："
echo "   MySQL: localhost:3306 (用户名: ordering_user, 密码: ordering_pass)"
echo "   Redis: localhost:6379"
echo "   应用: http://localhost:8080/api"
echo ""
echo "🔧 常用命令："
echo "   查看日志: docker-compose logs -f"
echo "   停止服务: docker-compose down"
echo "   重启服务: docker-compose restart"
echo "   进入容器: docker-compose exec mysql mysql -u ordering_user -pordering_pass ordering_system" 