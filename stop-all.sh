#!/bin/bash

echo "🛑 停止智能点餐系统..."

# 停止后端服务
if [ -f "backend.pid" ]; then
    BACKEND_PID=$(cat backend.pid)
    if ps -p $BACKEND_PID > /dev/null; then
        echo "停止后端服务 (PID: $BACKEND_PID)..."
        kill $BACKEND_PID
        rm backend.pid
    else
        echo "后端服务已停止"
        rm backend.pid
    fi
else
    echo "后端服务未运行"
fi

# 停止商家后台
if [ -f "admin.pid" ]; then
    ADMIN_PID=$(cat admin.pid)
    if ps -p $ADMIN_PID > /dev/null; then
        echo "停止商家后台 (PID: $ADMIN_PID)..."
        kill $ADMIN_PID
        rm admin.pid
    else
        echo "商家后台已停止"
        rm admin.pid
    fi
else
    echo "商家后台未运行"
fi

# 停止 Docker 容器
echo "停止 Docker 容器..."
docker-compose -f docker-compose-dev.yml down

echo "✅ 所有服务已停止！" 