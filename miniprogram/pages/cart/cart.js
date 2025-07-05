Page({
  data: {
    cartItems: [],
    selectedCoupon: null,
    selectedAddress: null,
    remark: '',
    totalAmount: 0,
    discountAmount: 0,
    deliveryFee: 5,
    actualAmount: 0,
    availableCoupons: [
      {
        id: 1,
        name: '满30减5',
        type: 'discount',
        value: 5,
        minAmount: 30
      },
      {
        id: 2,
        name: '满50减10',
        type: 'discount',
        value: 10,
        minAmount: 50
      }
    ]
  },

  onLoad() {
    this.loadCartData();
  },

  onShow() {
    this.loadCartData();
    this.calculatePrice();
  },

  // 加载购物车数据
  loadCartData() {
    // 从本地存储或全局状态获取购物车数据
    const cartItems = wx.getStorageSync('cartItems') || [];
    this.setData({ cartItems });
  },

  // 保存购物车数据
  saveCartData() {
    wx.setStorageSync('cartItems', this.data.cartItems);
  },

  // 增加商品数量
  increaseQuantity(e) {
    const itemId = e.currentTarget.dataset.id;
    const cartItems = this.data.cartItems.map(item => {
      if (item.id === itemId) {
        return { ...item, quantity: item.quantity + 1 };
      }
      return item;
    });
    
    this.setData({ cartItems });
    this.saveCartData();
    this.calculatePrice();
  },

  // 减少商品数量
  decreaseQuantity(e) {
    const itemId = e.currentTarget.dataset.id;
    const cartItems = this.data.cartItems.map(item => {
      if (item.id === itemId && item.quantity > 1) {
        return { ...item, quantity: item.quantity - 1 };
      }
      return item;
    });
    
    this.setData({ cartItems });
    this.saveCartData();
    this.calculatePrice();
  },

  // 删除商品
  removeItem(e) {
    const itemId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这个商品吗？',
      success: (res) => {
        if (res.confirm) {
          const cartItems = this.data.cartItems.filter(item => item.id !== itemId);
          this.setData({ cartItems });
          this.saveCartData();
          this.calculatePrice();
          
          wx.showToast({
            title: '删除成功',
            icon: 'success'
          });
        }
      }
    });
  },

  // 计算价格
  calculatePrice() {
    const totalAmount = this.data.cartItems.reduce((total, item) => {
      return total + (item.price * item.quantity);
    }, 0);

    let discountAmount = 0;
    if (this.data.selectedCoupon && totalAmount >= this.data.selectedCoupon.minAmount) {
      discountAmount = this.data.selectedCoupon.value;
    }

    const actualAmount = totalAmount - discountAmount + this.data.deliveryFee;

    this.setData({
      totalAmount: totalAmount.toFixed(2),
      discountAmount: discountAmount.toFixed(2),
      actualAmount: actualAmount.toFixed(2)
    });
  },

  // 选择优惠券
  selectCoupon() {
    const coupons = this.data.availableCoupons;
    const currentAmount = parseFloat(this.data.totalAmount);
    
    const availableCoupons = coupons.filter(coupon => currentAmount >= coupon.minAmount);
    
    if (availableCoupons.length === 0) {
      wx.showToast({
        title: '暂无可用优惠券',
        icon: 'none'
      });
      return;
    }

    wx.showActionSheet({
      itemList: availableCoupons.map(coupon => coupon.name),
      success: (res) => {
        const selectedCoupon = availableCoupons[res.tapIndex];
        this.setData({ selectedCoupon });
        this.calculatePrice();
      }
    });
  },

  // 选择地址
  selectAddress() {
    // 跳转到地址选择页面
    wx.navigateTo({
      url: '/pages/address/address'
    });
  },

  // 备注输入
  onRemarkInput(e) {
    this.setData({
      remark: e.detail.value
    });
  },

  // 跳转到菜单页面
  goToMenu() {
    wx.switchTab({
      url: '/pages/menu/menu'
    });
  },

  // 结算
  checkout() {
    if (this.data.cartItems.length === 0) {
      wx.showToast({
        title: '购物车为空',
        icon: 'none'
      });
      return;
    }

    if (!this.data.selectedAddress) {
      wx.showToast({
        title: '请选择配送地址',
        icon: 'none'
      });
      return;
    }

    // 创建订单数据
    const orderData = {
      items: this.data.cartItems,
      totalAmount: this.data.totalAmount,
      discountAmount: this.data.discountAmount,
      deliveryFee: this.data.deliveryFee,
      actualAmount: this.data.actualAmount,
      coupon: this.data.selectedCoupon,
      address: this.data.selectedAddress,
      remark: this.data.remark
    };

    // 跳转到订单确认页面
    wx.navigateTo({
      url: `/pages/order/order?orderData=${encodeURIComponent(JSON.stringify(orderData))}`
    });
  }
}); 