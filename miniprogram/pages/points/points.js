const { api } = require('../../utils/api');

Page({
  data: {
    totalPoints: 0,
    records: [],
    exchangeItems: [],
    userId: 2 // TODO: 实际项目应从登录态获取
  },

  onLoad() {
    this.loadPointsInfo();
    this.loadPointsRecords();
    this.loadExchangeItems();
  },

  // 加载积分信息
  loadPointsInfo() {
    // 模拟积分数据
    this.setData({
      totalPoints: 1280
    });
  },

  // 加载积分记录
  loadPointsRecords() {
    // 模拟积分记录数据
    const records = [
      {
        id: 1,
        title: '消费获得积分',
        points: 50,
        type: 'earn',
        createTime: '2025-07-06 12:30:00'
      },
      {
        id: 2,
        title: '积分兑换优惠券',
        points: 100,
        type: 'spend',
        createTime: '2025-07-05 15:20:00'
      },
      {
        id: 3,
        title: '消费获得积分',
        points: 30,
        type: 'earn',
        createTime: '2025-07-04 18:45:00'
      },
      {
        id: 4,
        title: '新用户注册奖励',
        points: 200,
        type: 'earn',
        createTime: '2025-07-01 10:00:00'
      }
    ];
    
    this.setData({
      records: records
    });
  },

  // 加载兑换商品
  loadExchangeItems() {
    // 模拟兑换商品数据
    const items = [
      {
        id: 1,
        name: '满30减5优惠券',
        description: '全场通用，有效期30天',
        points: 500,
        imageUrl: '/images/coupon-icon.png'
      },
      {
        id: 2,
        name: '免费配送券',
        description: '免配送费，有效期7天',
        points: 300,
        imageUrl: '/images/delivery-icon.png'
      },
      {
        id: 3,
        name: '生日优惠券',
        description: '生日当月可用，8折优惠',
        points: 800,
        imageUrl: '/images/birthday-icon.png'
      }
    ];
    
    this.setData({
      exchangeItems: items
    });
  },

  // 跳转到积分兑换页面
  goToExchange() {
    wx.navigateTo({
      url: '/pages/points-exchange/points-exchange'
    });
  },

  // 跳转到积分记录页面
  goToRecords() {
    wx.navigateTo({
      url: '/pages/points-records/points-records'
    });
  },

  // 兑换商品
  exchangeItem(e) {
    const item = e.currentTarget.dataset.item;
    
    if (this.data.totalPoints < item.points) {
      wx.showToast({
        title: '积分不足',
        icon: 'none'
      });
      return;
    }

    wx.showModal({
      title: '确认兑换',
      content: `确定要使用${item.points}积分兑换"${item.name}"吗？`,
      success: (res) => {
        if (res.confirm) {
          this.confirmExchange(item);
        }
      }
    });
  },

  // 确认兑换
  confirmExchange(item) {
    wx.showLoading({
      title: '兑换中...'
    });

    // 这里应该调用后端API进行兑换
    setTimeout(() => {
      wx.hideLoading();
      
      // 更新积分
      this.setData({
        totalPoints: this.data.totalPoints - item.points
      });
      
      // 添加兑换记录
      const newRecord = {
        id: Date.now(),
        title: `积分兑换${item.name}`,
        points: item.points,
        type: 'spend',
        createTime: new Date().toLocaleString()
      };
      
      this.setData({
        records: [newRecord, ...this.data.records]
      });
      
      wx.showToast({
        title: '兑换成功',
        icon: 'success'
      });
    }, 1000);
  }
}); 