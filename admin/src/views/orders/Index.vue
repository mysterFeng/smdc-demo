<template>
  <div class="orders-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
          <el-button type="primary" @click="handleRefresh">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </template>
      
      <div class="page-content">
        <p>订单管理功能开发中...</p>
        <el-button type="primary" @click="$router.push('/dashboard')">
          返回仪表盘
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
const handleRefresh = () => {
  // 刷新订单逻辑
}
</script>

<style scoped lang="scss">
.orders-page {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .page-content {
    text-align: center;
    padding: 60px 0;
    
    p {
      font-size: 16px;
      color: #666;
      margin-bottom: 20px;
    }
  }
}
</style> 