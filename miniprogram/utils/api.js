// API基础配置
// 开发环境：使用内网穿透域名或配置的合法域名
// 生产环境：使用正式的HTTPS域名
const BASE_URL = 'http://127.0.0.1:8080/api'; // 开发环境使用127.0.0.1，注意添加/api前缀

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
  }
};

module.exports = {
  request,
  api
}; 