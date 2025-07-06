-- 修复数据库表结构脚本
-- 解决表已存在和字段不匹配的问题

-- 1. 先删除可能存在的旧表（注意外键约束）
SET FOREIGN_KEY_CHECKS = 0;

-- 删除可能存在的旧表
DROP TABLE IF EXISTS `user_coupons`;
DROP TABLE IF EXISTS `addresses`;
DROP TABLE IF EXISTS `coupons`;

SET FOREIGN_KEY_CHECKS = 1;

-- 2. 重新创建优惠券模板表
CREATE TABLE `coupons` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` VARCHAR(64) NOT NULL COMMENT '优惠券名称',
  `type` VARCHAR(32) NOT NULL DEFAULT 'discount' COMMENT '优惠券类型：discount-满减券，cash-现金券',
  `value` DECIMAL(10,2) NOT NULL COMMENT '优惠金额',
  `min_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '满减门槛金额',
  `total_count` INT NOT NULL DEFAULT 0 COMMENT '发放总量，0表示无限制',
  `received_count` INT NOT NULL DEFAULT 0 COMMENT '已领取数量',
  `start_time` DATETIME NOT NULL COMMENT '生效时间',
  `end_time` DATETIME NOT NULL COMMENT '失效时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '优惠券描述',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status_time` (`status`, `start_time`, `end_time`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券模板表';

-- 3. 重新创建用户优惠券表
CREATE TABLE `user_coupons` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `coupon_id` BIGINT NOT NULL COMMENT '优惠券模板ID',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-未使用，1-已使用，2-已过期',
  `order_id` BIGINT DEFAULT NULL COMMENT '使用订单ID',
  `received_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `used_at` DATETIME DEFAULT NULL COMMENT '使用时间',
  `expired_at` DATETIME NOT NULL COMMENT '过期时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_status` (`user_id`, `status`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_expired_at` (`expired_at`),
  CONSTRAINT `fk_user_coupons_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_coupons_coupon_id` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- 4. 重新创建用户地址表
CREATE TABLE `addresses` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `name` VARCHAR(32) NOT NULL COMMENT '收货人姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
  `province` VARCHAR(32) NOT NULL COMMENT '省',
  `city` VARCHAR(32) NOT NULL COMMENT '市',
  `district` VARCHAR(32) NOT NULL COMMENT '区/县',
  `detail` VARCHAR(128) NOT NULL COMMENT '详细地址',
  `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认地址：0-否，1-是',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_user_default` (`user_id`, `is_default`),
  CONSTRAINT `fk_addresses_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户地址表';

-- 5. 插入测试数据

-- 插入优惠券模板数据
INSERT INTO `coupons` (`name`, `type`, `value`, `min_amount`, `total_count`, `start_time`, `end_time`, `status`, `description`) VALUES
('满30减5', 'discount', 5.00, 30.00, 1000, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 1, '满30元可用，立减5元'),
('满50减10', 'discount', 10.00, 50.00, 500, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 1, '满50元可用，立减10元'),
('满100减20', 'discount', 20.00, 100.00, 200, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 1, '满100元可用，立减20元'),
('新用户专享券', 'discount', 15.00, 0.00, 1000, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 1, '新用户专享，无门槛使用');

-- 插入用户优惠券数据（假设用户ID为2）
INSERT INTO `user_coupons` (`user_id`, `coupon_id`, `status`, `expired_at`) VALUES
(2, 1, 0, '2024-12-31 23:59:59'),
(2, 2, 0, '2024-12-31 23:59:59'),
(2, 4, 0, '2024-12-31 23:59:59');

-- 插入用户地址数据（假设用户ID为2）
INSERT INTO `addresses` (`user_id`, `name`, `phone`, `province`, `city`, `district`, `detail`, `is_default`) VALUES
(2, '张三', '13800000001', '北京市', '北京市', '朝阳区', '三里屯街道xxx号', 1),
(2, '李四', '13800000002', '北京市', '北京市', '海淀区', '中关村街道yyy号', 0),
(2, '王五', '13800000003', '北京市', '北京市', '西城区', '西单街道zzz号', 0);

-- 6. 验证数据插入成功
SELECT '优惠券模板数据' as table_name, COUNT(*) as count FROM coupons
UNION ALL
SELECT '用户优惠券数据' as table_name, COUNT(*) as count FROM user_coupons
UNION ALL
SELECT '用户地址数据' as table_name, COUNT(*) as count FROM addresses; 