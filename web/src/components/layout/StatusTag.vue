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
    'ACTIVE': 'success',
    'INACTIVE': 'warning',
    'BLOCKED': 'danger',
    'SUSPENDED': 'info'
  }
  return statusTypes[status] || 'info'
}
</script>