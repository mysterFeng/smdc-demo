# 智能点餐系统

基于微信小程序 + Spring Boot + Vue.js 的智能点餐系统

## 项目概述

这是一个完整的智能点餐系统，包含：
- **后端服务**：Spring Boot + MySQL + Redis
- **用户端**：微信小程序
- **商家端**：Vue.js + Element Plus 管理后台

## 项目结构

```
demo/
├── src/                    # 后端服务源码
├── miniprogram/           # 微信小程序
│   ├── pages/            # 页面文件
│   ├── app.js            # 小程序入口
│   ├── app.json          # 小程序配置
│   └── app.wxss          # 全局样式
├── admin/                # 商家管理后台
│   ├── src/              # Vue源码
│   ├── package.json      # 依赖配置
│   └── vite.config.js    # Vite配置
├── docker-compose.yml    # Docker环境配置
├── docker-compose-dev.yml # 开发环境配置
└── README.md             # 项目说明
```

## 快速开始

### 1. 启动后端服务

```bash
# 启动 MySQL 和 Redis
./docker-dev.sh

# 启动 Spring Boot 应用
mvn spring-boot:run
```

### 2. 启动商家管理后台

```bash
cd admin
./start.sh
```

### 3. 开发微信小程序

使用微信开发者工具打开 `miniprogram` 目录

## 技术栈

### 后端
- **框架**: Spring Boot 2.6.13
- **数据库**: MySQL 8.0
- **缓存**: Redis 6.2
- **安全**: Spring Security + JWT
- **ORM**: Spring Data JPA
- **工具**: Hutool, Lombok

### 前端（商家后台）
- **框架**: Vue 3.3.4
- **UI库**: Element Plus 2.3.8
- **路由**: Vue Router 4.2.4
- **状态管理**: Pinia 2.1.6
- **构建工具**: Vite 4.4.5
- **图表**: ECharts 5.4.3

### 小程序
- **框架**: 微信小程序原生开发
- **UI组件**: WeUI
- **网络请求**: wx.request API

## 功能模块

### 用户端（小程序）
- ✅ 用户登录注册
- ✅ 菜品浏览搜索
- ✅ 购物车管理
- ✅ 订单创建支付
- ✅ 订单状态跟踪
- ✅ 个人中心管理

### 商家端（管理后台）
- ✅ 仪表盘统计
- ✅ 菜品管理（开发中）
- ✅ 分类管理（开发中）
- ✅ 订单管理（开发中）
- ✅ 用户管理（开发中）
- ✅ 数据统计（开发中）
- ✅ 系统设置（开发中）

## 开发环境

### 环境要求
- JDK 1.8+
- Maven 3.6+
- Node.js 16+
- MySQL 8.0+
- Redis 6.0+
- Docker & Docker Compose

### 端口配置
- **后端服务**: 8080
- **商家后台**: 3000
- **MySQL**: 3306
- **Redis**: 6379

## 部署说明

### 开发环境
```bash
# 1. 启动数据库和缓存
./docker-dev.sh

# 2. 启动后端服务
mvn spring-boot:run

# 3. 启动商家后台
cd admin && npm run dev
```

### 生产环境
```bash
# 使用 Docker Compose 一键部署
./docker-start.sh
```

## API 文档

### 用户相关
- `POST /api/users/login` - 用户登录
- `GET /api/users/{id}` - 获取用户信息
- `PUT /api/users/{id}` - 更新用户信息
- `POST /api/users/{id}/bind-phone` - 绑定手机号

### 菜品相关（待开发）
- `GET /api/dishes` - 获取菜品列表
- `POST /api/dishes` - 创建菜品
- `PUT /api/dishes/{id}` - 更新菜品
- `DELETE /api/dishes/{id}` - 删除菜品

### 订单相关（待开发）
- `GET /api/orders` - 获取订单列表
- `POST /api/orders` - 创建订单
- `PUT /api/orders/{id}` - 更新订单状态

## 开发规范

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用 ESLint + Prettier 进行代码格式化
- 遵循 Git 提交规范

### 命名规范
- **后端**: 大驼峰命名法（类名）、小驼峰命名法（方法名）
- **前端**: 小驼峰命名法（变量、方法）、kebab-case（文件名）
- **数据库**: 小写字母加下划线

## 常见问题

### 1. 端口被占用
```bash
# 查看端口占用
lsof -i :8080

# 结束进程
kill -9 <PID>
```

### 2. 数据库连接失败
```bash
# 检查 MySQL 容器状态
docker-compose -f docker-compose-dev.yml ps

# 查看日志
docker-compose -f docker-compose-dev.yml logs mysql
```

### 3. Redis 连接失败
```bash
# 检查 Redis 容器状态
docker-compose -f docker-compose-dev.yml ps

# 测试连接
docker-compose -f docker-compose-dev.yml exec redis redis-cli ping
```

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证

MIT License

## 联系方式

- 作者: myster
- 邮箱: myster@example.com
- 项目地址: https://github.com/myster/ordering-system

---

**注意**: 这是一个开发中的项目，部分功能仍在开发中。欢迎贡献代码和提出建议！ 