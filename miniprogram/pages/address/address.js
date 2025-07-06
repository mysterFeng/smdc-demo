const { api } = require('../../utils/api');

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

  // 加载地址列表（接口）
  loadAddresses() {
    const userId = 2; // TODO: 实际项目应从登录态获取
    api.getUserAddresses(userId).then(res => {
      if (res.code === 200) {
        this.setData({ addresses: res.data || [] });
      }
    });
  },

  // 选择地址
  selectAddress(e) {
    if (!this.data.isSelectMode) return;
    const address = e.currentTarget.dataset.address;
    // 返回上一页并设置选中地址
    const pages = getCurrentPages();
    const prevPage = pages[pages.length - 2];
    if (prevPage) {
      prevPage.setData({ selectedAddress: address });
    }
    wx.navigateBack();
  },

  // 添加地址
  addAddress() {
    wx.navigateTo({ url: '/pages/address-edit/address-edit' });
  },

  // 编辑地址
  editAddress(e) {
    const addressId = e.currentTarget.dataset.id;
    wx.navigateTo({ url: `/pages/address-edit/address-edit?id=${addressId}` });
  },

  // 删除地址
  deleteAddress(e) {
    const addressId = e.currentTarget.dataset.id;
    const userId = 2; // TODO: 实际项目应从登录态获取
    wx.showModal({
      title: '确认删除',
      content: '确定要删除该地址吗？',
      success: (res) => {
        if (res.confirm) {
          api.deleteAddress(addressId, userId).then(r => {
            if (r.code === 200) {
              wx.showToast({ title: '删除成功', icon: 'success' });
              this.loadAddresses();
            } else {
              wx.showToast({ title: r.message || '删除失败', icon: 'none' });
            }
          });
        }
      }
    });
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
  }
}); 