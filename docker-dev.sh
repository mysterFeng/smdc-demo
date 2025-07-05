#!/bin/bash

echo "🚀 启动点餐系统开发环境 (MySQL + Redis)..."

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
docker-compose -f docker-compose-dev.yml down

# 启动服务
echo "🔨 启动 MySQL 和 Redis 服务..."
docker-compose -f docker-compose-dev.yml up -d

# 等待服务启动
echo "⏳ 等待服务启动..."
sleep 15

# 检查服务状态
echo "📊 检查服务状态..."
docker-compose -f docker-compose-dev.yml ps

# 检查 MySQL 连接
echo "🔍 检查 MySQL 连接..."
docker-compose -f docker-compose-dev.yml exec mysql mysql -u ordering_user -pordering_pass -e "SELECT 1;" 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✅ MySQL 连接正常"
else
    echo "❌ MySQL 连接失败，请稍等片刻后重试"
fi

# 检查 Redis 连接
echo "🔍 检查 Redis 连接..."
docker-compose -f docker-compose-dev.yml exec redis redis-cli ping 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✅ Redis 连接正常"
else
    echo "❌ Redis 连接失败，请稍等片刻后重试"
fi

echo ""
echo "🎉 开发环境启动完成！"
echo ""
echo "📋 服务信息："
echo "   MySQL: localhost:3306"
echo "     数据库: ordering_system"
echo "     用户名: ordering_user"
echo "     密码: ordering_pass"
echo "     超级用户: root/12345678"
echo "   Redis: localhost:6379"
echo ""
echo "🔧 常用命令："
echo "   查看日志: docker-compose -f docker-compose-dev.yml logs -f"
echo "   停止服务: docker-compose -f docker-compose-dev.yml down"
echo "   重启服务: docker-compose -f docker-compose-dev.yml restart"
echo "   进入MySQL: docker-compose -f docker-compose-dev.yml exec mysql mysql -u ordering_user -pordering_pass ordering_system"
echo "   进入Redis: docker-compose -f docker-compose-dev.yml exec redis redis-cli"
echo ""
echo "💡 现在你可以在本地运行 Spring Boot 项目了！"
echo "   运行命令: mvn spring-boot:run" 