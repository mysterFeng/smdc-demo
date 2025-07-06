// API基础配置
// 开发环境：使用内网穿透域名或配置的合法域名
// 生产环境：使用正式的HTTPS域名
const BASE_URL = 'http://localhost:8080/api'; // 开发环境使用局域网IP，注意添加/api前缀

// 请求封装
const request = (options) => {
  return new Promise((resolve, reject) => {
    // 获取token
    const token = wx.getStorageSync('token');
    
    console.log('发起请求:', {
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : '',
        ...options.header
      }
    });
    
    wx.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : '',
        ...options.header
      },
      success: (res) => {
        console.log('请求成功:', res);
        
        if (res.statusCode === 200) {
          // 检查响应数据格式
          if (res.data && typeof res.data === 'object') {
            if (res.data.code === 200) {
              resolve(res.data);
            } else if (res.data.code === 401) {
              // token过期，跳转到登录页
              wx.removeStorageSync('token');
              wx.removeStorageSync('userInfo');
              wx.reLaunch({
                url: '/pages/login/login'
              });
              reject(new Error('登录已过期'));
            } else {
              console.error('业务错误:', res.data);
              reject(new Error(res.data.message || '请求失败'));
            }
          } else {
            console.error('响应数据格式错误:', res.data);
            reject(new Error('响应数据格式错误'));
          }
        } else {
          console.error('HTTP错误:', res.statusCode, res.data);
          reject(new Error(`HTTP ${res.statusCode}`));
        }
      },
      fail: (err) => {
        console.error('网络请求失败:', err);
        reject(new Error('网络请求失败'));
      }
    });
  });
};

