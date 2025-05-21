<template>
  <el-table :data="paperList" style="width: 100%">
    <el-table-column prop="name" label="试卷名称"/>
    <el-table-column prop="subject" label="科目"/>
    <el-table-column prop="uploadTime" label="上传时间"/>
    <el-table-column prop="status" label="批改状态">
      <template #default="{ row }">
        <StatusTag :status="row.status"/>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="200">
      <template #default="{ row }">
        <el-button type="primary" size="small" @click="$emit('start-grading', row)"
                   :disabled="row.status === 'graded'">智能批改
        </el-button>
        <el-button type="info" size="small" @click="$emit('view-result', row)"
                   :disabled="row.status !== 'ungraded'">查看结果
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
import {defineEmits, defineProps} from 'vue'
import StatusTag from '../layout/StatusTag.vue'

const props = defineProps({
  paperList: {type: Array, required: true}
})
defineEmits(['start-grading', 'view-result'])
</script>
