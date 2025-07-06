-- MySQL dump 10.13  Distrib 8.0.42, for Linux (aarch64)
--
-- Host: host.docker.internal    Database: ordering_system
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `addresses`
--

DROP TABLE IF EXISTS `addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `addresses` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `name` varchar(32) NOT NULL COMMENT '收货人姓名',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `province` varchar(32) NOT NULL COMMENT '省',
  `city` varchar(32) NOT NULL COMMENT '市',
  `district` varchar(32) NOT NULL COMMENT '区/县',
  `detail` varchar(128) NOT NULL COMMENT '详细地址',
  `is_default` tinyint NOT NULL DEFAULT '0' COMMENT '是否默认地址：0-否，1-是',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_user_default` (`user_id`,`is_default`),
  CONSTRAINT `fk_addresses_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户地址表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `addresses`
--

LOCK TABLES `addresses` WRITE;
/*!40000 ALTER TABLE `addresses` DISABLE KEYS */;
INSERT INTO `addresses` VALUES (1,2,'张三','13800000001','北京市','北京市','朝阳区','三里屯街道xxx号',1,'2025-07-06 05:29:02','2025-07-06 05:29:02'),(2,2,'李四','13800000002','北京市','北京市','海淀区','中关村街道yyy号',0,'2025-07-06 05:29:02','2025-07-06 05:29:02'),(4,2,'请问','13800000003','河北省','石家庄市','长安区','我饿请问',0,'2025-07-06 16:02:00','2025-07-06 16:15:04');
/*!40000 ALTER TABLE `addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `dish_id` bigint NOT NULL COMMENT '菜品ID',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '菜品数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '菜品单价（快照）',
  `dish_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜品名称（快照）',
  `dish_image_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜品图片URL（快照）',
  `selected` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否选中',
  `remark` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注信息',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_dish_id` (`dish_id`),
  KEY `idx_user_dish` (`user_id`,`dish_id`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `FK709eickf3kc0dujx3ub9i7btf` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKqf96jt4hthdxw36s3ebnq1yns` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车项目表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `description` varchar(200) DEFAULT NULL COMMENT '分类描述',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-禁用，1-正常',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜品分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'热菜','各种热炒菜品',1,1,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(2,'凉菜','各种凉拌菜品',2,1,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(3,'汤类','各种汤品',3,1,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(4,'主食','米饭、面条等',4,1,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(5,'饮品','各种饮料',5,1,'2025-07-06 00:32:43','2025-07-06 00:32:43',0);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coupons`
--

DROP TABLE IF EXISTS `coupons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupons` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) NOT NULL COMMENT '优惠券名称',
  `type` varchar(32) NOT NULL DEFAULT 'discount' COMMENT '优惠券类型：discount-满减券，cash-现金券',
  `value` decimal(10,2) NOT NULL COMMENT '优惠金额',
  `min_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '满减门槛金额',
  `total_count` int NOT NULL DEFAULT '0' COMMENT '发放总量，0表示无限制',
  `received_count` int NOT NULL DEFAULT '0' COMMENT '已领取数量',
  `start_time` datetime NOT NULL COMMENT '生效时间',
  `end_time` datetime NOT NULL COMMENT '失效时间',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `description` varchar(255) DEFAULT NULL COMMENT '优惠券描述',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status_time` (`status`,`start_time`,`end_time`),
  KEY `idx_type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='优惠券模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coupons`
--

