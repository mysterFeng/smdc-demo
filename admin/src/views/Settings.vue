<template>
  <div class="settings-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统设置</span>
          <el-button type="primary" @click="handleSave">
            <el-icon><Check /></el-icon>
            保存设置
          </el-button>
        </div>
      </template>
      
      <div class="page-content">
        <p>系统设置功能开发中...</p>
        <el-button type="primary" @click="$router.push('/dashboard')">
          返回仪表盘
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
const handleSave = () => {
  // 保存设置逻辑
}
</script>

<style scoped lang="scss">
.settings-page {
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