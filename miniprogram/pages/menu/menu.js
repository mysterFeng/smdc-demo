Page({
  data: {
    searchKeyword: '',
    currentCategory: 1,
    categories: [
      { id: 1, name: '热销推荐' },
      { id: 2, name: '主食' },
      { id: 3, name: '小菜' },
      { id: 4, name: '汤类' },
      { id: 5, name: '饮品' },
      { id: 6, name: '甜点' }
    ],
    dishes: [
      {
        id: 1,
        name: '宫保鸡丁',
        description: '经典川菜，鸡肉鲜嫩，花生香脆',
        price: 28.00,
        originalPrice: 32.00,
        imageUrl: '/images/dishes/gongbao-chicken.jpg',
        categoryId: 1,
        quantity: 0
      },
      {
        id: 2,
        name: '麻婆豆腐',
        description: '麻辣鲜香，豆腐嫩滑',
        price: 18.00,
        originalPrice: 22.00,
        imageUrl: '/images/dishes/mapo-tofu.jpg',
        categoryId: 1,
        quantity: 0
      },
      {
        id: 3,
        name: '白米饭',
        description: '精选东北大米，粒粒分明',
        price: 3.00,
        imageUrl: '/images/dishes/rice.jpg',
        categoryId: 2,
        quantity: 0
      },
      {
        id: 4,
        name: '酸辣汤',
        description: '开胃解腻，酸辣可口',
        price: 12.00,
        imageUrl: '/images/dishes/sour-soup.jpg',
        categoryId: 4,
        quantity: 0
      }
    ],
    cartCount: 0,
    cartTotal: 0
  },

  onLoad() {
    this.loadDishes();
    this.updateCartInfo();
  },

  onShow() {
    this.updateCartInfo();
  },

  // 加载菜品数据
  loadDishes() {
    // 这里应该调用API获取菜品数据
    // 暂时使用模拟数据
    console.log('加载菜品数据');
  },

  // 搜索输入
  onSearchInput(e) {
    this.setData({
      searchKeyword: e.detail.value
    });
    this.filterDishes();
  },

  // 筛选菜品
  filterDishes() {
    const keyword = this.data.searchKeyword.toLowerCase();
    const filteredDishes = this.data.dishes.filter(dish => 
      dish.name.toLowerCase().includes(keyword) || 
      dish.description.toLowerCase().includes(keyword)
    );
    this.setData({
      dishes: filteredDishes
    });
  },

  // 选择分类
  selectCategory(e) {
    const categoryId = e.currentTarget.dataset.id;
    this.setData({
      currentCategory: categoryId
    });
    this.filterDishesByCategory(categoryId);
  },

  // 按分类筛选菜品
  filterDishesByCategory(categoryId) {
    // 重新加载所有菜品，然后按分类筛选
    const allDishes = [
      {
        id: 1,
        name: '宫保鸡丁',
        description: '经典川菜，鸡肉鲜嫩，花生香脆',
        price: 28.00,
        originalPrice: 32.00,
        imageUrl: '/images/dishes/gongbao-chicken.jpg',
        categoryId: 1,
        quantity: 0
      },
      {
        id: 2,
        name: '麻婆豆腐',
        description: '麻辣鲜香，豆腐嫩滑',
        price: 18.00,
        originalPrice: 22.00,
        imageUrl: '/images/dishes/mapo-tofu.jpg',
        categoryId: 1,
        quantity: 0
      },
      {
        id: 3,
        name: '白米饭',
        description: '精选东北大米，粒粒分明',
        price: 3.00,
        imageUrl: '/images/dishes/rice.jpg',
        categoryId: 2,
        quantity: 0
      },
      {
        id: 4,
        name: '酸辣汤',
        description: '开胃解腻，酸辣可口',
        price: 12.00,
        imageUrl: '/images/dishes/sour-soup.jpg',
        categoryId: 4,
        quantity: 0
      }
    ];

    const filteredDishes = categoryId === 1 ? allDishes : allDishes.filter(dish => dish.categoryId === categoryId);
    this.setData({
      dishes: filteredDishes
    });
  },

  // 添加到购物车
  addToCart(e) {
    const dishId = e.currentTarget.dataset.id;
    const dishes = this.data.dishes.map(dish => {
      if (dish.id === dishId) {
        return { ...dish, quantity: dish.quantity + 1 };
      }
      return dish;
    });
    
    this.setData({ dishes });
    this.updateCartInfo();
    
    wx.showToast({
      title: '已添加到购物车',
      icon: 'success'
    });
  },

  // 增加数量
  increaseQuantity(e) {
    const dishId = e.currentTarget.dataset.id;
    const dishes = this.data.dishes.map(dish => {
      if (dish.id === dishId) {
        return { ...dish, quantity: dish.quantity + 1 };
      }
      return dish;
    });
    
    this.setData({ dishes });
    this.updateCartInfo();
  },

  // 减少数量
  decreaseQuantity(e) {
    const dishId = e.currentTarget.dataset.id;
    const dishes = this.data.dishes.map(dish => {
      if (dish.id === dishId && dish.quantity > 0) {
        return { ...dish, quantity: dish.quantity - 1 };
      }
      return dish;
    });
    
    this.setData({ dishes });
    this.updateCartInfo();
  },

  // 更新购物车信息
  updateCartInfo() {
    const cartItems = this.data.dishes.filter(dish => dish.quantity > 0);
    const cartCount = cartItems.reduce((total, item) => total + item.quantity, 0);
    const cartTotal = cartItems.reduce((total, item) => total + (item.price * item.quantity), 0);
    
    this.setData({
      cartCount,
      cartTotal: cartTotal.toFixed(2)
    });
  },

  // 跳转到购物车页面
  goToCart() {
    wx.switchTab({
      url: '/pages/cart/cart'
    });
  },

  // 菜品列表滚动
  onDishListScroll(e) {
    // 可以在这里实现滚动加载更多功能
  }
}); 