const api = require('../../utils/api.js').api;

Page({
  data: {
    phone: '',
    verifyCode: '',
    nickname: '',
    password: '',
    confirmPassword: '',
    gender: 0, // 0-保密，1-男，2-女
    isLoading: false,
    isSendingCode: false,
    canSendCode: false,
    countdown: 0
  },

  onLoad(options) {
    // 页面加载时的初始化
  },

  // 手机号输入
  onPhoneInput(e) {
    const phone = e.detail.value;
    this.setData({
      phone,
      canSendCode: this.validatePhone(phone)
    });
  },

  // 验证码输入
  onVerifyCodeInput(e) {
    this.setData({
      verifyCode: e.detail.value
    });
  },

  // 昵称输入
  onNicknameInput(e) {
    this.setData({
      nickname: e.detail.value
    });
  },

  // 密码输入
  onPasswordInput(e) {
    this.setData({
      password: e.detail.value
    });
  },

  // 确认密码输入
  onConfirmPasswordInput(e) {
    this.setData({
      confirmPassword: e.detail.value
    });
  },

  // 选择性别
  selectGender(e) {
    const gender = parseInt(e.currentTarget.dataset.gender);
    this.setData({ gender });
  },

  // 验证手机号
  validatePhone(phone) {
    return /^1[3-9]\d{9}$/.test(phone);
  },

  // 发送验证码
  sendVerifyCode() {
    if (!this.data.canSendCode || this.data.isSendingCode || this.data.countdown > 0) {
      return;
    }

    this.setData({ isSendingCode: true });

    api.sendVerifyCode({
      phone: this.data.phone,
      type: 'register'
    }).then(res => {
      wx.showToast({
        title: '验证码已发送',
        icon: 'success'
      });
      
      // 开始倒计时
      this.startCountdown();
    }).catch(err => {
      console.log('发送验证码失败:', err);
      wx.showToast({
        title: err.message || '发送失败',
        icon: 'none'
      });
    }).finally(() => {
      this.setData({ isSendingCode: false });
    });
  },

  // 开始倒计时
  startCountdown() {
    this.setData({ countdown: 60 });
    
    const timer = setInterval(() => {
      if (this.data.countdown > 0) {
        this.setData({
          countdown: this.data.countdown - 1
        });
      } else {
        clearInterval(timer);
      }
    }, 1000);
  },

  // 表单验证
  validateForm() {
    const { phone, verifyCode, password, confirmPassword } = this.data;
    
    if (!phone) {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      });
      return false;
    }
    
    if (!this.validatePhone(phone)) {
      wx.showToast({
        title: '请输入正确的手机号',
        icon: 'none'
      });
      return false;
    }
    
    if (!verifyCode) {
      wx.showToast({
        title: '请输入验证码',
        icon: 'none'
      });
      return false;
    }
    
    if (verifyCode.length !== 6) {
      wx.showToast({
        title: '验证码长度不正确',
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
    
    if (!confirmPassword) {
      wx.showToast({
        title: '请确认密码',
        icon: 'none'
      });
      return false;
    }
    
    if (password !== confirmPassword) {
      wx.showToast({
        title: '两次输入的密码不一致',
        icon: 'none'
      });
      return false;
    }
    
    return true;
  },

  // 注册
  register() {
    if (this.data.isLoading) return;
    
    if (!this.validateForm()) return;
    
    this.setData({ isLoading: true });
    
    wx.showLoading({
      title: '注册中...'
    });

    // 调用后端API进行注册
    api.register({
      phone: this.data.phone,
      verifyCode: this.data.verifyCode,
      nickname: this.data.nickname,
      password: this.data.password,
      confirmPassword: this.data.confirmPassword,
      gender: this.data.gender
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
        title: '注册成功',
        icon: 'success'
      });

      // 跳转到首页
      setTimeout(() => {
        this.redirectToHome();
      }, 1500);
    }).catch(err => {
      console.log('注册失败:', err);
      this.handleRegisterError(err.message || '注册失败');
    });
  },

  // 处理注册错误
  handleRegisterError(message) {
    wx.hideLoading();
    this.setData({ isLoading: false });
    
    wx.showToast({
      title: message,
      icon: 'none',
      duration: 2000
    });
  },

  // 跳转到首页
  redirectToHome() {
    wx.switchTab({
      url: '/pages/index/index'
    });
  },

  // 跳转到登录页面
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  }
}); 