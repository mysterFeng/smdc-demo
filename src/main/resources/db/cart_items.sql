-- 购物车项目表
CREATE TABLE IF NOT EXISTS `cart_items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `dish_id` BIGINT NOT NULL COMMENT '菜品ID',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '菜品数量',
  `unit_price` DECIMAL(10,2) NOT NULL COMMENT '菜品单价（快照）',
  `dish_name` VARCHAR(100) NOT NULL COMMENT '菜品名称（快照）',
  `dish_image_url` VARCHAR(255) COMMENT '菜品图片URL（快照）',
  `selected` BOOLEAN NOT NULL DEFAULT TRUE COMMENT '是否选中',
  `remark` VARCHAR(500) COMMENT '备注信息',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` BIGINT NOT NULL DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_dish_id` (`dish_id`),
  KEY `idx_user_dish` (`user_id`, `dish_id`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车项目表';

-- 添加外键约束（可选）
-- ALTER TABLE `cart_items` ADD CONSTRAINT `fk_cart_items_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `cart_items` ADD CONSTRAINT `fk_cart_items_dish_id` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`) ON DELETE CASCADE; 