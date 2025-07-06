const { api } = require('../../utils/api');

Page({
  data: {
    currentTab: 0, // 0-可用, 1-已使用, 2-已过期
    coupons: [],
    availableCount: 0,
    usedCount: 0,
    expiredCount: 0,
    userId: 2 // TODO: 实际项目应从登录态获取
  },

  onLoad() {
    this.loadCoupons();
  },

  onShow() {
    this.loadCoupons();
  },

  // 切换标签
  switchTab(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({
      currentTab: index
    });
    this.loadCoupons();
  },

  // 加载优惠券列表
  loadCoupons() {
    wx.showLoading({
      title: '加载中...'
    });

    const status = this.data.currentTab;
    
    api.getUserCoupons(this.data.userId, status).then(res => {
      wx.hideLoading();
      if (res.code === 200) {
        const coupons = res.data || [];
        this.setData({
          coupons: coupons
        });
        this.updateCounts();
      } else {
        wx.showToast({
          title: res.message || '加载失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.hideLoading();
      console.error('加载优惠券失败:', err);
      wx.showToast({
        title: '加载失败',
        icon: 'none'
      });
    });
  },

  // 更新各状态数量
  updateCounts() {
    // 这里应该调用API获取各状态的数量
    // 暂时使用模拟数据
    this.setData({
      availableCount: this.data.coupons.filter(c => c.status === 0).length,
      usedCount: this.data.coupons.filter(c => c.status === 1).length,
      expiredCount: this.data.coupons.filter(c => c.status === 2).length
    });
  },

  // 使用优惠券
  useCoupon(e) {
    const coupon = e.currentTarget.dataset.coupon;
    
    if (coupon.status !== 0) {
      wx.showToast({
        title: '优惠券不可用',
        icon: 'none'
      });
      return;
    }

    // 跳转到菜单页面选择商品
    wx.switchTab({
      url: '/pages/menu/menu'
    });
  },

  // 跳转到领券中心
  goToCouponCenter() {
    wx.navigateTo({
      url: '/pages/coupon-center/coupon-center'
    });
  }
}); 