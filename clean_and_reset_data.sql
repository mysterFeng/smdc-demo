-- 清理数据库中的乱码数据
USE ordering_system;

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

-- 验证数据
SELECT 'Categories:' as info;
SELECT * FROM categories;

SELECT 'Dishes:' as info;
SELECT * FROM dishes; 