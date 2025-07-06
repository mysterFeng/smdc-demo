Page({
  data: {
    // 页面数据
  },

  onLoad() {
    // 页面加载时的逻辑
  },

  // 复制文本
  copyText(e) {
    const text = e.currentTarget.dataset.text;
    
    wx.setClipboardData({
      data: text,
      success: () => {
        wx.showToast({
          title: '已复制到剪贴板',
          icon: 'success'
        });
      },
      fail: () => {
        wx.showToast({
          title: '复制失败',
          icon: 'none'
        });
      }
    });
  }
}); 