<template>
  <div class="categories-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分类管理</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加分类
          </el-button>
        </div>
      </template>
      
      <div class="page-content">
        <p>分类管理功能开发中...</p>
        <el-button type="primary" @click="$router.push('/dashboard')">
          返回仪表盘
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
const handleAdd = () => {
  // 添加分类逻辑
}
</script>

<style scoped lang="scss">
.categories-page {
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