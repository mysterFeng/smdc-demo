const { api } = require('../../utils/api');

Page({
  data: {
    isEdit: false,
    addressId: null,
    formData: {
      name: '',
      phone: '',
      region: [],
      detail: '',
      isDefault: false
    },
    region: []
  },

  onLoad(options) {
    if (options.id) {
      this.setData({ 
        isEdit: true,
        addressId: options.id
      });
      this.loadAddressData(options.id);
    }
  },

  // 加载地址数据（接口）
  loadAddressData(id) {
    const userId = 2; // TODO: 实际项目应从登录态获取
    api.getAddressById(id, userId).then(res => {
      if (res.code === 200 && res.data) {
        const address = res.data;
        this.setData({
          formData: {
            name: address.name,
            phone: address.phone,
            region: [address.province, address.city, address.district],
            detail: address.detail,
            isDefault: address.isDefault === 1
          },
          region: [address.province, address.city, address.district]
        });
      }
    });
  },

  // 省市区选择
  onRegionChange(e) {
    this.setData({
      'formData.region': e.detail.value,
      region: e.detail.value
    });
  },

  // 输入框输入
  onInput(e) {
    const field = e.currentTarget.dataset.field;
    this.setData({ [`formData.${field}`]: e.detail.value });
  },

  // 是否默认地址切换
  onDefaultChange(e) {
    this.setData({ 'formData.isDefault': e.detail.value });
  },

  // 切换默认地址（点击文字切换）
  toggleDefault() {
    this.setData({ 'formData.isDefault': !this.data.formData.isDefault });
  },

  // 保存地址（接口）
  saveAddress() {
    const userId = 2; // TODO: 实际项目应从登录态获取
    const { formData, isEdit, addressId } = this.data;
    if (!formData.name || !formData.phone || !formData.region.length || !formData.detail) {
      wx.showToast({ title: '请填写完整信息', icon: 'none' });
      return;
    }
    const data = {
      name: formData.name,
      phone: formData.phone,
      province: formData.region[0],
      city: formData.region[1],
      district: formData.region[2],
      detail: formData.detail,
      isDefault: formData.isDefault ? 1 : 0
    };
    if (isEdit) {
      api.updateAddress(addressId, userId, data).then(res => {
        if (res.code === 200) {
          wx.showToast({ title: '保存成功', icon: 'success' });
          wx.navigateBack();
        } else {
          wx.showToast({ title: res.message || '保存失败', icon: 'none' });
        }
      });
    } else {
      api.addAddress(userId, data).then(res => {
        if (res.code === 200) {
          wx.showToast({ title: '添加成功', icon: 'success' });
          wx.navigateBack();
        } else {
          wx.showToast({ title: res.message || '添加失败', icon: 'none' });
        }
      });
    }
  },

  // 删除地址
  deleteAddress() {
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这个地址吗？',
      success: (res) => {
        if (res.confirm) {
          const userId = 2; // TODO: 实际项目应从登录态获取
          api.deleteAddress(this.data.addressId, userId).then(res => {
            if (res.code === 200) {
              wx.showToast({
                title: '删除成功',
                icon: 'success'
              });
              setTimeout(() => {
                wx.navigateBack();
              }, 1500);
            } else {
              wx.showToast({
                title: res.message || '删除失败',
                icon: 'none'
              });
            }
          });
        }
      }
    });
  }
}); 