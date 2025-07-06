const { api } = require('../../utils/api.js');

Page({
  data: {
    currentStatus: 0, // 0: 全部, 1: 待付款, 2: 已付款, 3: 制作中, 4: 已完成
    statusTabs: [
      { label: '全部', value: 0 },
      { label: '待付款', value: 1 },
      { label: '已付款', value: 2 },
      { label: '制作中', value: 3 },
      { label: '已完成', value: 4 }
    ],
    orders: [],
    isLoading: false,
    userId: 2 // TODO: 实际项目应从登录态获取
  },

  onLoad() {
    this.loadOrders();
  },

  onShow() {
    this.loadOrders();
  },

  // 加载订单数据
  loadOrders() {
    this.setData({ isLoading: true });
    
    // 根据当前状态获取订单
    if (this.data.currentStatus === 0) {
      // 获取所有订单
      api.getUserOrders(this.data.userId, 0, 20).then(res => {
        this.setData({ isLoading: false });
        if (res.code === 200) {
          const orders = this.processOrderData(res.data.content);
          this.setData({ orders });
        } else {
          wx.showToast({
            title: res.message || '获取订单失败',
            icon: 'none'
          });
        }
      }).catch(err => {
        this.setData({ isLoading: false });
        console.error('获取订单列表失败:', err);
        wx.showToast({
          title: '获取订单失败',
          icon: 'none'
        });
      });
    } else {
      // 根据状态获取订单
      const status = this.getStatusString(this.data.currentStatus);
      if (status) {
        api.getUserOrdersByStatus(this.data.userId, status, 0, 20).then(res => {
          this.setData({ isLoading: false });
          if (res.code === 200) {
            const orders = this.processOrderData(res.data.content);
            this.setData({ orders });
          } else {
            wx.showToast({
              title: res.message || '获取订单失败',
              icon: 'none'
            });
          }
        }).catch(err => {
          this.setData({ isLoading: false });
          console.error('获取订单列表失败:', err);
          wx.showToast({
            title: '获取订单失败',
            icon: 'none'
          });
        });
      } else {
        this.setData({ isLoading: false });
        // 对于"制作中"状态，需要查询多个状态
        if (this.data.currentStatus === 3) {
          this.loadPreparingOrders();
        } else {
          this.setData({ orders: [] });
        }
      }
    }
  },

  // 加载制作中的订单（包括已付款、制作中、待取餐状态）
  loadPreparingOrders() {
    const statuses = ['PAID', 'PREPARING', 'READY'];
    const promises = statuses.map(status => 
      api.getUserOrdersByStatus(this.data.userId, status, 0, 20)
    );
    
    Promise.all(promises).then(responses => {
      let allOrders = [];
      responses.forEach(res => {
        if (res.code === 200 && res.data.content) {
          allOrders = allOrders.concat(res.data.content);
        }
      });
      
      // 按创建时间排序
      allOrders.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
      
      const orders = this.processOrderData(allOrders);
      this.setData({ orders });
    }).catch(err => {
      console.error('获取制作中订单失败:', err);
      wx.showToast({
        title: '获取订单失败',
        icon: 'none'
      });
    });
  },

  // 处理订单数据
  processOrderData(orders) {
    return orders.map(order => {
      // 计算商品总数
      const totalCount = order.orderItems ? order.orderItems.reduce((sum, item) => sum + item.quantity, 0) : 0;
      
      // 获取前几个商品用于显示
      const dishes = order.orderItems ? order.orderItems.slice(0, 3).map(item => ({
        id: item.dishId,
        name: item.dishName,
        imageUrl: item.dishImageUrl || '/images/default-dish.png',
        price: item.unitPrice,
        quantity: item.quantity
      })) : [];

      return {
        ...order,
        statusText: this.getStatusText(order.status),
        statusClass: this.getStatusClass(order.status),
        totalCount: totalCount,
        dishes: dishes,
        actualAmount: order.paidAmount || order.totalAmount,
        createTime: this.formatTime(order.createdAt)
      };
    });
  },

  // 获取状态文本
  getStatusText(status) {
    const statusMap = {
      'PENDING_PAYMENT': '待付款',
      'PAID': '已付款',
      'PREPARING': '制作中',
      'READY': '待取餐',
      'COMPLETED': '已完成',
      'CANCELLED': '已取消'
    };
    return statusMap[status] || '未知状态';
  },

  // 获取状态样式类
  getStatusClass(status) {
    const classMap = {
      'PENDING_PAYMENT': 'status-pending',
      'PAID': 'status-paid',
      'PREPARING': 'status-preparing',
      'READY': 'status-ready',
      'COMPLETED': 'status-completed',
      'CANCELLED': 'status-cancelled'
    };
    return classMap[status] || 'status-unknown';
  },

  // 获取状态字符串
  getStatusString(statusValue) {
    const statusMap = {
      1: 'PENDING_PAYMENT',
      2: 'PAID',
      3: null, // 制作中状态需要特殊处理
      4: 'COMPLETED'
    };
    return statusMap[statusValue] || '';
  },

  // 格式化时间
  formatTime(timeStr) {
    if (!timeStr) return '';
    const date = new Date(timeStr);
    return date.toLocaleString('zh-CN');
  },

  // 切换订单状态
  switchStatus(e) {
    const status = e.currentTarget.dataset.status;
    this.setData({ currentStatus: status });
    this.loadOrders();
  },

  // 跳转到订单详情
  goToOrderDetail(e) {
    const orderId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/order-detail/order-detail?id=${orderId}`
    });
  },

  // 取消订单
  cancelOrder(e) {
    const orderId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '确认取消',
      content: '确定要取消这个订单吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '取消中...' });
          
          api.cancelOrder(orderId, this.data.userId).then(res => {
            wx.hideLoading();
            if (res.code === 200) {
              wx.showToast({
                title: '订单已取消',
                icon: 'success'
              });
              this.loadOrders();
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

  // 确认收货
  confirmReceive(e) {
    const orderId = e.currentTarget.dataset.id;
    wx.showModal({
      title: '确认收货',
      content: '确认已收到商品吗？',
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '确认收货中...' });
          
          // 调用后端API确认收货
          api.confirmReceive(orderId).then(res => {
            wx.hideLoading();
            if (res.code === 200) {
              wx.showToast({
                title: '确认收货成功',
                icon: 'success'
              });
              // 重新加载订单列表
              this.loadOrders();
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
  goToReview(e) {
    const orderId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/review/review?orderId=${orderId}`
    });
  },

  // 再来一单
  reorder(e) {
    const orderId = e.currentTarget.dataset.id;
    const order = this.data.orders.find(o => o.id === orderId);
    
    if (!order || !order.orderItems) {
      wx.showToast({
        title: '订单数据错误',
        icon: 'none'
      });
      return;
    }

    // 调用API将商品加入购物车
    const promises = order.orderItems.map(item => 
      api.addToCart(this.data.userId, {
        dishId: item.dishId,
        quantity: item.quantity,
        remark: item.remark || ''
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
  },

  // 跳转到菜单页面
  goToMenu() {
    wx.switchTab({
      url: '/pages/menu/menu'
    });
  }
}); 