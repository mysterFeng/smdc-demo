const { api } = require('../../utils/api.js');

Page({
  data: {
    orderId: null,
    orderInfo: {},
    isLoading: true
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ orderId: options.id });
      this.loadOrderDetail(options.id);
    } else {
      wx.showToast({
        title: '订单ID缺失',
        icon: 'none'
      });
      wx.navigateBack();
    }
  },

  // 加载订单详情
  loadOrderDetail(orderId) {
    wx.showLoading({ title: '加载中...' });
    
    api.getOrderById(orderId).then(res => {
      wx.hideLoading();
      if (res.code === 200) {
        const orderData = res.data;
        
        // 转换后端数据格式为前端需要的格式
        const orderInfo = {
          id: orderData.id,
          orderNo: orderData.orderNo,
          status: this.getStatusValue(orderData.status),
          statusText: this.getStatusText(orderData.status),
          statusDesc: this.getStatusDesc(orderData.status),
          dishes: orderData.orderItems ? orderData.orderItems.map(item => ({
            id: item.dishId,
            name: item.dishName,
            description: item.remark || '',
            imageUrl: item.dishImageUrl || '/images/default-dish.png',
            price: item.unitPrice,
            quantity: item.quantity
          })) : [],
          totalAmount: orderData.totalAmount,
          discountAmount: 0, // 暂时设为0，后续可以从优惠券信息中获取
          deliveryFee: 5, // 固定配送费
          actualAmount: orderData.paidAmount || orderData.totalAmount,
          deliveryName: orderData.receiverName,
          deliveryPhone: orderData.receiverPhone,
          deliveryAddress: orderData.receiverAddress,
          createTime: this.formatTime(orderData.createdAt),
          paymentTime: orderData.paidTime ? this.formatTime(orderData.paidTime) : '',
          remark: orderData.remark || ''
        };

        this.setData({ 
          orderInfo: orderInfo,
          isLoading: false
        });
      } else {
        wx.showToast({
          title: res.message || '加载订单详情失败',
          icon: 'none'
        });
        wx.navigateBack();
      }
    }).catch(err => {
      wx.hideLoading();
      console.error('加载订单详情失败:', err);
      wx.showToast({
        title: '加载订单详情失败',
        icon: 'none'
      });
      wx.navigateBack();
    });
  },

  // 获取状态值
  getStatusValue(status) {
    const statusMap = {
      'PENDING_PAYMENT': 1,
      'PAID': 2,
      'PREPARING': 2,
      'READY': 2,
      'COMPLETED': 3,
      'CANCELLED': 0
    };
    return statusMap[status] || 1;
  },

  // 获取状态文本
  getStatusText(status) {
    const statusMap = {
      'PENDING_PAYMENT': '待付款',
      'PAID': '已支付',
      'PREPARING': '制作中',
      'READY': '待取餐',
      'COMPLETED': '已完成',
      'CANCELLED': '已取消'
    };
    return statusMap[status] || '未知状态';
  },

  // 获取状态描述
  getStatusDesc(status) {
    const descMap = {
      'PENDING_PAYMENT': '请在30分钟内完成支付',
      'PAID': '商家正在为您准备美食',
      'PREPARING': '商家正在为您准备美食',
      'READY': '美食已准备完成，请及时取餐',
      'COMPLETED': '订单已完成，感谢您的使用',
      'CANCELLED': '订单已取消'
    };
    return descMap[status] || '订单处理中';
  },

  // 格式化时间
  formatTime(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr);
    return date.toLocaleString('zh-CN');
  },

  // 取消订单
  cancelOrder() {
    wx.showModal({
      title: '确认取消',
      content: '确定要取消这个订单吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '取消中...' });
          
          api.cancelOrder(this.data.orderId, 2).then(res => { // 用户ID暂时写死为2
            wx.hideLoading();
            if (res.code === 200) {
              wx.showToast({
                title: '订单已取消',
                icon: 'success'
              });
              
              // 重新加载订单详情
              this.loadOrderDetail(this.data.orderId);
            } else {
              wx.showToast({
                title: res.message || '取消订单失败',
                icon: 'none'
              });
            }
          }).catch(err => {
            wx.hideLoading();
            console.error('取消订单失败:', err);
            wx.showToast({
              title: '取消订单失败',
              icon: 'none'
            });
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

    // 调用支付API
    api.payOrder(this.data.orderId, 2, 'WECHAT').then(res => { // 用户ID暂时写死为2
      wx.hideLoading();
      if (res.code === 200) {
        wx.showToast({
          title: '支付成功',
          icon: 'success'
        });
        
        // 重新加载订单详情
        this.loadOrderDetail(this.data.orderId);
      } else {
        wx.showToast({
          title: res.message || '支付失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.hideLoading();
      console.error('支付失败:', err);
      wx.showToast({
        title: '支付失败',
        icon: 'none'
      });
    });
  },

  // 确认收货
  confirmReceive() {
    wx.showModal({
      title: '确认收货',
      content: '确认已收到商品吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '确认收货中...' });
          
          // 调用后端API确认收货
          api.confirmReceive(this.data.orderId).then(res => {
            wx.hideLoading();
            if (res.code === 200) {
              wx.showToast({
                title: '确认收货成功',
                icon: 'success'
              });
              
              // 更新本地订单状态
              this.setData({
                'orderInfo.status': 4,
                'orderInfo.statusText': '已完成',
                'orderInfo.statusDesc': '订单已完成，感谢您的使用'
              });
            } else {
              wx.showToast({
                title: res.message || '确认收货失败',
                icon: 'none'
              });
            }
          }).catch(err => {
            wx.hideLoading();
            console.error('确认收货失败:', err);
            wx.showToast({
              title: '确认收货失败',
              icon: 'none'
            });
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
    const dishes = this.data.orderInfo.dishes;
    
    // 调用API将商品加入购物车
    const promises = dishes.map(dish => 
      api.addToCart(2, { // 用户ID暂时写死为2
        dishId: dish.id,
        quantity: dish.quantity,
        remark: dish.description
      })
    );
    
    Promise.all(promises).then(() => {
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
    }).catch(err => {
      console.error('加入购物车失败:', err);
      wx.showToast({
        title: '加入购物车失败',
        icon: 'none'
      });
    });
  }
}); 