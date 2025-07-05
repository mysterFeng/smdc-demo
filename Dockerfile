# 使用官方OpenJDK 8作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制Maven配置文件
COPY pom.xml .

# 复制源代码
COPY src ./src

# 安装Maven并构建项目
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests && \
    apk del maven

# 暴露端口
EXPOSE 8080

# 启动应用
CMD ["java", "-jar", "target/ordering-system-1.0.0.jar"] 