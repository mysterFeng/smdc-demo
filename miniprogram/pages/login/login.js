const { api } = require('../../utils/api.js');

Page({
  data: {
    isLoading: false,
    phone: '',
    password: ''
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

  // 手机号输入
  onPhoneInput(e) {
    this.setData({
      phone: e.detail.value
    });
  },

  // 密码输入
  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    });
  },

  // 表单验证
  validateForm() {
    const { phone, password } = this.data;
    
    if (!phone) {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      });
      return false;
    }
    
    if (!/^1[3-9]\d{9}$/.test(phone)) {
      wx.showToast({
        title: '请输入正确的手机号',
        icon: 'none'
      });
      return false;
    }
    
    if (!password) {
      wx.showToast({
        title: '请输入密码',
        icon: 'none'
      });
      return false;
    }
    
    if (password.length < 6) {
      wx.showToast({
        title: '密码长度不能少于6位',
        icon: 'none'
      });
      return false;
    }
    
    return true;
  },

  // 手机号密码登录
  phoneLogin() {
    if (this.data.isLoading) return;
    
    if (!this.validateForm()) return;
    
    this.setData({ isLoading: true });
    
    wx.showLoading({
      title: '登录中...'
    });

    // 调用后端API进行登录验证
    api.phoneLogin({
      phone: this.data.phone,
      password: this.data.password
    }).then(res => {
      wx.hideLoading();
      this.setData({ isLoading: false });
      
      const responseData = res.data;
      
      // 保存登录信息
      wx.setStorageSync('token', responseData.token);
      wx.setStorageSync('userInfo', {
        id: responseData.id,
        nickname: responseData.nickname,
        avatar: responseData.avatar,
        phone: responseData.phone,
        gender: responseData.gender
      });

      wx.showToast({
        title: '登录成功',
        icon: 'success'
      });

      // 跳转到首页
      setTimeout(() => {
        this.redirectToHome();
      }, 1500);
    }).catch(err => {
      console.log('登录失败:', err);
      this.handleLoginError(err.message || '登录失败');
    });
  },

  // 微信登录
  wechatLogin() {
    if (this.data.isLoading) return;
    
    wx.showModal({
      title: '微信登录',
      content: '微信登录功能暂时不可用，请使用手机号密码登录',
      showCancel: false,
      confirmText: '知道了'
    });
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

  // 忘记密码
  forgotPassword() {
    wx.showModal({
      title: '忘记密码',
      content: '请联系客服重置密码',
      showCancel: false,
      confirmText: '知道了'
    });
  },

  // 注册账号
  goToRegister() {
    wx.navigateTo({
      url: '/pages/register/register'
    });
  },

  // 跳转到用户协议
  goToUserAgreement() {
    wx.showModal({
      title: '用户协议',
      content: '用户协议内容开发中',
      showCancel: false,
      confirmText: '知道了'
    });
  },

  // 跳转到隐私政策
  goToPrivacyPolicy() {
    wx.showModal({
      title: '隐私政策',
      content: '隐私政策内容开发中',
      showCancel: false,
      confirmText: '知道了'
    });
  },

  // 跳转到首页
  redirectToHome() {
    wx.switchTab({
      url: '/pages/index/index'
    });
  }
}); 