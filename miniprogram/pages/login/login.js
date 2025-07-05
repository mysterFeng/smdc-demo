Page({
  data: {
    isLoading: false
  },

  onLoad() {
    // 检查是否已经登录
    this.checkLoginStatus();
  },

  // 检查登录状态
  checkLoginStatus() {
    const userInfo = wx.getStorageSync('userInfo');
    const token = wx.getStorageSync('token');
    
    if (userInfo && token) {
      // 已经登录，跳转到首页
      this.redirectToHome();
    }
  },

  // 微信登录
  wechatLogin() {
    if (this.data.isLoading) return;
    
    this.setData({ isLoading: true });
    
    wx.showLoading({
      title: '登录中...'
    });

    // 获取微信登录凭证
    wx.login({
      success: (res) => {
        if (res.code) {
          // 获取用户信息
          wx.getUserProfile({
            desc: '用于完善用户资料',
            success: (userRes) => {
              this.handleLoginSuccess(res.code, userRes.userInfo);
            },
            fail: (err) => {
              console.log('获取用户信息失败:', err);
              // 用户拒绝授权，使用默认信息
              this.handleLoginSuccess(res.code, {
                nickName: '微信用户',
                avatarUrl: '/images/default-avatar.png'
              });
            }
          });
        } else {
          this.handleLoginError('获取登录凭证失败');
        }
      },
      fail: (err) => {
        console.log('微信登录失败:', err);
        this.handleLoginError('微信登录失败');
      }
    });
  },

  // 处理登录成功
  handleLoginSuccess(code, userInfo) {
    // 这里应该调用后端API进行登录验证
    // 模拟API调用
    setTimeout(() => {
      // 模拟登录成功返回的数据
      const mockResponse = {
        token: 'mock_token_' + Date.now(),
        userInfo: {
          ...userInfo,
          id: 1,
          phone: '138****8888'
        }
      };

      // 保存登录信息
      wx.setStorageSync('token', mockResponse.token);
      wx.setStorageSync('userInfo', mockResponse.userInfo);

      wx.hideLoading();
      this.setData({ isLoading: false });

      wx.showToast({
        title: '登录成功',
        icon: 'success'
      });

      // 跳转到首页
      setTimeout(() => {
        this.redirectToHome();
      }, 1500);
    }, 1000);
  },

  // 处理登录失败
  handleLoginError(message) {
    wx.hideLoading();
    this.setData({ isLoading: false });
    
    wx.showToast({
      title: message,
      icon: 'none'
    });
  },

  // 手机号登录
  phoneLogin() {
    wx.showModal({
      title: '手机号登录',
      content: '手机号登录功能开发中，请使用微信登录',
      showCancel: false,
      confirmText: '知道了'
    });
  },

  // 跳转到用户协议
  goToUserAgreement() {
    wx.navigateTo({
      url: '/pages/agreement/user-agreement'
    });
  },

  // 跳转到隐私政策
  goToPrivacyPolicy() {
    wx.navigateTo({
      url: '/pages/agreement/privacy-policy'
    });
  },

  // 跳转到首页
  redirectToHome() {
    wx.switchTab({
      url: '/pages/index/index'
    });
  }
}); 