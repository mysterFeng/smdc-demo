<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6" v-for="stat in stats" :key="stat.title">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{ background: stat.color }">
              <el-icon :size="24">
                <component :is="stat.icon" />
              </el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ stat.value }}</div>
              <div class="stat-title">{{ stat.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>订单趋势</span>
            </div>
          </template>
          <div ref="orderChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>菜品销量排行</span>
            </div>
          </template>
          <div ref="dishChartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最新订单 -->
    <el-card class="recent-orders">
      <template #header>
        <div class="card-header">
          <span>最新订单</span>
          <el-button type="primary" size="small" @click="$router.push('/orders')">
            查看全部
          </el-button>
        </div>
      </template>
      
      <el-table :data="recentOrders" style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="customerName" label="客户姓名" width="120" />
        <el-table-column prop="totalAmount" label="金额" width="100">
          <template #default="{ row }">
            ¥{{ row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="viewOrder(row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts'

const router = useRouter()

// 统计数据
const stats = ref([
  {
    title: '今日订单',
    value: 156,
    icon: 'Document',
    color: '#409eff'
  },
  {
    title: '今日销售额',
    value: '¥12,580',
    icon: 'Money',
    color: '#67c23a'
  },
  {
    title: '活跃用户',
    value: 1234,
    icon: 'User',
    color: '#e6a23c'
  },
  {
    title: '菜品总数',
    value: 89,
    icon: 'Food',
    color: '#f56c6c'
  }
])

// 最新订单
const recentOrders = ref([
  {
    id: 1,
    orderNo: 'ORD20240101001',
    customerName: '张三',
    totalAmount: 128.00,
    status: 1,
    createTime: '2024-01-01 12:30:00'
  },
  {
    id: 2,
    orderNo: 'ORD20240101002',
    customerName: '李四',
    totalAmount: 89.00,
    status: 2,
    createTime: '2024-01-01 12:25:00'
  },
  {
    id: 3,
    orderNo: 'ORD20240101003',
    customerName: '王五',
    totalAmount: 156.00,
    status: 0,
    createTime: '2024-01-01 12:20:00'
  },
  {
    id: 4,
    orderNo: 'ORD20240101004',
    customerName: '赵六',
    totalAmount: 67.00,
    status: 3,
    createTime: '2024-01-01 12:15:00'
  }
])

const orderChartRef = ref()
const dishChartRef = ref()

// 获取状态类型
const getStatusType = (status) => {
  const types = {
    0: 'warning',   // 待支付
    1: 'primary',   // 已支付
    2: 'success',   // 已完成
    3: 'danger'     // 已取消
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    0: '待支付',
    1: '已支付',
    2: '已完成',
    3: '已取消'
  }
  return texts[status] || '未知'
}

// 查看订单详情
const viewOrder = (order) => {
  router.push(`/orders/${order.id}`)
}

// 初始化订单趋势图表
const initOrderChart = () => {
  const chart = echarts.init(orderChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['00:00', '04:00', '08:00', '12:00', '16:00', '20:00', '24:00']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '订单数',
        type: 'line',
        smooth: true,
        data: [12, 8, 15, 45, 38, 28, 10],
        itemStyle: {
          color: '#409eff'
        },
        areaStyle: {
          color: {
            type: 'linear',
            x: 0,
            y: 0,
            x2: 0,
            y2: 1,
            colorStops: [
              { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
              { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
            ]
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
}

// 初始化菜品销量图表
const initDishChart = () => {
  const chart = echarts.init(dishChartRef.value)
  
  const option = {
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '销量',
        type: 'pie',
        radius: '60%',
        data: [
          { value: 35, name: '宫保鸡丁' },
          { value: 28, name: '麻婆豆腐' },
          { value: 22, name: '水煮鱼' },
          { value: 18, name: '回锅肉' },
          { value: 15, name: '其他' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  chart.setOption(option)
}

onMounted(() => {
  nextTick(() => {
    initOrderChart()
    initDishChart()
  })
})
</script>

<style scoped lang="scss">
.dashboard {
  .stats-row {
    margin-bottom: 20px;
  }
  
  .stat-card {
    .stat-content {
      display: flex;
      align-items: center;
    }
    
    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      margin-right: 16px;
    }
    
    .stat-info {
      .stat-value {
        font-size: 24px;
        font-weight: bold;
        color: #333;
        margin-bottom: 4px;
      }
      
      .stat-title {
        font-size: 14px;
        color: #666;
      }
    }
  }
  
  .charts-row {
    margin-bottom: 20px;
  }
  
  .chart-card {
    .chart-container {
      height: 300px;
    }
  }
  
  .recent-orders {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}
</style> 