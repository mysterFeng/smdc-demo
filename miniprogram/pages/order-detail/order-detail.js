Page({
  data: {
    orderId: null,
    orderInfo: {}
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ orderId: options.id });
      this.loadOrderDetail(options.id);
    }
  },

  // 加载订单详情
  loadOrderDetail(orderId) {
    // 模拟订单详情数据
    const mockOrderDetail = {
      id: orderId,
      orderNo: 'ORDER20240101001',
      status: 1,
      statusText: '待付款',
      statusDesc: '请在30分钟内完成支付',
      dishes: [
        {
          id: 1,
          name: '宫保鸡丁',
          description: '经典川菜，鸡肉鲜嫩，花生香脆',
          imageUrl: '/images/dishes/gongbao-chicken.jpg',
          price: 28.00,
          quantity: 1
        },
        {
          id: 2,
          name: '麻婆豆腐',
          description: '麻辣鲜香，豆腐嫩滑',
          imageUrl: '/images/dishes/mapo-tofu.jpg',
          price: 18.00,
          quantity: 2
        }
      ],
      totalAmount: 64.00,
      discountAmount: 5.00,
      deliveryFee: 5.00,
      actualAmount: 64.00,
      deliveryName: '张三',
      deliveryPhone: '138****8888',
      deliveryAddress: '北京市朝阳区某某街道某某小区1号楼101室',
      createTime: '2024-01-01 12:30:00',
      paymentTime: '',
      remark: '请尽快配送'
    };

    this.setData({ orderInfo: mockOrderDetail });
  },

  // 取消订单
  cancelOrder() {
    wx.showModal({
      title: '确认取消',
      content: '确定要取消这个订单吗？',
      success: (res) => {
        if (res.confirm) {
          // 这里应该调用API取消订单
          wx.showToast({
            title: '订单已取消',
            icon: 'success'
          });
          
          // 更新订单状态
          this.setData({
            'orderInfo.status': 0,
            'orderInfo.statusText': '已取消',
            'orderInfo.statusDesc': '订单已取消'
          });
        }
      }
    });
  },

  // 支付订单
  payOrder() {
    wx.showLoading({
      title: '支付中...'
    });

    // 模拟支付过程
    setTimeout(() => {
      wx.hideLoading();
      
      // 模拟支付成功
      this.setData({
        'orderInfo.status': 2,
        'orderInfo.statusText': '待收货',
        'orderInfo.statusDesc': '商家正在为您准备美食',
        'orderInfo.paymentTime': new Date().toLocaleString()
      });

      wx.showToast({
        title: '支付成功',
        icon: 'success'
      });
    }, 2000);
  },

  // 确认收货
  confirmReceive() {
    wx.showModal({
      title: '确认收货',
      content: '确认已收到商品吗？',
      success: (res) => {
        if (res.confirm) {
          // 这里应该调用API确认收货
          wx.showToast({
            title: '确认收货成功',
            icon: 'success'
          });
          
          // 更新订单状态
          this.setData({
            'orderInfo.status': 3,
            'orderInfo.statusText': '待评价',
            'orderInfo.statusDesc': '请为本次服务打分'
          });
        }
      }
    });
  },

  // 跳转到评价页面
  goToReview() {
    wx.navigateTo({
      url: `/pages/review/review?orderId=${this.data.orderId}`
    });
  },

  // 再来一单
  reorder() {
    // 将订单中的商品重新加入购物车
    const dishes = this.data.orderInfo.dishes.map(dish => ({
      ...dish,
      quantity: dish.quantity
    }));
    
    // 保存到购物车
    wx.setStorageSync('cartItems', dishes);
    
    wx.showToast({
      title: '已加入购物车',
      icon: 'success'
    });
    
    // 跳转到购物车页面
    setTimeout(() => {
      wx.switchTab({
        url: '/pages/cart/cart'
      });
    }, 1500);
  }
}); 