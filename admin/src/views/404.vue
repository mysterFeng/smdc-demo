<template>
  <div class="not-found">
    <div class="not-found-content">
      <h1>404</h1>
      <h2>页面不存在</h2>
      <p>抱歉，您访问的页面不存在或已被删除</p>
      <el-button type="primary" @click="$router.push('/')">
        返回首页
      </el-button>
    </div>
  </div>
</template>

<style scoped lang="scss">
.not-found {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  
  .not-found-content {
    text-align: center;
    
    h1 {
      font-size: 120px;
      color: #409eff;
      margin-bottom: 20px;
    }
    
    h2 {
      font-size: 32px;
      color: #333;
      margin-bottom: 16px;
    }
    
    p {
      font-size: 16px;
      color: #666;
      margin-bottom: 30px;
    }
  }
}
</style> 