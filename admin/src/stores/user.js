import { defineStore } from 'pinia'
import { ref } from 'vue'
import { login, getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref({})
  const permissions = ref([])

  // 设置token
  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  // 清除token
  const clearToken = () => {
    token.value = ''
    localStorage.removeItem('token')
  }

  // 设置用户信息
  const setUserInfo = (info) => {
    userInfo.value = info
  }

  // 登录
  const loginAction = async (loginForm) => {
    try {
      const response = await login(loginForm)
      const { token: newToken, userInfo: info } = response.data
      
      setToken(newToken)
      setUserInfo(info)
      
      return response
    } catch (error) {
      throw error
    }
  }

  // 获取用户信息
  const getUserInfoAction = async () => {
    try {
      const response = await getUserInfo()
      setUserInfo(response.data)
      return response
    } catch (error) {
      throw error
    }
  }

  // 登出
  const logout = () => {
    clearToken()
    userInfo.value = {}
    permissions.value = []
  }

  // 检查是否有权限
  const hasPermission = (permission) => {
    return permissions.value.includes(permission)
  }

  return {
    token,
    userInfo,
    permissions,
    setToken,
    clearToken,
    setUserInfo,
    loginAction,
    getUserInfoAction,
    logout,
    hasPermission
  }
}) 