-- 更新优惠券数据，设置有效的过期时间
-- 创建时间：2025-07-06

-- 更新优惠券模板的过期时间为2025年底
UPDATE coupons SET 
    end_time = '2025-12-31 23:59:59',
    updated_at = NOW()
WHERE id IN (1, 2, 3, 4, 5);

-- 更新用户优惠券的过期时间
UPDATE user_coupons SET 
    expired_at = '2025-12-31 23:59:59',
    updated_at = NOW()
WHERE id IN (1, 2, 3, 4, 5);

-- 查看更新后的数据
SELECT '优惠券模板数据:' as info;
SELECT id, name, start_time, end_time, status FROM coupons;

SELECT '用户优惠券数据:' as info;
SELECT id, user_id, coupon_id, status, expired_at FROM user_coupons; 