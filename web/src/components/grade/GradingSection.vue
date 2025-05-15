<template>
  <el-card class="grading-card">
    <template #header>
      <div class="card-header">
        <h3>智能阅卷系统</h3>
        <div class="header-buttons">
          <el-button type="primary" @click="$emit('upload')">上传试卷</el-button>
          <el-button type="info" @click="$emit('refresh')">
            <el-icon>
              <Refresh />
            </el-icon>
            刷新
          </el-button>
        </div>
      </div>
    </template>
    <PaperTable :paper-list="paperList" @start-grading="$emit('start-grading', $event)" @view-result="$emit('view-result', $event)" />
  </el-card>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import PaperTable from './PaperTable.vue'
import { Refresh } from '@element-plus/icons-vue'
const props = defineProps({
  paperList: { type: Array, required: true }
})
defineEmits(['upload', 'refresh', 'start-grading', 'view-result'])
</script>

<style scoped>
.grading-card {
  margin-bottom: 24px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-buttons > * {
  margin-left: 8px;
}
</style>
