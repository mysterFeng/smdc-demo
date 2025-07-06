-- 安全重建订单表SQL脚本
-- 处理外键约束和表已存在的问题

-- 步骤1: 删除依赖的外键约束
-- 先删除payments表的外键约束（如果存在）
SET FOREIGN_KEY_CHECKS = 0;

-- 删除payments表（如果存在且不需要保留数据）
DROP TABLE IF EXISTS payments;

-- 删除order_items表（如果存在）
DROP TABLE IF EXISTS order_items;

-- 删除orders表（如果存在）
DROP TABLE IF EXISTS orders;

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 步骤2: 创建新的orders表
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT COMMENT '订单ID' PRIMARY KEY,
    order_no VARCHAR(50) NOT NULL COMMENT '订单号' UNIQUE,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING_PAYMENT' COMMENT '订单状态：PENDING_PAYMENT(待支付)、PAID(已支付)、PREPARING(制作中)、READY(待取餐)、COMPLETED(已完成)、CANCELLED(已取消)',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    paid_amount DECIMAL(10,2) COMMENT '实付金额',
    receiver_name VARCHAR(50) COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) COMMENT '收货人电话',
    receiver_address TEXT COMMENT '收货地址',
    remark TEXT COMMENT '备注',
    payment_method VARCHAR(20) COMMENT '支付方式：WECHAT(微信支付)、ALIPAY(支付宝)、CASH(现金)',
    paid_time TIMESTAMP NULL COMMENT '支付时间',
    expected_pickup_time TIMESTAMP NULL COMMENT '预计取餐时间',
    actual_pickup_time TIMESTAMP NULL COMMENT '实际取餐时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
    version BIGINT DEFAULT 0 COMMENT '版本号',
    INDEX idx_user_id (user_id),
    INDEX idx_order_no (order_no),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    CONSTRAINT fk_orders_user_id FOREIGN KEY (user_id) REFERENCES users (id)
) COMMENT '订单表';

-- 步骤3: 创建新的order_items表
CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT COMMENT '订单详情ID' PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    dish_id BIGINT NOT NULL COMMENT '菜品ID',
    dish_name VARCHAR(100) NOT NULL COMMENT '菜品名称',
    dish_image_url VARCHAR(200) COMMENT '菜品图片URL',
    unit_price DECIMAL(10,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL COMMENT '数量',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计',
    remark VARCHAR(200) COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
    version BIGINT DEFAULT 0 COMMENT '版本号',
    INDEX idx_order_id (order_id),
    INDEX idx_dish_id (dish_id),
    CONSTRAINT fk_order_items_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_dish_id FOREIGN KEY (dish_id) REFERENCES dishes (id)
) COMMENT '订单详情表';

-- 步骤4: 重新创建payments表（如果需要）
-- 注意：这里创建的是一个基础的payments表，您可能需要根据实际需求调整
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT COMMENT '支付记录ID' PRIMARY KEY,
    order_id BIGINT NOT NULL COMMENT '订单ID',
    payment_no VARCHAR(50) NOT NULL COMMENT '支付单号' UNIQUE,
    payment_method VARCHAR(20) NOT NULL COMMENT '支付方式',
    payment_amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '支付状态：PENDING(待支付)、SUCCESS(成功)、FAILED(失败)',
    payment_time TIMESTAMP NULL COMMENT '支付时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
    INDEX idx_order_id (order_id),
    INDEX idx_payment_no (payment_no),
    INDEX idx_payment_status (payment_status),
    CONSTRAINT fk_payments_order_id FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE
) COMMENT '支付记录表';

-- 验证表创建成功
SELECT 'orders表创建成功' AS message, COUNT(*) AS table_count FROM information_schema.tables WHERE table_name = 'orders' AND table_schema = DATABASE()
UNION ALL
SELECT 'order_items表创建成功' AS message, COUNT(*) AS table_count FROM information_schema.tables WHERE table_name = 'order_items' AND table_schema = DATABASE()
UNION ALL
SELECT 'payments表创建成功' AS message, COUNT(*) AS table_count FROM information_schema.tables WHERE table_name = 'payments' AND table_schema = DATABASE(); 