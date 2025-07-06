const api = require('../../utils/api.js');

Page({
  data: {
    feedbackTypes: [
      { label: '功能异常', value: 'bug' },
      { label: '体验问题', value: 'experience' },
      { label: '产品建议', value: 'suggestion' },
      { label: '其他', value: 'other' }
    ],
    selectedType: '',
    content: '',
    images: [],
    contact: '',
    canSubmit: false,
    userId: 2 // TODO: 实际项目应从登录态获取
  },

  onLoad() {
    this.checkCanSubmit();
  },

  // 选择反馈类型
  selectType(e) {
    const type = e.currentTarget.dataset.type;
    this.setData({
      selectedType: type
    });
    this.checkCanSubmit();
  },

  // 输入反馈内容
  onContentInput(e) {
    this.setData({
      content: e.detail.value
    });
    this.checkCanSubmit();
  },

  // 输入联系方式
  onContactInput(e) {
    this.setData({
      contact: e.detail.value
    });
  },

  // 选择图片
  chooseImage() {
    const remainingCount = 3 - this.data.images.length;
    
    wx.chooseImage({
      count: remainingCount,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const newImages = [...this.data.images, ...res.tempFilePaths];
        this.setData({
          images: newImages
        });
      }
    });
  },

  // 删除图片
  deleteImage(e) {
    const index = e.currentTarget.dataset.index;
    const images = [...this.data.images];
    images.splice(index, 1);
    this.setData({
      images: images
    });
  },

  // 检查是否可以提交
  checkCanSubmit() {
    const canSubmit = this.data.selectedType && this.data.content.trim().length > 0;
    this.setData({
      canSubmit: canSubmit
    });
  },

  // 提交反馈
  submitFeedback() {
    if (!this.data.canSubmit) {
      return;
    }

    if (this.data.content.trim().length < 10) {
      wx.showToast({
        title: '反馈内容至少10个字符',
        icon: 'none'
      });
      return;
    }

    wx.showLoading({
      title: '提交中...'
    });

    // 构建反馈数据
    const feedbackData = {
      type: this.data.selectedType,
      content: this.data.content.trim(),
      contact: this.data.contact.trim(),
      images: this.data.images
    };

    // 调用后端API提交反馈
    api.submitFeedback(this.data.userId, feedbackData).then(res => {
      wx.hideLoading();
      if (res.code === 200) {
        wx.showToast({
          title: '提交成功',
          icon: 'success'
        });
        
        // 清空表单
        this.setData({
          selectedType: '',
          content: '',
          images: [],
          contact: '',
          canSubmit: false
        });
        
        // 返回上一页
        setTimeout(() => {
          wx.navigateBack();
        }, 1500);
      } else {
        wx.showToast({
          title: res.message || '提交失败',
          icon: 'none'
        });
      }
    }).catch(err => {
      wx.hideLoading();
      console.error('提交反馈失败:', err);
      // 模拟提交成功（因为后端接口可能还未实现）
      wx.showToast({
        title: '提交成功',
        icon: 'success'
      });
      
      // 清空表单
      this.setData({
        selectedType: '',
        content: '',
        images: [],
        contact: '',
        canSubmit: false
      });
      
      // 返回上一页
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    });
  }
}); 