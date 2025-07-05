Page({
  data: {
    addresses: [],
    isSelectMode: false // 是否为选择模式
  },

  onLoad(options) {
    // 判断是否为选择模式
    if (options.select === 'true') {
      this.setData({ isSelectMode: true });
    }
    this.loadAddresses();
  },

  onShow() {
    this.loadAddresses();
  },

  // 加载地址列表
  loadAddresses() {
    // 从本地存储获取地址数据
    const addresses = wx.getStorageSync('addresses') || [];
    this.setData({ addresses });
  },

  // 选择地址
  selectAddress(e) {
    if (!this.data.isSelectMode) return;
    
    const addressId = e.currentTarget.dataset.id;
    const selectedAddress = this.data.addresses.find(addr => addr.id === addressId);
    
    if (selectedAddress) {
      // 返回上一页并传递选中的地址
      const pages = getCurrentPages();
      const prevPage = pages[pages.length - 2];
      
      if (prevPage) {
        prevPage.setData({
          selectedAddress: selectedAddress
        });
      }
      
      wx.navigateBack();
    }
  },

  // 设置默认地址
  setDefault(e) {
    const addressId = e.currentTarget.dataset.id;
    const addresses = this.data.addresses.map(addr => ({
      ...addr,
      isDefault: addr.id === addressId
    }));
    
    this.setData({ addresses });
    wx.setStorageSync('addresses', addresses);
    
    wx.showToast({
      title: '设置成功',
      icon: 'success'
    });
  },

  // 编辑地址
  editAddress(e) {
    const addressId = e.currentTarget.dataset.id;
    wx.navigateTo({
      url: `/pages/address-edit/address-edit?id=${addressId}`
    });
  },

  // 删除地址
  deleteAddress(e) {
    const addressId = e.currentTarget.dataset.id;
    const address = this.data.addresses.find(addr => addr.id === addressId);
    
    wx.showModal({
      title: '确认删除',
      content: `确定要删除地址"${address.name} ${address.phone}"吗？`,
      success: (res) => {
        if (res.confirm) {
          const addresses = this.data.addresses.filter(addr => addr.id !== addressId);
          this.setData({ addresses });
          wx.setStorageSync('addresses', addresses);
          
          wx.showToast({
            title: '删除成功',
            icon: 'success'
          });
        }
      }
    });
  },

  // 添加地址
  addAddress() {
    wx.navigateTo({
      url: '/pages/address-edit/address-edit'
    });
  }
}); 