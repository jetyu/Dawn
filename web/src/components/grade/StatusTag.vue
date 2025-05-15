<template>
  <el-tag :type="getStatusType(status)">
    {{ formatStatus(status) }}
  </el-tag>
</template>

<script setup>
import { formatStatus } from '@/utils/layoutFormat'

const props = defineProps({
  status: {
    type: String,
    required: true
  }
})

const getStatusType = (status) => {
  const statusTypes = {
    // 用户状态
    'ACTIVE': 'success',
    'INACTIVE': 'warning',
    'BLOCKED': 'danger',
    'SUSPENDED': 'info',
    // 试卷批改状态
    'UNGRADED': 'info',   
    'GRADED': 'success'   
  }
  return statusTypes[status] || 'info'
}
</script>