LOCK TABLES `coupons` WRITE;
/*!40000 ALTER TABLE `coupons` DISABLE KEYS */;
INSERT INTO `coupons` VALUES (1,'满30减5','discount',5.00,30.00,1000,0,'2024-01-01 00:00:00','2024-12-31 23:59:59',1,'满30元可用，立减5元','2025-07-06 05:29:02','2025-07-06 05:29:02'),(2,'满50减10','discount',10.00,50.00,500,0,'2024-01-01 00:00:00','2024-12-31 23:59:59',1,'满50元可用，立减10元','2025-07-06 05:29:02','2025-07-06 05:29:02'),(3,'满100减20','discount',20.00,100.00,200,0,'2024-01-01 00:00:00','2024-12-31 23:59:59',1,'满100元可用，立减20元','2025-07-06 05:29:02','2025-07-06 05:29:02'),(4,'新用户专享券','discount',15.00,0.00,1000,0,'2024-01-01 00:00:00','2024-12-31 23:59:59',1,'新用户专享，无门槛使用','2025-07-06 05:29:02','2025-07-06 05:29:02');
/*!40000 ALTER TABLE `coupons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dishes`
--

DROP TABLE IF EXISTS `dishes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dishes` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜品ID',
  `name` varchar(100) NOT NULL COMMENT '菜品名称',
  `description` text COMMENT '菜品描述',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片URL',
  `stock` int DEFAULT '0' COMMENT '库存',
  `status` tinyint DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `sort_order` int DEFAULT '0' COMMENT '排序',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `dishes_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dishes`
--

LOCK TABLES `dishes` WRITE;
/*!40000 ALTER TABLE `dishes` DISABLE KEYS */;
INSERT INTO `dishes` VALUES (1,'宫保鸡丁','经典川菜，鸡肉配花生米',28.00,32.00,1,'/images/dishes/gongbao-chicken.jpg',100,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(2,'麻婆豆腐','麻辣鲜香的豆腐菜品',18.00,22.00,1,'/images/dishes/mapo-tofu.jpg',50,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(3,'红烧肉','肥而不腻的红烧肉',35.00,40.00,1,'/images/dishes/hongshao-rou.jpg',80,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(4,'糖醋里脊','酸甜可口的糖醋里脊',32.00,38.00,1,'/images/dishes/tangcu-liji.jpg',60,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(5,'凉拌黄瓜','清爽可口的凉菜',12.00,15.00,2,'/images/dishes/liangban-huanggua.jpg',30,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(6,'口水鸡','麻辣鲜香的口水鸡',25.00,30.00,2,'/images/dishes/koushui-ji.jpg',40,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(7,'紫菜蛋花汤','营养丰富的汤品',8.00,10.00,3,'/images/dishes/zicai-danhua.jpg',80,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(8,'酸辣汤','开胃解腻的酸辣汤',10.00,12.00,3,'/images/dishes/suanla-tang.jpg',70,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(9,'白米饭','香软可口的白米饭',2.00,3.00,4,'/images/dishes/rice.jpg',200,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(10,'阳春面','清淡爽口的阳春面',8.00,10.00,4,'/images/dishes/yangchun-mian.jpg',100,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(11,'可乐','冰镇可口可乐',5.00,6.00,5,'/images/dishes/cola.jpg',100,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0),(12,'柠檬水','清爽解腻的柠檬水',6.00,8.00,5,'/images/dishes/lemon-water.jpg',80,1,0,'2025-07-06 00:32:43','2025-07-06 00:32:43',0);
/*!40000 ALTER TABLE `dishes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单详情ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `dish_id` bigint NOT NULL COMMENT '菜品ID',
  `dish_name` varchar(100) NOT NULL COMMENT '菜品名称',
  `dish_image_url` varchar(200) DEFAULT NULL COMMENT '菜品图片URL',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `quantity` int NOT NULL COMMENT '数量',
  `subtotal` decimal(10,2) NOT NULL COMMENT '小计',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_dish_id` (`dish_id`),
  CONSTRAINT `fk_order_items_dish_id` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`),
  CONSTRAINT `fk_order_items_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单详情表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,1,1,'宫保鸡丁','/images/dishes/gongbao-chicken.jpg',28.00,2,56.00,'','2025-07-06 11:38:09','2025-07-06 11:38:09',0),(2,2,2,'麻婆豆腐','/images/dishes/mapo-tofu.jpg',18.00,1,18.00,NULL,'2025-07-06 11:39:46','2025-07-06 11:39:46',0),(3,2,3,'红烧肉','/images/dishes/hongshao-rou.jpg',35.00,2,70.00,NULL,'2025-07-06 11:39:46','2025-07-06 11:39:46',0),(4,3,1,'宫保鸡丁','/images/dishes/gongbao-chicken.jpg',28.00,3,84.00,'','2025-07-06 15:05:50','2025-07-06 15:05:50',0),(5,3,2,'麻婆豆腐','/images/dishes/mapo-tofu.jpg',18.00,3,54.00,'','2025-07-06 15:05:50','2025-07-06 15:05:50',0),(6,4,1,'宫保鸡丁','/images/dishes/gongbao-chicken.jpg',28.00,1,28.00,'','2025-07-06 15:18:05','2025-07-06 15:18:05',0),(7,4,2,'麻婆豆腐','/images/dishes/mapo-tofu.jpg',18.00,1,18.00,'','2025-07-06 15:18:05','2025-07-06 15:18:05',0),(8,4,3,'红烧肉','/images/dishes/hongshao-rou.jpg',35.00,1,35.00,'','2025-07-06 15:18:05','2025-07-06 15:18:05',0),(9,5,3,'红烧肉','/images/dishes/hongshao-rou.jpg',35.00,1,35.00,'','2025-07-06 15:45:04','2025-07-06 15:45:04',0),(10,5,1,'宫保鸡丁','/images/dishes/gongbao-chicken.jpg',28.00,1,28.00,'','2025-07-06 15:45:04','2025-07-06 15:45:04',0),(11,5,2,'麻婆豆腐','/images/dishes/mapo-tofu.jpg',18.00,1,18.00,'','2025-07-06 15:45:04','2025-07-06 15:45:04',0),(12,6,1,'宫保鸡丁','/images/dishes/gongbao-chicken.jpg',28.00,2,56.00,'','2025-07-06 21:44:20','2025-07-06 21:44:20',0),(13,7,2,'麻婆豆腐','/images/dishes/mapo-tofu.jpg',18.00,1,18.00,'','2025-07-06 21:45:07','2025-07-06 21:45:07',0),(14,8,1,'宫保鸡丁','/images/dishes/gongbao-chicken.jpg',28.00,1,28.00,'','2025-07-06 21:52:16','2025-07-06 21:52:16',0);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `status` varchar(20) NOT NULL DEFAULT 'PENDING_PAYMENT' COMMENT '订单状态：PENDING_PAYMENT(待支付)、PAID(已支付)、PREPARING(制作中)、READY(待取餐)、COMPLETED(已完成)、CANCELLED(已取消)',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `paid_amount` decimal(10,2) DEFAULT NULL COMMENT '实付金额',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收货人电话',
  `receiver_address` text COMMENT '收货地址',
  `remark` text COMMENT '备注',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式：WECHAT(微信支付)、ALIPAY(支付宝)、CASH(现金)',
  `paid_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `expected_pickup_time` timestamp NULL DEFAULT NULL COMMENT '预计取餐时间',
  `actual_pickup_time` timestamp NULL DEFAULT NULL COMMENT '实际取餐时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_status` (`status`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `fk_orders_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'ORDER202507061038096937',2,'PENDING_PAYMENT',56.00,NULL,'张三','13800000001','北京市朝阳区xxx街道xxx号','请尽快配送',NULL,NULL,NULL,NULL,'2025-07-06 11:38:09','2025-07-06 11:38:09',0),(2,'ORDER202507061039464251',2,'CANCELLED',88.00,NULL,'李四','13800000002','北京市海淀区yyy街道yyy号','不要辣椒',NULL,NULL,NULL,NULL,'2025-07-06 11:39:46','2025-07-06 15:44:33',1),(3,'ORDER202507061405497132',2,'COMPLETED',138.00,138.00,'张三','13800000001','北京市北京市朝阳区三里屯街道xxx号','厄齐尔','WECHAT','2025-07-06 15:31:27','2025-07-06 16:01:27','2025-07-06 15:44:48','2025-07-06 15:05:50','2025-07-06 15:44:48',2),(4,'ORDER202507061418047527',2,'COMPLETED',81.00,81.00,'张三','13800000001','北京市北京市朝阳区三里屯街道xxx号','','WECHAT','2025-07-06 15:18:12','2025-07-06 15:48:12','2025-07-06 15:44:10','2025-07-06 15:18:05','2025-07-06 15:44:10',2),(5,'ORDER202507061445035439',2,'PAID',81.00,81.00,'张三','13800000001','北京市北京市朝阳区三里屯街道xxx号','','WECHAT','2025-07-06 15:45:13','2025-07-06 16:15:13',NULL,'2025-07-06 15:45:04','2025-07-06 15:45:13',1),(6,'ORDER202507062044203864',2,'PAID',56.00,56.00,'张三','13800000001','北京市北京市朝阳区三里屯街道xxx号','','WECHAT','2025-07-06 21:44:26','2025-07-06 22:14:26',NULL,'2025-07-06 21:44:20','2025-07-06 21:44:26',1),(7,'ORDER202507062045073161',2,'COMPLETED',18.00,18.00,'张三','13800000001','北京市北京市朝阳区三里屯街道xxx号','','WECHAT','2025-07-06 21:45:10','2025-07-06 22:15:10','2025-07-06 21:45:29','2025-07-06 21:45:07','2025-07-06 21:45:29',2),(8,'ORDER202507062052163049',2,'PAID',28.00,28.00,'张三','13800000001','北京市北京市朝阳区三里屯街道xxx号','','WECHAT','2025-07-06 21:52:19','2025-07-06 22:22:19',NULL,'2025-07-06 21:52:16','2025-07-06 21:52:19',1);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '支付记录ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `payment_no` varchar(50) NOT NULL COMMENT '支付单号',
  `payment_method` varchar(20) NOT NULL COMMENT '支付方式',
  `payment_amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `payment_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '支付状态：PENDING(待支付)、SUCCESS(成功)、FAILED(失败)',
  `payment_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `payment_no` (`payment_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_payment_no` (`payment_no`),
  KEY `idx_payment_status` (`payment_status`),
  CONSTRAINT `fk_payments_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_coupons`
--

DROP TABLE IF EXISTS `user_coupons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_coupons` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `coupon_id` bigint NOT NULL COMMENT '优惠券模板ID',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未使用，1-已使用，2-已过期',
  `order_id` bigint DEFAULT NULL COMMENT '使用订单ID',
  `received_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `used_at` datetime DEFAULT NULL COMMENT '使用时间',
  `expired_at` datetime NOT NULL COMMENT '过期时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_status` (`user_id`,`status`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_expired_at` (`expired_at`),
  CONSTRAINT `fk_user_coupons_coupon_id` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_user_coupons_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户优惠券表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_coupons`
--

LOCK TABLES `user_coupons` WRITE;
/*!40000 ALTER TABLE `user_coupons` DISABLE KEYS */;
INSERT INTO `user_coupons` VALUES (1,2,1,1,1,'2025-07-06 05:29:02','2025-07-06 21:54:31','2026-07-06 14:47:57','2025-07-06 05:29:02','2025-07-06 21:54:31'),(2,2,2,0,NULL,'2025-07-06 05:29:02',NULL,'2026-07-06 14:47:57','2025-07-06 05:29:02','2025-07-06 14:47:57'),(3,2,4,0,NULL,'2025-07-06 05:29:02',NULL,'2026-07-06 14:47:57','2025-07-06 05:29:02','2025-07-06 14:47:57');
/*!40000 ALTER TABLE `user_coupons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `openid` varchar(100) NOT NULL COMMENT '微信OpenID',
  `nickname` varchar(50) DEFAULT NULL COMMENT '微信昵称',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `gender` tinyint DEFAULT '0' COMMENT '性别：0-未知，1-男，2-女',
  `status` tinyint DEFAULT '1' COMMENT '用户状态：0-禁用，1-正常',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` bigint DEFAULT '0' COMMENT '版本号',
  `password` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `openid` (`openid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'test_openid_001','æµ‹è¯•ç”¨æˆ·',NULL,'13800138000',0,1,'2025-07-05 22:12:17','2025-07-05 11:22:53','2025-07-05 22:12:17',6,'e10adc3949ba59abbe56e057f20f883e'),(2,'phone_13800000001','myster',NULL,'13800000001',1,1,'2025-07-06 14:41:59','2025-07-05 22:42:51','2025-07-06 14:41:59',4,'e10adc3949ba59abbe56e057f20f883e');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-06 14:18:25
