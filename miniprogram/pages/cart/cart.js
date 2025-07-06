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
    // 获取当前用户ID（这里暂时使用固定值，后续从登录信息获取）
    const userId = wx.getStorageSync('userId') || 2;
    
    const { api } = require('../../utils/api.js');
    
    // 从后端获取购物车数据
    api.getUserCart(userId).then(res => {
      console.log('获取购物车成功:', res);
      if (res.code === 200) {
        this.setData({ 
          cartItems: res.data.items || [],
          totalAmount: res.data.totalAmount || 0,
          selectedTotalAmount: res.data.selectedTotalAmount || 0
        });
        this.calculatePrice();
      }
    }).catch(err => {
      console.error('获取购物车失败:', err);
      wx.showToast({
        title: '获取购物车失败',
        icon: 'none'
      });
    });
  },

  // 保存购物车数据
  saveCartData() {
    wx.setStorageSync('cartItems', this.data.cartItems);
  },

  // 增加商品数量
  increaseQuantity(e) {
    const itemId = e.currentTarget.dataset.id;
    const cartItem = this.data.cartItems.find(item => item.id === itemId);
    
    if (!cartItem) return;
    
    const userId = wx.getStorageSync('userId') || 2;
    const { api } = require('../../utils/api.js');
    
    // 调用后端API更新购物车项目
    api.updateCartItem(userId, {
      cartItemId: itemId,
      quantity: cartItem.quantity + 1
    }).then(res => {
      console.log('更新购物车项目成功:', res);
      // 重新加载购物车数据
      this.loadCartData();
    }).catch(err => {
      console.error('更新购物车项目失败:', err);
      wx.showToast({
        title: err.message || '更新失败',
        icon: 'none'
      });
    });
  },

  // 减少商品数量
  decreaseQuantity(e) {
    const itemId = e.currentTarget.dataset.id;
    const cartItem = this.data.cartItems.find(item => item.id === itemId);
    
    if (!cartItem || cartItem.quantity <= 1) return;
    
    const userId = wx.getStorageSync('userId') || 2;
    const { api } = require('../../utils/api.js');
    
    // 调用后端API更新购物车项目
    api.updateCartItem(userId, {
      cartItemId: itemId,
      quantity: cartItem.quantity - 1
    }).then(res => {
      console.log('更新购物车项目成功:', res);
      // 重新加载购物车数据
      this.loadCartData();
    }).catch(err => {
      console.error('更新购物车项目失败:', err);
      wx.showToast({
        title: err.message || '更新失败',
        icon: 'none'
      });
    });
  },

  // 删除商品
  removeItem(e) {
    const itemId = e.currentTarget.dataset.id;
    const userId = wx.getStorageSync('userId') || 2;
    
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这个商品吗？',
      success: (res) => {
        if (res.confirm) {
          const { api } = require('../../utils/api.js');
          
          // 调用后端API删除购物车项目
          api.removeCartItem(userId, itemId).then(res => {
            console.log('删除购物车项目成功:', res);
            
            // 重新加载购物车数据
            this.loadCartData();
            
            wx.showToast({
              title: '删除成功',
              icon: 'success'
            });
          }).catch(err => {
            console.error('删除购物车项目失败:', err);
            wx.showToast({
              title: err.message || '删除失败',
              icon: 'none'
            });
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