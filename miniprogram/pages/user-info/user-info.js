const { api } = require('../../utils/api');

Page({
  data: {
    userInfo: {},
    genderOptions: ['保密', '男', '女'],
    genderIndex: 0,
    userId: 2 // TODO: 实际项目应从登录态获取
  },

  onLoad() {
    this.loadUserInfo();
  },

  // 加载用户信息
  loadUserInfo() {
    const userInfo = wx.getStorageSync('userInfo');
    if (userInfo) {
      this.setData({
        userInfo: userInfo,
        genderIndex: userInfo.gender || 0
      });
    }
  },

  // 更换头像
  changeAvatar() {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFilePaths[0];
        
        // 这里应该上传图片到服务器
        // 暂时直接设置本地路径
        this.setData({
          'userInfo.avatarUrl': tempFilePath
        });
        
        wx.showToast({
          title: '头像更新成功',
          icon: 'success'
        });
      }
    });
  },

  // 昵称输入
  onNicknameInput(e) {
    this.setData({
      'userInfo.nickName': e.detail.value
    });
  },

  // 性别选择
  onGenderChange(e) {
    this.setData({
      genderIndex: e.detail.value,
      'userInfo.gender': parseInt(e.detail.value)
    });
  },

  // 生日选择
  onBirthdayChange(e) {
    this.setData({
      'userInfo.birthday': e.detail.value
    });
  },

  // 绑定手机号
  bindPhone() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  // 保存用户信息
  saveUserInfo() {
    const userInfo = this.data.userInfo;
    
    if (!userInfo.nickName || userInfo.nickName.trim() === '') {
      wx.showToast({
        title: '请输入昵称',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '保存中...'
    });

    // 调用后端API保存用户信息
    api.updateUserInfo(this.data.userId, {
      nickName: userInfo.nickName,
      gender: userInfo.gender,
      birthday: userInfo.birthday,
      avatarUrl: userInfo.avatarUrl
    }).then(res => {
      wx.hideLoading();
      if (res.code === 200) {
        // 更新本地存储
        wx.setStorageSync('userInfo', userInfo);
        
        wx.showToast({
          title: '保存成功',
          icon: 'success'
        });
        
        // 返回上一页
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '保存失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.hideLoading();
      console.error('保存用户信息失败:', err);
      wx.showToast({
        title: '保存失败',
        icon: 'none'
      });
    });
  }
}); 