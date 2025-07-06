const { api } = require('../../utils/api.js');

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
    // 检查全局标记，决定是否强制刷新
    if (getApp().globalData && getApp().globalData.needRefreshCoupons) {
      this.loadCoupons();
      getApp().globalData.needRefreshCoupons = false;
    } else {
      this.loadCoupons();
    }
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
        // 处理后端返回的数据格式
        const processedCoupons = coupons.map(coupon => ({
          id: coupon.id,
          couponName: coupon.couponName,
          couponValue: coupon.couponValue,
          couponMinAmount: coupon.couponMinAmount,
          couponDescription: coupon.couponDescription,
          status: coupon.status,
          statusDesc: coupon.statusDesc,
          receivedAt: this.formatDate(coupon.receivedAt),
          expiredAt: this.formatDate(coupon.expiredAt),
          isAvailable: coupon.isAvailable
        }));
        
        this.setData({
          coupons: processedCoupons
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
      // 模拟数据（因为后端接口可能还未实现）
      const mockCoupons = [
        {
          id: 1,
          couponName: '满30减5',
          couponValue: 5,
          couponMinAmount: 30,
          couponDescription: '满30元可用，立减5元',
          status: 0,
          statusDesc: '未使用',
          receivedAt: '2025-07-06',
          expiredAt: '2025-12-31',
          isAvailable: true
        },
        {
          id: 2,
          couponName: '满50减10',
          couponValue: 10,
          couponMinAmount: 50,
          couponDescription: '满50元可用，立减10元',
          status: 1,
          statusDesc: '已使用',
          receivedAt: '2025-07-05',
          expiredAt: '2025-12-31',
          isAvailable: false
        },
        {
          id: 3,
          couponName: '满20减3',
          couponValue: 3,
          couponMinAmount: 20,
          couponDescription: '满20元可用，立减3元',
          status: 2,
          statusDesc: '已过期',
          receivedAt: '2025-06-01',
          expiredAt: '2025-06-30',
          isAvailable: false
        }
      ];
      
      // 根据当前状态过滤
      const filteredCoupons = mockCoupons.filter(c => c.status === status);
      
      this.setData({
        coupons: filteredCoupons
      });
      this.updateCounts();
    });
  },

  // 格式化日期
  formatDate(dateString) {
    if (!dateString) return '';
    const date = new Date(dateString);
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
  },

  // 更新各状态数量
  updateCounts() {
    // 获取所有优惠券数据来计算各状态数量
    api.getUserCoupons(this.data.userId).then(res => {
      if (res.code === 200) {
        const allCoupons = res.data || [];
        this.setData({
          availableCount: allCoupons.filter(c => c.status === 0 && c.isAvailable).length,
          usedCount: allCoupons.filter(c => c.status === 1).length,
          expiredCount: allCoupons.filter(c => c.status === 2 || (c.status === 0 && !c.isAvailable)).length
        });
      }
    }).catch(err => {
      console.error('获取优惠券统计失败:', err);
      // 使用当前页面的数据作为备选
      const coupons = this.data.coupons;
      this.setData({
        availableCount: coupons.filter(c => c.status === 0).length,
        usedCount: coupons.filter(c => c.status === 1).length,
        expiredCount: coupons.filter(c => c.status === 2).length
      });
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

    // 跳转到购物车页面，让用户选择商品后使用优惠券
    wx.switchTab({
      url: '/pages/cart/cart'
    });
    
    // 设置全局标记，订单确认后需要刷新优惠券列表
    getApp().globalData.selectedCoupon = coupon;
    getApp().globalData.needRefreshCoupons = true;
  },

  // 跳转到领券中心
  goToCouponCenter() {
    wx.navigateTo({
      url: '/pages/coupon-center/coupon-center'
    });
  }
}); 