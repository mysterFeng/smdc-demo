// app.js
App({
  globalData: {
    userInfo: null,
    token: null,
    baseUrl: 'http://localhost:8080/api',
    cartItems: [],
    selectedAddress: null
  },

  onLaunch() {
    // 检查登录状态
    this.checkLoginStatus();
    
    // 获取系统信息
    this.getSystemInfo();
  },

  // 检查登录状态
  checkLoginStatus() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    
    if (token && userInfo) {
      this.globalData.token = token;
      this.globalData.userInfo = userInfo;
    }
  },

  // 获取系统信息
  getSystemInfo() {
    wx.getSystemInfo({
      success: (res) => {
        this.globalData.systemInfo = res;
      }
    });
  },

  // 微信登录
  wxLogin() {
    return new Promise((resolve, reject) => {
      wx.login({
        success: (res) => {
          if (res.code) {
            // 发送 res.code 到后台换取 openId, sessionKey, unionId
            this.loginWithCode(res.code).then(resolve).catch(reject);
          } else {
            reject(new Error('登录失败！' + res.errMsg));
          }
        },
        fail: reject
      });
    });
  },

  // 使用code登录
  loginWithCode(code) {
    return new Promise((resolve, reject) => {
      wx.request({
        url: `${this.globalData.baseUrl}/users/login`,
        method: 'POST',
        data: {
          code: code
        },
        success: (res) => {
          if (res.data.code === 200) {
            const { token, userInfo } = res.data.data;
            
            // 保存登录信息
            this.globalData.token = token;
            this.globalData.userInfo = userInfo;
            
            wx.setStorageSync('token', token);
            wx.setStorageSync('userInfo', userInfo);
            
            resolve(res.data.data);
          } else {
            reject(new Error(res.data.message || '登录失败'));
          }
        },
        fail: reject
      });
    });
  },

  // 请求封装
  request(options) {
    return new Promise((resolve, reject) => {
      const token = this.globalData.token;
      
      wx.request({
        url: `${this.globalData.baseUrl}${options.url}`,
        method: options.method || 'GET',
        data: options.data || {},
        header: {
          'Content-Type': 'application/json',
          'Authorization': token ? `Bearer ${token}` : '',
          ...options.header
        },
        success: (res) => {
          if (res.statusCode === 200) {
            if (res.data.code === 200) {
              resolve(res.data);
            } else if (res.data.code === 401) {
              // token过期，重新登录
              this.handleTokenExpired();
              reject(new Error('登录已过期，请重新登录'));
            } else {
              reject(new Error(res.data.message || '请求失败'));
            }
          } else {
            reject(new Error(`HTTP ${res.statusCode}`));
          }
        },
        fail: reject
      });
    });
  },

  // 处理token过期
  handleTokenExpired() {
    this.globalData.token = null;
    this.globalData.userInfo = null;
    wx.removeStorageSync('token');
    wx.removeStorageSync('userInfo');
    
    wx.showToast({
      title: '登录已过期',
      icon: 'none'
    });
    
    // 跳转到登录页
    wx.reLaunch({
      url: '/pages/login/login'
    });
  },

  // 购物车相关方法
  addToCart(dish) {
    const cartItems = this.globalData.cartItems;
    const existingItem = cartItems.find(item => item.id === dish.id);
    
    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      cartItems.push({
        ...dish,
        quantity: 1
      });
    }
    
    this.updateCart();
  },

  removeFromCart(dishId) {
    const cartItems = this.globalData.cartItems;
    const index = cartItems.findIndex(item => item.id === dishId);
    
    if (index > -1) {
      cartItems.splice(index, 1);
      this.updateCart();
    }
  },

  updateCartItemQuantity(dishId, quantity) {
    const cartItems = this.globalData.cartItems;
    const item = cartItems.find(item => item.id === dishId);
    
    if (item) {
      if (quantity <= 0) {
        this.removeFromCart(dishId);
      } else {
        item.quantity = quantity;
        this.updateCart();
      }
    }
  },

  updateCart() {
    // 更新购物车到本地存储
    wx.setStorageSync('cartItems', this.globalData.cartItems);
    
    // 更新购物车tab的徽标
    const totalQuantity = this.globalData.cartItems.reduce((total, item) => total + item.quantity, 0);
    if (totalQuantity > 0) {
      wx.setTabBarBadge({
        index: 2, // 购物车tab的索引
        text: totalQuantity.toString()
      });
    } else {
      wx.removeTabBarBadge({
        index: 2
      });
    }
  },

  clearCart() {
    this.globalData.cartItems = [];
    wx.removeStorageSync('cartItems');
    wx.removeTabBarBadge({
      index: 2
    });
  },

  getCartTotal() {
    return this.globalData.cartItems.reduce((total, item) => {
      return total + (item.price * item.quantity);
    }, 0);
  }
}); 