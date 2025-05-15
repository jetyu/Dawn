<template>
  <el-dialog
    :model-value="visible"
    @update:model-value="val => $emit('update:visible', val)"
    title="上传试卷"
    width="400px"
    @close="$emit('update:visible', false)"
  >
    <el-form :model="form">
      <el-form-item label="试卷名称" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="文件">
        <el-upload :before-upload="beforeFileUpload" :on-success="handleFileUpload" :show-file-list="false">
          <el-button type="primary">选择文件</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="$emit('close')">取消</el-button>
      <el-button type="primary" @click="$emit('submit', form)">上传</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
const props = defineProps({
  visible: { type: Boolean, required: true },
  form: { type: Object, required: true },
  beforeFileUpload: { type: Function, required: true },
  handleFileUpload: { type: Function, required: true }
})
const emit = defineEmits(['close', 'submit', 'update:visible'])
</script>
