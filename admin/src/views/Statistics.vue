<template>
  <div class="statistics-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据统计</span>
          <el-button type="primary" @click="handleRefresh">
            <el-icon><Refresh /></el-icon>
            刷新数据
          </el-button>
        </div>
      </template>
      
      <div class="page-content">
        <p>数据统计功能开发中...</p>
        <el-button type="primary" @click="$router.push('/dashboard')">
          返回仪表盘
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
const handleRefresh = () => {
  // 刷新统计数据逻辑
}
</script>

<style scoped lang="scss">
.statistics-page {
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