// API接口
const api = {
  // 微信登录
  login: (data) => {
    return request({
      url: '/v1/users/login',
      method: 'POST',
      data
    });
  },
  
  // 手机号密码登录
  phoneLogin: (data) => {
    return request({
      url: '/v1/users/phone-login',
      method: 'POST',
      data
    });
  },
  
  // 获取用户信息
  getUserInfo: (userId) => {
    return request({
      url: `/v1/users/${userId}`,
      method: 'GET'
    });
  },
  
  // 更新用户信息
  updateUser: (userId, data) => {
    return request({
      url: `/v1/users/${userId}`,
      method: 'PUT',
      data
    });
  },
  
  // 绑定手机号
  bindPhone: (userId, phone) => {
    return request({
      url: `/v1/users/${userId}/bind-phone`,
      method: 'POST',
      data: { phone }
    });
  },
  
  // 用户注册
  register: (data) => {
    return request({
      url: '/v1/users/register',
      method: 'POST',
      data
    });
  },
  
  // 发送验证码
  sendVerifyCode: (data) => {
    return request({
      url: '/v1/users/send-verify-code',
      method: 'POST',
      data
    });
  },
  
  // ========== 菜品相关接口 ==========
  
  // 获取菜品列表（支持按分类过滤）
  getDishList: (page = 0, size = 10, categoryId = null) => {
    let url = `/v1/dishes?page=${page}&size=${size}`;
    if (categoryId) {
      url += `&categoryId=${categoryId}`;
    }
    return request({
      url: url,
      method: 'GET'
    });
  },
  
  // 根据分类获取菜品（分页）
  getDishesByCategory: (categoryId, page = 0, size = 10) => {
    return request({
      url: `/v1/dishes?categoryId=${categoryId}&page=${page}&size=${size}`,
      method: 'GET'
    });
  },
  
  // 获取菜品详情
  getDishById: (id) => {
    return request({
      url: `/v1/dishes/${id}`,
      method: 'GET'
    });
  },
  
  // 搜索菜品
  searchDishes: (keyword) => {
    return request({
      url: `/v1/dishes/search?keyword=${encodeURIComponent(keyword)}`,
      method: 'GET'
    });
  },
  
  // 获取推荐菜品
  getRecommendDishes: (limit = 4) => {
    return request({
      url: `/v1/dishes/recommend?limit=${limit}`,
      method: 'GET'
    });
  },
  
  // ========== 分类相关接口 ==========
  
  // 获取所有分类
  getAllCategories: () => {
    return request({
      url: '/v1/categories',
      method: 'GET'
    });
  },
  
  // 获取启用的分类
  getActiveCategories: () => {
    return request({
      url: '/v1/categories/active',
      method: 'GET'
    });
  },
  
  // 获取分类详情
  getCategoryById: (id) => {
    return request({
      url: `/v1/categories/${id}`,
      method: 'GET'
    });
  },
  
  // ========== 购物车相关接口 ==========
  
  // 添加菜品到购物车
  addToCart: (userId, data) => {
    return request({
      url: `/v1/cart/${userId}/items`,
      method: 'POST',
      data
    });
  },
  
  // 更新购物车项目
  updateCartItem: (userId, data) => {
    return request({
      url: `/v1/cart/${userId}/items`,
      method: 'PUT',
      data
    });
  },
  
  // 删除购物车项目
  removeCartItem: (userId, cartItemId) => {
    return request({
      url: `/v1/cart/${userId}/items/${cartItemId}`,
      method: 'DELETE'
    });
  },
  
  // 根据菜品ID删除购物车项目
  removeCartItemByDishId: (userId, dishId) => {
    return request({
      url: `/v1/cart/${userId}/items/dish/${dishId}`,
      method: 'DELETE'
    });
  },
  
  // 清空购物车
  clearCart: (userId) => {
    return request({
      url: `/v1/cart/${userId}/items`,
      method: 'DELETE'
    });
  },
  
  // 清空选中的购物车项目
  clearSelectedItems: (userId) => {
    return request({
      url: `/v1/cart/${userId}/items/selected`,
      method: 'DELETE'
    });
  },
  
  // 获取用户购物车
  getUserCart: (userId) => {
    return request({
      url: `/v1/cart/${userId}`,
      method: 'GET'
    });
  },
  
  // 获取用户购物车项目列表
  getUserCartItems: (userId) => {
    return request({
      url: `/v1/cart/${userId}/items`,
      method: 'GET'
    });
  },
  
  // 获取用户选中的购物车项目
  getSelectedCartItems: (userId) => {
    return request({
      url: `/v1/cart/${userId}/items/selected`,
      method: 'GET'
    });
  },
  
  // 切换购物车项目选中状态
  toggleCartItemSelection: (userId, cartItemId) => {
    return request({
      url: `/v1/cart/${userId}/items/${cartItemId}/toggle`,
      method: 'PUT'
    });
  },
  
  // 全选/取消全选购物车项目
  toggleAllCartItemsSelection: (userId, selected) => {
    return request({
      url: `/v1/cart/${userId}/items/select-all`,
      method: 'PUT',
      data: { selected }
    });
  },
  
  // 获取购物车项目数量
  getCartItemCount: (userId) => {
    return request({
      url: `/v1/cart/${userId}/count`,
      method: 'GET'
    });
  },
  
  // ========== 订单相关接口 ==========
  
  // 创建订单
  createOrder: (data) => {
    return request({
      url: '/v1/orders',
      method: 'POST',
      data
    });
  },
  
  // 获取订单详情
  getOrderById: (orderId) => {
    return request({
      url: `/v1/orders/${orderId}`,
      method: 'GET'
    });
  },
  
  // 根据订单号获取订单详情
  getOrderByOrderNo: (orderNo) => {
    return request({
      url: `/v1/orders/no/${orderNo}`,
      method: 'GET'
    });
  },
  
  // 获取用户订单列表
  getUserOrders: (userId, page = 0, size = 10) => {
    return request({
      url: `/v1/orders/user/${userId}?page=${page}&size=${size}`,
      method: 'GET'
    });
  },
  
  // 根据状态获取用户订单列表
  getUserOrdersByStatus: (userId, status, page = 0, size = 10) => {
    return request({
      url: `/v1/orders/user/${userId}/status/${status}?page=${page}&size=${size}`,
      method: 'GET'
    });
  },
  
  // 取消订单
  cancelOrder: (orderId, userId) => {
    return request({
      url: `/v1/orders/${orderId}/cancel?userId=${userId}`,
      method: 'POST'
    });
  },
  
  // 支付订单
  payOrder: (orderId, userId, paymentMethod) => {
    return request({
      url: `/v1/orders/${orderId}/pay?userId=${userId}&paymentMethod=${paymentMethod}`,
      method: 'POST'
    });
  },
  
  // 确认收货（更新订单状态为已完成）
  confirmReceive: (orderId) => {
    return request({
      url: `/v1/orders/${orderId}/status?status=COMPLETED`,
      method: 'POST'
    });
  },
  
  // ========== 地址相关接口 ==========
  
  // 获取用户地址列表
  getUserAddresses: (userId) => {
    return request({
      url: `/v1/addresses?userId=${userId}`,
      method: 'GET'
    });
  },
  
  // 添加地址
  addAddress: (userId, data) => {
    return request({
      url: `/v1/addresses?userId=${userId}`,
      method: 'POST',
      data
    });
  },
  
  // 更新地址
  updateAddress: (id, userId, data) => {
    return request({
      url: `/v1/addresses/${id}?userId=${userId}`,
      method: 'PUT',
      data
    });
  },
  
  // 删除地址
  deleteAddress: (id, userId) => {
    return request({
      url: `/v1/addresses/${id}?userId=${userId}`,
      method: 'DELETE'
    });
  },
  
  // 设置默认地址
  setDefaultAddress: (id, userId) => {
    return request({
      url: `/v1/addresses/${id}/default?userId=${userId}`,
      method: 'PUT'
    });
  },
  
  // ========== 优惠券相关接口 ==========
  
  // 获取可用优惠券列表
  getAvailableCoupons: () => {
    return request({
      url: '/v1/coupons',
      method: 'GET'
    });
  },
  
  // 获取优惠券详情
  getCouponById: (id) => {
    return request({
      url: `/v1/coupons/${id}`,
      method: 'GET'
    });
  },
  
  // 用户领取优惠券
  receiveCoupon: (userId, couponId) => {
    return request({
      url: '/v1/coupons/receive',
      method: 'POST',
      data: { userId, couponId }
    });
  },
  
  // 获取用户优惠券列表
  getUserCoupons: (userId, status = null) => {
    let url = `/v1/coupons/user/${userId}`;
    if (status !== null) {
      url += `?status=${status}`;
    }
    return request({
      url: url,
      method: 'GET'
    });
  },
  
  // 获取用户可用优惠券（根据订单金额筛选）
  getAvailableUserCoupons: (userId, orderAmount) => {
    return request({
      url: `/v1/coupons/user/${userId}/available?orderAmount=${orderAmount}`,
      method: 'GET'
    });
  },
  
  // 使用优惠券
  useCoupon: (userId, userCouponId, orderId) => {
    return request({
      url: '/v1/coupons/use',
      method: 'POST',
      data: { userId, userCouponId, orderId }
    });
  },
  
  // 验证优惠券是否可用
  validateCoupon: (userId, userCouponId, orderAmount) => {
    return request({
      url: '/v1/coupons/validate',
      method: 'POST',
      data: { userId, userCouponId, orderAmount }
    });
  },
  
  // 获取地址详情
  getAddressById: (id, userId) => request({ url: `/v1/addresses/${id}?userId=${userId}` }),
  
  // ========== 用户相关接口 ==========
  
  // 获取用户信息
  getUserInfo: (userId) => request({ url: `/v1/users/${userId}` }),
  
  // 更新用户信息
  updateUserInfo: (userId, data) => request({ url: `/v1/users/${userId}`, method: 'PUT', data }),
  
  // 获取用户积分
  getUserPoints: (userId) => request({ url: `/v1/users/${userId}/points` }),
  
  // 获取积分记录
  getPointsRecords: (userId, page = 0, size = 10) => request({ url: `/v1/users/${userId}/points/records?page=${page}&size=${size}` }),
  
  // 积分兑换
  exchangePoints: (userId, itemId) => request({ url: `/v1/users/${userId}/points/exchange`, method: 'POST', data: { itemId } }),
  
  // ========== 反馈相关接口 ==========
  
  // 提交反馈
  submitFeedback: (userId, data) => request({ url: `/v1/feedback?userId=${userId}`, method: 'POST', data }),
  
  // 获取反馈列表
  getFeedbackList: (userId, page = 0, size = 10) => request({ url: `/v1/feedback?userId=${userId}&page=${page}&size=${size}` }),
  
  // ========== 统计相关接口 ==========
  
  // 获取用户订单统计
  getUserOrderStats: (userId) => request({ url: `/v1/users/${userId}/order-stats` }),
  
  // 获取用户优惠券统计
  getUserCouponStats: (userId) => request({ url: `/v1/users/${userId}/coupon-stats` })
};

module.exports = {
  request,
  api
}; 