const { api } = require('../../utils/api.js');

Page({
  data: {
    userInfo: {},
    isLoggedIn: false,
    userId: null,
    stats: {
      pendingCount: 0,
      deliveringCount: 0,
      reviewCount: 0,
      completedCount: 0
    },
    couponCount: 0,
    points: 0
  },

  onLoad() {
    this.loadUserInfo();
    this.loadStats();
  },

  onShow() {
    this.loadUserInfo();
    this.loadStats();
  },

  // 加载用户信息
  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    const isLoggedIn = !!userInfo;
    
    this.setData({
      userInfo: userInfo || {},
      isLoggedIn,
      userId: userInfo ? userInfo.id || 1 : 1 // 设置默认用户ID
    });
  },

  // 加载统计数据
  loadStats() {
    // 设置默认值
    this.setData({
      stats: {
        pendingCount: 0,
        deliveringCount: 0,
        reviewCount: 0,
        completedCount: 0
      },
      couponCount: 0,
      points: 0
    });

    // 调用后端API获取统计数据
    api.getUserOrderStats(this.data.userId).then(res => {
      if (res.code === 200 && res.data) {
        this.setData({
          stats: res.data
        });
      }
    }).catch(err => {
      console.error('获取订单统计失败:', err);
      // 模拟数据
      this.setData({
        stats: {
          pendingCount: 2,
          deliveringCount: 1,
          reviewCount: 3,
          completedCount: 8
        }
      });
    });
    
    // 获取优惠券统计
    api.getUserCouponStats(this.data.userId).then(res => {
      if (res.code === 200 && res.data) {
        this.setData({
          couponCount: res.data.availableCount || 0
        });
      }
    }).catch(err => {
      console.error('获取优惠券统计失败:', err);
      // 模拟数据
      this.setData({
        couponCount: 5
      });
    });
    
    // 获取积分信息
    api.getUserPoints(this.data.userId).then(res => {
      if (res.code === 200 && res.data) {
        this.setData({
          points: res.data.points || 0
        });
      }
    }).catch(err => {
      console.error('获取积分信息失败:', err);
      // 模拟数据
      this.setData({
        points: 1280
      });
    });
  },

  // 跳转到用户信息页面
  goToUserInfo() {
    if (!this.data.isLoggedIn) {
      this.login();
      return;
    }
    
    wx.navigateTo({
      url: '/pages/user-info/user-info'
    });
  },

  // 微信登录
  login() {
    wx.showLoading({
      title: '登录中...'
    });

    wx.login({
      success: (res) => {
        if (res.code) {
          // 这里应该调用后端API进行登录
          console.log('登录成功，code:', res.code);
          
          // 模拟登录成功
          setTimeout(() => {
            const mockUserInfo = {
              nickName: '测试用户',
              avatarUrl: '/images/default-avatar.png',
              phone: '138****8888'
            };
            
            wx.setStorageSync('userInfo', mockUserInfo);
            this.setData({
              userInfo: mockUserInfo,
              isLoggedIn: true
            });
            
            wx.hideLoading();
            wx.showToast({
              title: '登录成功',
              icon: 'success'
            });
          }, 1000);
        } else {
          wx.hideLoading();
          wx.showToast({
            title: '登录失败',
            icon: 'none'
          });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({
          title: '登录失败',
          icon: 'none'
        });
      }
    });
  },

  // 跳转到订单页面
  goToOrders(e) {
    const status = e.currentTarget.dataset.status;
    wx.switchTab({
      url: '/pages/order/order'
    });
    
    // 可以通过全局状态传递选中的状态
    getApp().globalData.selectedOrderStatus = status;
  },

  // 跳转到地址管理
  goToAddress() {
    wx.navigateTo({
      url: '/pages/address/address'
    });
  },

  // 跳转到优惠券页面
  goToCoupons() {
    wx.navigateTo({
      url: '/pages/coupons/coupons'
    });
  },

  // 跳转到积分中心
  goToPoints() {
    wx.navigateTo({
      url: '/pages/points/points'
    });
  },

  // 跳转到客服中心
  goToCustomerService() {
    wx.showModal({
      title: '客服中心',
      content: '客服电话：400-123-4567\n工作时间：9:00-18:00',
      showCancel: false,
      confirmText: '知道了'
    });
  },

  // 跳转到意见反馈
  goToFeedback() {
    wx.navigateTo({
      url: '/pages/feedback/feedback'
    });
  },

  // 跳转到关于我们
  goToAbout() {
    wx.navigateTo({
      url: '/pages/about/about'
    });
  },

  // 退出登录
  logout() {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          // 清除本地存储的用户信息
          wx.removeStorageSync('userInfo');
          wx.removeStorageSync('token');
          
          this.setData({
            userInfo: {},
            isLoggedIn: false
          });
          
          wx.showToast({
            title: '已退出登录',
            icon: 'success'
          });
        }
      }
    });
  }
}); 