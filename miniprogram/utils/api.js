// API基础配置
// 开发环境：使用内网穿透域名或配置的合法域名
// 生产环境：使用正式的HTTPS域名
const BASE_URL = 'http://192.168.1.8:8080/api'; // 开发环境使用局域网IP，注意添加/api前缀

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
  }
};

module.exports = {
  request,
  api
}; 