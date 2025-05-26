<template>
  <div class="dashboard">
    <el-container>
      <el-aside width="200px">
        <el-menu class="dashboard-menu" :default-active="activeMenu">
          <el-menu-item index="1" @click="switchContent('overview')">
            <el-icon>
              <DataLine />
            </el-icon>
            <span>教学概览</span>
          </el-menu-item>
          <el-menu-item index="2" @click="switchContent('grading')">
            <el-icon>
              <Document />
            </el-icon>
            <span>智能阅卷</span>
          </el-menu-item>
          <el-menu-item index="3" @click="switchContent('assistant')">
            <el-icon>
              <ChatDotRound />
            </el-icon>
            <span>学习助手</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <!-- 智能阅卷模块 -->
        <IntelligentGrading v-if="currentContent === 'grading'" />
        <!-- 教学概览模块 -->
        <TeachingOverview v-else-if="currentContent === 'overview'" />
        <!-- 学习助手模块 -->
        <LearningAssistant v-else-if="currentContent === 'assistant'" />
      </el-main>
    </el-container>


  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Document, DataLine, ChatDotRound } from '@element-plus/icons-vue'
import IntelligentGrading from '@/components/grading/IntelligentGrading.vue'
import TeachingOverview from '@/components/overview/TeachingOverview.vue'
import LearningAssistant from '@/components/chat/LearningAssistant.vue'

// 当前选中的菜单项
const activeMenu = ref('1')
// 当前显示的内容
const currentContent = ref('overview')

// 切换内容显示
const switchContent = (content) => {
  currentContent.value = content
  // 切换菜单
  activeMenu.value = content === 'overview' ? '1' : content === 'grading' ? '2' : '3'
  // 切换到智能阅卷重新获取试卷列表
  if (content === 'grading') {
    fetchPaperList()
  }
}

</script>

<style scoped>
.dashboard {
  min-height: calc(100vh - 120px);
  background-color: #f5f7fa;
}

.dashboard-menu {
  height: 100%;
  border-right: solid 1px #e6e6e6;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-buttons {
  display: flex;
  gap: 10px;
}

.grading-card {
  margin-bottom: 20px;
}

.overview-section,
.grading-section {
  padding: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.el-upload__tip {
  color: #909399;
  font-size: 12px;
  margin-top: 8px;
}
</style>