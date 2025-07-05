Page({
  data: {
    currentStatus: 0, // 0: 全部, 1: 待付款, 2: 待收货, 3: 待评价, 4: 已完成
    statusTabs: [
      { label: '全部', value: 0 },
      { label: '待付款', value: 1 },
      { label: '待收货', value: 2 },
      { label: '待评价', value: 3 },
      { label: '已完成', value: 4 }
    ],
    orders: []
  },

  onLoad() {
    this.loadOrders();
  },

  onShow() {
    this.loadOrders();
  },

  // 加载订单数据
  loadOrders() {
    // 模拟订单数据
    const mockOrders = [
      {
        id: 1,
        orderNo: 'ORDER20240101001',
        status: 1,
        statusText: '待付款',
        statusClass: 'status-pending',
        dishes: [
          {
            id: 1,
            name: '宫保鸡丁',
            imageUrl: '/images/dishes/gongbao-chicken.jpg',
            price: 28.00,
            quantity: 1
          },
          {
            id: 2,
            name: '麻婆豆腐',
            imageUrl: '/images/dishes/mapo-tofu.jpg',
            price: 18.00,
            quantity: 2
          }
        ],
        totalCount: 3,
        actualAmount: 64.00,
        createTime: '2024-01-01 12:30:00'
      },
      {
        id: 2,
        orderNo: 'ORDER20240101002',
        status: 2,
        statusText: '待收货',
        statusClass: 'status-delivering',
        dishes: [
          {
            id: 3,
            name: '白米饭',
            imageUrl: '/images/dishes/rice.jpg',
            price: 3.00,
            quantity: 2
          }
        ],
        totalCount: 2,
        actualAmount: 11.00,
        createTime: '2024-01-01 11:20:00'
      },
      {
        id: 3,
        orderNo: 'ORDER20240101003',
        status: 3,
        statusText: '待评价',
        statusClass: 'status-completed',
        dishes: [
          {
            id: 4,
            name: '酸辣汤',
            imageUrl: '/images/dishes/sour-soup.jpg',
            price: 12.00,
            quantity: 1
          }
        ],
        totalCount: 1,
        actualAmount: 17.00,
        createTime: '2024-01-01 10:15:00'
      }
    ];

    // 根据当前状态筛选订单
    const filteredOrders = this.data.currentStatus === 0 
      ? mockOrders 
      : mockOrders.filter(order => order.status === this.data.currentStatus);

    this.setData({ orders: filteredOrders });
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
          // 这里应该调用API取消订单
          wx.showToast({
            title: '订单已取消',
            icon: 'success'
          });
          this.loadOrders();
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
          // 这里应该调用API确认收货
          wx.showToast({
            title: '确认收货成功',
            icon: 'success'
          });
          this.loadOrders();
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
    // 这里应该将订单中的商品重新加入购物车
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
  },

  // 跳转到菜单页面
  goToMenu() {
    wx.switchTab({
      url: '/pages/menu/menu'
    });
  }
}); 