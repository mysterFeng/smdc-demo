const { api } = require('../../utils/api');

Page({
  data: {
    cartItems: [],
    totalAmount: 0,
    deliveryFee: 0,
    discountAmount: 0,
    actualAmount: 0,
    addressList: [],
    selectedAddress: null,
    allCoupons: [],
    availableCoupons: [],
    selectedCoupon: null,
    remark: '',
    showCouponModal: false,
    userId: 2 // TODO: 实际项目应从登录态获取
  },

  onShow() {
    this.loadCart();
    this.loadAddresses();
    this.loadCoupons();
  },

  // 加载购物车数据（接口）
  loadCart() {
    api.getUserCartItems(this.data.userId).then(res => {
      if (res.code === 200) {
        const cartItems = res.data || [];
        console.log('购物车数据:', cartItems);
        this.setData({ cartItems });
        this.calculateTotal();
      }
    }).catch(err => {
      console.error('加载购物车失败:', err);
      wx.showToast({ title: '加载购物车失败', icon: 'none' });
    });
  },

  // 加载用户地址（接口）
  loadAddresses() {
    api.getUserAddresses(this.data.userId).then(res => {
      if (res.code === 200) {
        const addressList = res.data || [];
        let selectedAddress = addressList.find(a => a.isDefault === 1) || addressList[0] || null;
        this.setData({ addressList, selectedAddress });
      }
    });
  },

  // 加载用户优惠券（接口）
  loadCoupons() {
    api.getUserCoupons(this.data.userId).then(res => {
      if (res.code === 200) {
        this.setData({ allCoupons: res.data || [] });
      }
    });
  },

  // 增加商品数量
  increaseQuantity(e) {
    const id = e.currentTarget.dataset.id;
    const item = this.data.cartItems.find(item => item.id === id);
    if (!item) return;
    
    api.updateCartItem(this.data.userId, {
      cartItemId: id,
      quantity: item.quantity + 1
    }).then(res => {
      if (res.code === 200) {
        this.loadCart(); // 重新加载购物车数据
      } else {
        wx.showToast({ title: res.message || '操作失败', icon: 'none' });
      }
    }).catch(() => {
      wx.showToast({ title: '操作失败', icon: 'none' });
    });
  },

  // 减少商品数量
  decreaseQuantity(e) {
    const id = e.currentTarget.dataset.id;
    const item = this.data.cartItems.find(item => item.id === id);
    if (!item || item.quantity <= 1) return;
    
    api.updateCartItem(this.data.userId, {
      cartItemId: id,
      quantity: item.quantity - 1
    }).then(res => {
      if (res.code === 200) {
        this.loadCart(); // 重新加载购物车数据
      } else {
        wx.showToast({ title: res.message || '操作失败', icon: 'none' });
      }
    }).catch(() => {
      wx.showToast({ title: '操作失败', icon: 'none' });
    });
  },

  // 删除商品
  removeItem(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这个商品吗？',
      success: (res) => {
        if (res.confirm) {
          api.removeCartItem(this.data.userId, id).then(res => {
            if (res.code === 200) {
              wx.showToast({ title: '删除成功', icon: 'success' });
              this.loadCart(); // 重新加载购物车数据
            } else {
              wx.showToast({ title: res.message || '删除失败', icon: 'none' });
            }
          }).catch(() => {
            wx.showToast({ title: '删除失败', icon: 'none' });
          });
        }
      }
    });
  },

  // 跳转到菜单页
  goToMenu() {
    wx.switchTab({
      url: '/pages/menu/menu'
    });
  },

  // 选择地址
  selectAddress() {
    wx.navigateTo({
      url: '/pages/address/address?select=true'
    });
  },

  // 选择优惠券
  selectCoupon() {
    const coupons = this.data.allCoupons;
    const currentAmount = parseFloat(this.data.totalAmount);
    // 过滤可用优惠券：未使用且未过期且满足金额条件
    const availableCoupons = coupons.filter(coupon => 
      coupon.status === 0 && // 未使用
      coupon.isAvailable && // 可用（未过期）
      currentAmount >= coupon.couponMinAmount // 满足金额条件
    );
    
    console.log('当前金额:', currentAmount);
    console.log('所有优惠券:', coupons);
    console.log('可用优惠券:', availableCoupons);
    
    if (availableCoupons.length === 0) {
      wx.showToast({ title: '暂无可用优惠券', icon: 'none' });
      return;
    }
    this.setData({ showCouponModal: true, availableCoupons });
  },

  // 确认选择优惠券
  onCouponSelect(e) {
    const coupon = e.currentTarget.dataset.coupon;
    console.log('选中的优惠券:', coupon);
    this.setData({ selectedCoupon: coupon, showCouponModal: false });
    this.calculateTotal();
  },

  // 关闭优惠券弹窗
  closeCouponModal() {
    this.setData({ showCouponModal: false });
  },

  // 清除选中的优惠券
  clearSelectedCoupon() {
    this.setData({ selectedCoupon: null });
    this.calculateTotal();
  },

  // 阻止事件冒泡
  stopPropagation() {
    // 空方法，用于阻止事件冒泡
  },

  // 备注输入
  onRemarkInput(e) {
    this.setData({ remark: e.detail.value });
  },

  // 金额结算（前端预估，实际以后端为准）
  calculateTotal() {
    const cartItems = this.data.cartItems;
    let total = cartItems.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0);
    
    // 配送费（固定5元，实际项目中可以根据距离计算）
    const deliveryFee = total > 0 ? 5 : 0;
    
    // 优惠金额
    let discountAmount = 0;
    const coupon = this.data.selectedCoupon;
    if (coupon && coupon.isAvailable && total >= coupon.couponMinAmount) {
      discountAmount = coupon.couponValue;
    }
    
    // 实付金额 = 商品总额 - 优惠金额 + 配送费
    const actualAmount = Math.max(0, total - discountAmount + deliveryFee);
    
    console.log('价格计算:', {
      total,
      deliveryFee,
      discountAmount,
      actualAmount,
      coupon: coupon ? coupon.couponName : '无'
    });
    
    this.setData({ 
      totalAmount: total,
      deliveryFee: deliveryFee,
      discountAmount: discountAmount,
      actualAmount: actualAmount
    });
  },

  // 提交订单
  submitOrder() {
    const { cartItems, selectedAddress, selectedCoupon, remark, userId } = this.data;
    if (!selectedAddress) {
      wx.showToast({ title: '请选择收货地址', icon: 'none' });
      return;
    }
    if (cartItems.length === 0) {
      wx.showToast({ title: '购物车为空', icon: 'none' });
      return;
    }
    
    // 构造下单参数 - 按照后端OrderCreateDTO的格式
    const orderData = {
      userId: userId,
      receiverName: selectedAddress.name,
      receiverPhone: selectedAddress.phone,
      receiverAddress: `${selectedAddress.province}${selectedAddress.city}${selectedAddress.district}${selectedAddress.detail}`,
      remark: remark || '',
      paymentMethod: 'WECHAT' // 默认微信支付
    };
    
    console.log('提交订单数据:', orderData);
    
    wx.showLoading({ title: '正在下单...' });
    api.createOrder(orderData).then(res => {
      wx.hideLoading();
      if (res.code === 200) {
        wx.showToast({ title: '下单成功', icon: 'success' });
        // 跳转到订单详情页
        wx.redirectTo({ url: `/pages/order-detail/order-detail?id=${res.data.id}` });
      } else {
        wx.showToast({ title: res.message || '下单失败', icon: 'none' });
      }
    }).catch((err) => {
      wx.hideLoading();
      console.error('下单失败:', err);
      wx.showToast({ title: '下单失败', icon: 'none' });
    });
  }
}); 