// 初始化测试数据脚本
// 在微信开发者工具的控制台中运行此脚本

// 初始化测试地址
function initTestAddresses() {
  const testAddresses = [
    {
      id: 1,
      name: '张三',
      phone: '13800000001',
      region: '北京市 北京市 朝阳区',
      regionArray: ['北京市', '北京市', '朝阳区'],
      detail: '三里屯街道xxx号',
      isDefault: true,
      address: '北京市朝阳区三里屯街道xxx号'
    },
    {
      id: 2,
      name: '李四',
      phone: '13800000002',
      region: '北京市 北京市 海淀区',
      regionArray: ['北京市', '北京市', '海淀区'],
      detail: '中关村街道yyy号',
      isDefault: false,
      address: '北京市海淀区中关村街道yyy号'
    },
    {
      id: 3,
      name: '王五',
      phone: '13800000003',
      region: '北京市 北京市 西城区',
      regionArray: ['北京市', '北京市', '西城区'],
      detail: '西单街道zzz号',
      isDefault: false,
      address: '北京市西城区西单街道zzz号'
    }
  ];
  
  wx.setStorageSync('addresses', testAddresses);
  console.log('测试地址初始化完成:', testAddresses);
}

// 初始化测试用户信息
function initTestUserInfo() {
  const userInfo = {
    id: 2,
    username: 'testuser',
    phone: '13800000001',
    avatar: '/images/avatar/default.png',
    nickname: '测试用户'
  };
  
  wx.setStorageSync('userInfo', userInfo);
  console.log('测试用户信息初始化完成:', userInfo);
}

// 初始化购物车测试数据
function initTestCartItems() {
  const testCartItems = [
    {
      id: 1,
      dishId: 1,
      dishName: '宫保鸡丁',
      dishImageUrl: '/images/dishes/gongbao-chicken.jpg',
      quantity: 2,
      unitPrice: 28.0,
      subtotal: 56.0,
      selected: true,
      remark: ''
    },
    {
      id: 2,
      dishId: 2,
      dishName: '麻婆豆腐',
      dishImageUrl: '/images/dishes/mapo-tofu.jpg',
      quantity: 1,
      unitPrice: 18.0,
      subtotal: 18.0,
      selected: true,
      remark: ''
    }
  ];
  
  // 注意：这里只是示例，实际购物车数据应该通过API获取
  console.log('购物车测试数据示例:', testCartItems);
}

// 执行初始化
function initAllTestData() {
  console.log('开始初始化测试数据...');
  
  initTestAddresses();
  initTestUserInfo();
  initTestCartItems();
  
  console.log('所有测试数据初始化完成！');
  console.log('现在可以测试购物车和下单功能了。');
}

// 导出函数供控制台调用
window.initTestAddresses = initTestAddresses;
window.initTestUserInfo = initTestUserInfo;
window.initTestCartItems = initTestCartItems;
window.initAllTestData = initAllTestData;

// 自动执行初始化
initAllTestData(); 