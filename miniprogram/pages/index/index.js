const app = getApp();

Page({
  data: {
    userInfo: null,
    searchKeyword: '',
    banners: [
      {
        id: 1,
        imageUrl: '/images/banner1.jpg',
        title: '新品上市'
      },
      {
        id: 2,
        imageUrl: '/images/banner2.jpg',
        title: '限时优惠'
      },
      {
        id: 3,
        imageUrl: '/images/banner3.jpg',
        title: '特色推荐'
      }
    ],
    recommendDishes: [],
    shopInfo: {
      businessHours: '09:00-22:00',
      phone: '400-123-4567',
      address: '北京市朝阳区xxx街道xxx号'
    }
  },

  onLoad() {
    this.loadUserInfo();
    this.loadRecommendDishes();
  },

  onShow() {
    // 每次显示页面时更新用户信息
    this.loadUserInfo();
    this.updateDishQuantities();
  },

  // 加载用户信息
  loadUserInfo() {
    const userInfo = app.globalData.userInfo;
    this.setData({
      userInfo: userInfo
    });
  },

  // 加载推荐菜品
  loadRecommendDishes() {
    const { api } = require('../../utils/api.js');
    
    api.getRecommendDishes(4).then(res => {
      if (res.code === 200) {
        const dishes = res.data.map(dish => ({
          ...dish,
          quantity: 0
        }));
        this.setData({
          recommendDishes: dishes
        });
      }
    }).catch(err => {
      console.error('获取推荐菜品失败:', err);
      // 如果API调用失败，使用默认数据
      const mockDishes = [
        {
          id: 1,
          name: '宫保鸡丁',
          description: '经典川菜，口感麻辣鲜香',
          price: 28.00,
          imageUrl: '/images/dish1.jpg',
          quantity: 0
        },
        {
          id: 2,
          name: '麻婆豆腐',
          description: '嫩滑豆腐配麻辣肉末',
          price: 18.00,
          imageUrl: '/images/dish2.jpg',
          quantity: 0
        },
        {
          id: 3,
          name: '水煮鱼',
          description: '新鲜草鱼，麻辣鲜香',
          price: 48.00,
          imageUrl: '/images/dish3.jpg',
          quantity: 0
        },
        {
          id: 4,
          name: '回锅肉',
          description: '肥而不腻，香辣可口',
          price: 32.00,
          imageUrl: '/images/dish4.jpg',
          quantity: 0
        }
      ];
      this.setData({
        recommendDishes: mockDishes
      });
    });
  },

  // 更新菜品数量（从购物车获取）
  updateDishQuantities() {
    const cartItems = app.globalData.cartItems;
    const recommendDishes = this.data.recommendDishes.map(dish => {
      const cartItem = cartItems.find(item => item.id === dish.id);
      return {
        ...dish,
        quantity: cartItem ? cartItem.quantity : 0
      };
    });

    this.setData({
      recommendDishes: recommendDishes
    });
  },

  // 搜索输入
  onSearchInput(e) {
    this.setData({
      searchKeyword: e.detail.value
    });
  },

  // 轮播图点击
  onBannerTap(e) {
    const item = e.currentTarget.dataset.item;
    wx.showToast({
      title: item.title,
      icon: 'none'
    });
  },

  // 添加到购物车
  addToCart(e) {
    const dish = e.currentTarget.dataset.dish;
    app.addToCart(dish);
    
    // 更新页面显示
    this.updateDishQuantities();
    
    wx.showToast({
      title: '已添加到购物车',
      icon: 'success'
    });
  },

  // 增加数量
  increaseQuantity(e) {
    const id = e.currentTarget.dataset.id;
    const dish = this.data.recommendDishes.find(item => item.id === id);
    if (dish) {
      app.updateCartItemQuantity(id, dish.quantity + 1);
      this.updateDishQuantities();
    }
  },

  // 减少数量
  decreaseQuantity(e) {
    const id = e.currentTarget.dataset.id;
    const dish = this.data.recommendDishes.find(item => item.id === id);
    if (dish && dish.quantity > 0) {
      app.updateCartItemQuantity(id, dish.quantity - 1);
      this.updateDishQuantities();
    }
  },

  // 跳转到菜单页
  goToMenu() {
    wx.switchTab({
      url: '/pages/menu/menu'
    });
  },

  // 跳转到订单页
  goToOrder() {
    wx.switchTab({
      url: '/pages/order/order'
    });
  },

  // 跳转到个人中心
  goToProfile() {
    wx.switchTab({
      url: '/pages/profile/profile'
    });
  },

  // 跳转到地址管理
  goToAddress() {
    wx.navigateTo({
      url: '/pages/address/address'
    });
  },

  // 跳转到登录页
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  // 跳转到菜品详情
  goToDishDetail(e) {
    const dish = e.currentTarget.dataset.dish;
    // 这里可以跳转到菜品详情页
    wx.showToast({
      title: `查看${dish.name}详情`,
      icon: 'none'
    });
  },

  // 拨打电话
  callShop() {
    wx.makePhoneCall({
      phoneNumber: this.data.shopInfo.phone
    });
  }
}); 