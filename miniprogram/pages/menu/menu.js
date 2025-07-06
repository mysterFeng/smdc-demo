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
    const { api } = require('../../utils/api.js');
    
    console.log('开始加载菜品数据...');
    
    // 获取分类列表
    api.getActiveCategories().then(res => {
      console.log('获取分类成功:', res);
      if (res.code === 200) {
        this.setData({
          categories: res.data
        });
      }
    }).catch(err => {
      console.error('获取分类失败:', err);
      wx.showToast({
        title: '获取分类失败',
        icon: 'none'
      });
    });
    
    // 获取菜品列表（默认获取所有菜品）
    api.getDishList(0, 20).then(res => {
      console.log('获取菜品成功:', res);
      if (res.code === 200) {
        const dishes = res.data.content.map(dish => ({
          ...dish,
          quantity: 0
        }));
        this.setData({
          dishes: dishes
        });
      }
    }).catch(err => {
      console.error('获取菜品失败:', err);
      wx.showToast({
        title: '获取菜品失败',
        icon: 'none'
      });
    });
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
    const keyword = this.data.searchKeyword;
    if (!keyword.trim()) {
      // 如果搜索关键词为空，重新加载所有菜品
      this.loadDishes();
      return;
    }
    
    const { api } = require('../../utils/api.js');
    
    api.searchDishes(keyword).then(res => {
      if (res.code === 200) {
        const dishes = res.data.map(dish => ({
          ...dish,
          quantity: 0
        }));
        this.setData({
          dishes: dishes
        });
      }
    }).catch(err => {
      console.error('搜索菜品失败:', err);
      wx.showToast({
        title: '搜索菜品失败',
        icon: 'none'
      });
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
    const { api } = require('../../utils/api.js');
    
    if (categoryId === 1) {
      // 如果是第一个分类（通常是"热菜"），获取所有菜品
      api.getDishList(0, 20).then(res => {
        if (res.code === 200) {
          const dishes = res.data.content.map(dish => ({
            ...dish,
            quantity: 0
          }));
          this.setData({
            dishes: dishes
          });
        }
      }).catch(err => {
        console.error('获取菜品失败:', err);
        wx.showToast({
          title: '获取菜品失败',
          icon: 'none'
        });
      });
    } else {
      // 根据分类ID获取菜品
      api.getDishesByCategory(categoryId, 0, 20).then(res => {
        if (res.code === 200) {
          const dishes = res.data.content.map(dish => ({
            ...dish,
            quantity: 0
          }));
          this.setData({
            dishes: dishes
          });
        }
      }).catch(err => {
        console.error('获取分类菜品失败:', err);
        wx.showToast({
          title: '获取分类菜品失败',
          icon: 'none'
        });
      });
    }
  },

  // 添加到购物车
  addToCart(e) {
    const dishId = e.currentTarget.dataset.id;
    const dish = this.data.dishes.find(d => d.id === dishId);
    
    if (!dish) {
      wx.showToast({
        title: '菜品不存在',
        icon: 'none'
      });
      return;
    }
    
    // 获取当前用户ID（这里暂时使用固定值，后续从登录信息获取）
    const userId = wx.getStorageSync('userId') || 2;
    
    const { api } = require('../../utils/api.js');
    
    // 调用后端API添加菜品到购物车
    api.addToCart(userId, {
      dishId: dishId,
      quantity: 1,
      remark: ''
    }).then(res => {
      console.log('添加到购物车成功:', res);
      
      // 更新本地菜品数量
      const dishes = this.data.dishes.map(d => {
        if (d.id === dishId) {
          return { ...d, quantity: d.quantity + 1 };
        }
        return d;
      });
      
      this.setData({ dishes });
      this.updateCartInfo();
      
      wx.showToast({
        title: '已添加到购物车',
        icon: 'success'
      });
    }).catch(err => {
      console.error('添加到购物车失败:', err);
      wx.showToast({
        title: err.message || '添加失败',
        icon: 'none'
      });
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