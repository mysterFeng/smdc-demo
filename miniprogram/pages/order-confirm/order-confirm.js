const { api } = require('../../utils/api.js');

Page({
  data: {
    orderData: null,
    selectedAddress: null,
    paymentMethod: 'wechat', // 默认微信支付
    paymentMethods: [
      { id: 'wechat', name: '微信支付', icon: '/images/payment/wechat.png' },
      { id: 'alipay', name: '支付宝', icon: '/images/payment/alipay.png' }
    ],
    isLoading: false,
    selectedCoupon: null, // 选中的优惠券
    availableCoupons: [], // 可用优惠券列表
    showCouponModal: false // 是否显示优惠券选择弹窗
  },

  onLoad(options) {
    // 从购物车页面传递过来的订单数据
    if (options.orderData) {
      try {
        const orderData = JSON.parse(decodeURIComponent(options.orderData));
        this.setData({ orderData });
        this.loadDefaultAddress();
        this.loadAvailableCoupons(); // 加载可用优惠券
      } catch (error) {
        console.error('解析订单数据失败:', error);
        wx.showToast({
          title: '订单数据错误',
          icon: 'none'
        });
        wx.navigateBack();
      }
    } else {
      wx.showToast({
        title: '订单数据缺失',
        icon: 'none'
      });
      wx.navigateBack();
    }
  },

  // 加载默认地址
  loadDefaultAddress() {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo || !userInfo.id) {
      return;
    }

    // 这里应该调用地址API获取默认地址
    // 暂时使用模拟数据
    const mockAddress = {
      id: 1,
      name: '张三',
      phone: '13800000001',
      address: '北京市朝阳区xxx街道xxx号',
      isDefault: true
    };

    this.setData({ selectedAddress: mockAddress });
  },

  // 加载可用优惠券
  loadAvailableCoupons() {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo || !userInfo.id) {
      return;
    }

    const orderAmount = this.data.orderData.totalAmount;
    
    api.getAvailableUserCoupons(userInfo.id, orderAmount).then(res => {
      if (res.code === 200) {
        this.setData({
          availableCoupons: res.data || []
        });
      }
    }).catch(err => {
      console.error('加载可用优惠券失败:', err);
    });
  },

  // 选择地址
  selectAddress() {
    wx.navigateTo({
      url: '/pages/address/address?select=true'
    });
  },

  // 选择支付方式
  selectPaymentMethod(e) {
    const method = e.currentTarget.dataset.method;
    this.setData({ paymentMethod: method });
  },

  // 显示优惠券选择弹窗
  showCouponSelector() {
    this.setData({ showCouponModal: true });
  },

  // 隐藏优惠券选择弹窗
  hideCouponSelector() {
    this.setData({ showCouponModal: false });
  },

  // 选择优惠券
  selectCoupon(e) {
    const coupon = e.currentTarget.dataset.coupon;
    this.setData({
      selectedCoupon: coupon,
      showCouponModal: false
    });
  },

  // 取消选择优惠券
  cancelCoupon() {
    this.setData({ selectedCoupon: null });
  },

  // 提交订单
  submitOrder() {
    if (this.data.isLoading) return;

    if (!this.data.selectedAddress) {
      wx.showToast({
        title: '请选择配送地址',
        icon: 'none'
      });
      return;
    }

    this.setData({ isLoading: true });
    wx.showLoading({ title: '创建订单中...' });

    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo || !userInfo.id) {
      wx.hideLoading();
      this.setData({ isLoading: false });
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      });
      return;
    }

    // 构建订单数据
    const orderCreateData = {
      userId: userInfo.id,
      receiverName: this.data.selectedAddress.name,
      receiverPhone: this.data.selectedAddress.phone,
      receiverAddress: this.data.selectedAddress.address,
      remark: this.data.orderData.remark || '',
      paymentMethod: this.data.paymentMethod
    };

    // 调用创建订单API
    api.createOrder(orderCreateData).then(res => {
      if (res.code === 200) {
        const order = res.data;
        
        // 如果选择了优惠券，使用优惠券
        if (this.data.selectedCoupon) {
          this.useCoupon(order.id, userInfo.id);
        } else {
          this.handleOrderSuccess(order);
        }
      } else {
        wx.hideLoading();
        this.setData({ isLoading: false });
        wx.showToast({
          title: res.message || '创建订单失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      console.error('创建订单失败:', err);
      wx.hideLoading();
      this.setData({ isLoading: false });
      
      wx.showToast({
        title: err.message || '创建订单失败',
        icon: 'none'
      });
    });
  },

  // 使用优惠券
  useCoupon(orderId, userId) {
    const selectedCoupon = this.data.selectedCoupon;
    
    api.useCoupon(userId, selectedCoupon.id, orderId).then(res => {
      if (res.code === 200) {
        wx.showToast({
          title: '优惠券使用成功',
          icon: 'success'
        });
        this.handleOrderSuccess({ id: orderId });
      } else {
        wx.hideLoading();
        this.setData({ isLoading: false });
        wx.showToast({
          title: res.message || '使用优惠券失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      console.error('使用优惠券失败:', err);
      wx.hideLoading();
      this.setData({ isLoading: false });
      wx.showToast({
        title: '使用优惠券失败',
        icon: 'none'
      });
    });
  },

  // 处理订单创建成功
  handleOrderSuccess(order) {
    wx.hideLoading();
    this.setData({ isLoading: false });
    
    wx.showToast({
      title: '订单创建成功',
      icon: 'success'
    });

    // 清空购物车
    this.clearCart();

    // 跳转到支付页面或订单详情页
    setTimeout(() => {
      wx.redirectTo({
        url: `/pages/order-detail/order-detail?id=${order.id}`
      });
    }, 1500);
  },

  // 清空购物车
  clearCart() {
    const userInfo = wx.getStorageSync('userInfo');
    if (!userInfo || !userInfo.id) return;

    api.clearCart(userInfo.id).then(res => {
      console.log('清空购物车成功:', res);
    }).catch(err => {
      console.error('清空购物车失败:', err);
    });
  },

  // 返回购物车
  goBack() {
    wx.navigateBack();
  },

  // 阻止事件冒泡
  stopPropagation() {
    // 空方法，用于阻止事件冒泡
  }
}); 