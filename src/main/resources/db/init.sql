-- 创建数据库
CREATE DATABASE IF NOT EXISTS ordering_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ordering_system;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    openid VARCHAR(100) UNIQUE NOT NULL COMMENT '微信OpenID',
    nickname VARCHAR(50) COMMENT '微信昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    phone VARCHAR(20) COMMENT '手机号',
    password VARCHAR(32) COMMENT '密码（MD5加密）',
    gender TINYINT DEFAULT 0 COMMENT '性别：0-未知，1-男，2-女',
    status TINYINT DEFAULT 1 COMMENT '用户状态：0-禁用，1-正常',
    last_login_time TIMESTAMP NULL COMMENT '最后登录时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version BIGINT DEFAULT 0 COMMENT '版本号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 菜品分类表
CREATE TABLE IF NOT EXISTS categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(200) COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version BIGINT DEFAULT 0 COMMENT '版本号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品分类表';

-- 菜品表
CREATE TABLE IF NOT EXISTS dishes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜品ID',
    name VARCHAR(100) NOT NULL COMMENT '菜品名称',
    description TEXT COMMENT '菜品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    original_price DECIMAL(10,2) COMMENT '原价',
    category_id BIGINT COMMENT '分类ID',
    image_url VARCHAR(255) COMMENT '图片URL',
    stock INT DEFAULT 0 COMMENT '库存',
    status TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-上架',
    sort_order INT DEFAULT 0 COMMENT '排序',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version BIGINT DEFAULT 0 COMMENT '版本号',
    FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品表';

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) UNIQUE NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    discount_amount DECIMAL(10,2) DEFAULT 0 COMMENT '优惠金额',
    actual_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    status TINYINT DEFAULT 0 COMMENT '订单状态：0-待支付，1-已支付，2-制作中，3-已完成，4-已取消',
    payment_method TINYINT COMMENT '支付方式：1-微信支付',
    payment_time TIMESTAMP NULL COMMENT '支付时间',
    delivery_address TEXT COMMENT '配送地址',
    delivery_phone VARCHAR(20) COMMENT '配送电话',
    delivery_name VARCHAR(50) COMMENT '配送姓名',
    remark TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version BIGINT DEFAULT 0 COMMENT '版本号',
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单详情表
CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单详情ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    dish_name VARCHAR(100) NOT NULL COMMENT '菜品名称',
    dish_price DECIMAL(10,2) NOT NULL COMMENT '菜品价格',
    quantity INT NOT NULL COMMENT '数量',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (dish_id) REFERENCES dishes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- 地址表
CREATE TABLE IF NOT EXISTS addresses (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '地址ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    detail_address TEXT NOT NULL COMMENT '详细地址',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version BIGINT DEFAULT 0 COMMENT '版本号',
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地址表';

-- 支付记录表
CREATE TABLE IF NOT EXISTS payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '支付记录ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    payment_no VARCHAR(100) UNIQUE NOT NULL COMMENT '支付单号',
    amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    payment_method TINYINT NOT NULL COMMENT '支付方式：1-微信支付',
    status TINYINT DEFAULT 0 COMMENT '支付状态：0-待支付，1-支付成功，2-支付失败',
    transaction_id VARCHAR(100) COMMENT '微信支付交易号',
    paid_time TIMESTAMP NULL COMMENT '支付时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version BIGINT DEFAULT 0 COMMENT '版本号',
    FOREIGN KEY (order_id) REFERENCES orders(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- 清理现有数据
DELETE FROM dishes;
DELETE FROM categories;

-- 重置自增ID
ALTER TABLE dishes AUTO_INCREMENT = 1;
ALTER TABLE categories AUTO_INCREMENT = 1;

-- 插入分类数据
INSERT INTO categories (name, description, sort_order, status, created_at, updated_at, version) VALUES
('热菜', '各种热炒菜品', 1, 1, NOW(), NOW(), 0),
('凉菜', '各种凉拌菜品', 2, 1, NOW(), NOW(), 0),
('汤类', '各种汤品', 3, 1, NOW(), NOW(), 0),
('主食', '米饭、面条等', 4, 1, NOW(), NOW(), 0),
('饮品', '各种饮料', 5, 1, NOW(), NOW(), 0);

-- 插入菜品数据
INSERT INTO dishes (name, description, price, original_price, category_id, image_url, stock, status, sort_order, created_at, updated_at, version) VALUES
('宫保鸡丁', '经典川菜，鸡肉配花生米', 28.00, 32.00, 1, '/images/dishes/gongbao-chicken.jpg', 100, 1, 0, NOW(), NOW(), 0),
('麻婆豆腐', '麻辣鲜香的豆腐菜品', 18.00, 22.00, 1, '/images/dishes/mapo-tofu.jpg', 50, 1, 0, NOW(), NOW(), 0),
('红烧肉', '肥而不腻的红烧肉', 35.00, 40.00, 1, '/images/dishes/hongshao-rou.jpg', 80, 1, 0, NOW(), NOW(), 0),
('糖醋里脊', '酸甜可口的糖醋里脊', 32.00, 38.00, 1, '/images/dishes/tangcu-liji.jpg', 60, 1, 0, NOW(), NOW(), 0),
('凉拌黄瓜', '清爽可口的凉菜', 12.00, 15.00, 2, '/images/dishes/liangban-huanggua.jpg', 30, 1, 0, NOW(), NOW(), 0),
('口水鸡', '麻辣鲜香的口水鸡', 25.00, 30.00, 2, '/images/dishes/koushui-ji.jpg', 40, 1, 0, NOW(), NOW(), 0),
('紫菜蛋花汤', '营养丰富的汤品', 8.00, 10.00, 3, '/images/dishes/zicai-danhua.jpg', 80, 1, 0, NOW(), NOW(), 0),
('酸辣汤', '开胃解腻的酸辣汤', 10.00, 12.00, 3, '/images/dishes/suanla-tang.jpg', 70, 1, 0, NOW(), NOW(), 0),
('白米饭', '香软可口的白米饭', 2.00, 3.00, 4, '/images/dishes/rice.jpg', 200, 1, 0, NOW(), NOW(), 0),
('阳春面', '清淡爽口的阳春面', 8.00, 10.00, 4, '/images/dishes/yangchun-mian.jpg', 100, 1, 0, NOW(), NOW(), 0),
('可乐', '冰镇可口可乐', 5.00, 6.00, 5, '/images/dishes/cola.jpg', 100, 1, 0, NOW(), NOW(), 0),
('柠檬水', '清爽解腻的柠檬水', 6.00, 8.00, 5, '/images/dishes/lemon-water.jpg', 80, 1, 0, NOW(), NOW(), 0);

-- 插入测试用户账号（密码：123456，MD5加密后为：e10adc3949ba59abbe56e057f20f883e）
INSERT INTO users (openid, nickname, phone, password, status) VALUES 
('test_openid_001', '测试用户', '13800138000', 'e10adc3949ba59abbe56e057f20f883e', 1); 