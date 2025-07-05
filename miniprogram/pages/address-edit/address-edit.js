Page({
  data: {
    isEdit: false,
    addressId: null,
    formData: {
      name: '',
      phone: '',
      region: '',
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

  // 加载地址数据
  loadAddressData(id) {
    const addresses = wx.getStorageSync('addresses') || [];
    const address = addresses.find(addr => addr.id == id);
    
    if (address) {
      this.setData({
        formData: {
          name: address.name,
          phone: address.phone,
          region: address.region,
          detail: address.detail,
          isDefault: address.isDefault
        },
        region: address.regionArray || []
      });
    }
  },

  // 收货人姓名输入
  onNameInput(e) {
    this.setData({
      'formData.name': e.detail.value
    });
  },

  // 手机号码输入
  onPhoneInput(e) {
    this.setData({
      'formData.phone': e.detail.value
    });
  },

  // 地区选择
  onRegionChange(e) {
    const region = e.detail.value;
    this.setData({
      region: region,
      'formData.region': region.join(' ')
    });
  },

  // 详细地址输入
  onDetailInput(e) {
    this.setData({
      'formData.detail': e.detail.value
    });
  },

  // 切换默认地址
  toggleDefault() {
    this.setData({
      'formData.isDefault': !this.data.formData.isDefault
    });
  },

  // 默认地址开关变化
  onDefaultChange(e) {
    this.setData({
      'formData.isDefault': e.detail.value
    });
  },

  // 验证表单
  validateForm() {
    const { name, phone, region, detail } = this.data.formData;
    
    if (!name.trim()) {
      wx.showToast({
        title: '请输入收货人姓名',
        icon: 'none'
      });
      return false;
    }
    
    if (!phone.trim()) {
      wx.showToast({
        title: '请输入手机号码',
        icon: 'none'
      });
      return false;
    }
    
    // 简单的手机号验证
    const phoneRegex = /^1[3-9]\d{9}$/;
    if (!phoneRegex.test(phone)) {
      wx.showToast({
        title: '请输入正确的手机号码',
        icon: 'none'
      });
      return false;
    }
    
    if (!region.trim()) {
      wx.showToast({
        title: '请选择所在地区',
        icon: 'none'
      });
      return false;
    }
    
    if (!detail.trim()) {
      wx.showToast({
        title: '请输入详细地址',
        icon: 'none'
      });
      return false;
    }
    
    return true;
  },

  // 保存地址
  saveAddress() {
    if (!this.validateForm()) {
      return;
    }
    
    const addresses = wx.getStorageSync('addresses') || [];
    const formData = this.data.formData;
    
    if (this.data.isEdit) {
      // 编辑模式
      const index = addresses.findIndex(addr => addr.id == this.data.addressId);
      if (index !== -1) {
        addresses[index] = {
          ...addresses[index],
          ...formData,
          regionArray: this.data.region
        };
      }
    } else {
      // 新增模式
      const newAddress = {
        id: Date.now(),
        ...formData,
        regionArray: this.data.region
      };
      
      // 如果设为默认地址，取消其他地址的默认状态
      if (formData.isDefault) {
        addresses.forEach(addr => addr.isDefault = false);
      }
      
      addresses.push(newAddress);
    }
    
    // 保存到本地存储
    wx.setStorageSync('addresses', addresses);
    
    wx.showToast({
      title: this.data.isEdit ? '修改成功' : '添加成功',
      icon: 'success'
    });
    
    // 返回上一页
    setTimeout(() => {
      wx.navigateBack();
    }, 1500);
  },

  // 删除地址
  deleteAddress() {
    wx.showModal({
      title: '确认删除',
      content: '确定要删除这个地址吗？',
      success: (res) => {
        if (res.confirm) {
          const addresses = wx.getStorageSync('addresses') || [];
          const filteredAddresses = addresses.filter(addr => addr.id != this.data.addressId);
          
          wx.setStorageSync('addresses', filteredAddresses);
          
          wx.showToast({
            title: '删除成功',
            icon: 'success'
          });
          
          setTimeout(() => {
            wx.navigateBack();
          }, 1500);
        }
      }
    });
  }
}); 