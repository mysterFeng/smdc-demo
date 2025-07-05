<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

onMounted(() => {
  // 检查登录状态
  const token = localStorage.getItem('token')
  if (token) {
    userStore.setToken(token)
    // 获取用户信息
    userStore.getUserInfo()
  }
})
</script>

<style>
#app {
  height: 100vh;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  background-color: #f5f5f5;
}
</style> 