#!/bin/bash

echo "智能点餐小程序后端系统启动脚本"
echo "================================"

# 检查Java环境
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java环境，请先安装JDK 1.8+"
    exit 1
fi

echo "Java版本:"
java -version

# 检查Maven环境
if command -v mvn &> /dev/null; then
    echo "Maven版本:"
    mvn -version
    echo ""
    echo "开始编译和启动项目..."
    mvn spring-boot:run
else
    echo "警告: 未找到Maven，请先安装Maven 3.6+"
    echo ""
    echo "安装Maven的方法:"
    echo "1. 使用Homebrew: brew install maven"
    echo "2. 下载Maven: https://maven.apache.org/download.cgi"
    echo ""
    echo "或者使用IDE（如IntelliJ IDEA）直接运行DemoApplication类"
    exit 1
fi 