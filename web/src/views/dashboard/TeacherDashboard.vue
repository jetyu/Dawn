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
        <div v-if="currentContent === 'grading'" class="grading-section">
          <el-card class="grading-card">
            <template #header>
              <div class="card-header">
                <h3>智能阅卷系统</h3>
                <div class="header-buttons">
                  <el-button type="primary" @click="handleUpload">上传试卷</el-button>
                  <el-button type="info" @click="refreshPaperList">
                    <el-icon>
                      <Refresh />
                    </el-icon>
                    刷新
                  </el-button>
                </div>
              </div>
            </template>

            <!-- 试卷列表 -->
            <el-table :data="paperList" style="width: 100%">
              <el-table-column prop="name" label="试卷名称" />
              <el-table-column prop="subject" label="科目" />
              <el-table-column prop="uploadTime" label="上传时间" />
              <el-table-column prop="status" label="批改状态">
                <template #default="{ row }">
                  <el-tag :type="row.status === '已完成' ? 'success' : 'warning'">
                    {{ row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template #default="{ row }">
                  <el-button type="primary" size="small" @click="handleStartGrading(row)"
                    :disabled="row.status === '已完成'">智能批改</el-button>
                  <el-button type="info" size="small" @click="viewResult(row)"
                    :disabled="row.status !== '已完成'">查看结果</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>

        <!-- 教学概览模块 -->
        <div v-else-if="currentContent === 'overview'" class="overview-section">
          <el-card>
            <template #header>
              <h3>教学概览</h3>
            </template>
            <div class="overview-content">
              <!-- 待实现：教学概览内容 -->
            </div>
          </el-card>
        </div>

        <!-- 学习助手模块 -->
        <div v-else-if="currentContent === 'assistant'" class="assistant-section">
          <el-card class="assistant-card">
            <template #header>
              <h3>AI学习助手</h3>
            </template>
            <LearningAssistant />
          </el-card>
        </div>
      </el-main>
    </el-container>

    <!-- 上传试卷对话框 -->
    <el-dialog v-model="uploadDialogVisible" title="上传试卷" width="30%">
      <el-form :model="uploadForm" label-width="80px">
        <el-form-item label="试卷名称">
          <el-input v-model="uploadForm.name" placeholder="请输入试卷名称" />
        </el-form-item>
        <el-form-item label="科目">
          <el-select v-model="uploadForm.subject" placeholder="请选择科目">
            <el-option label="语文" value="语文" />
            <el-option label="数学" value="数学" />
            <el-option label="英语" value="英语" />
          </el-select>
        </el-form-item>
        <el-form-item label="试卷文件">
          <el-upload class="upload-demo" :action="null" :http-request="handleFileUpload"
            :before-upload="beforeFileUpload" :limit="1">
            <el-button type="primary">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">
                支持上传jpg/png/pdf格式的试卷文件，单个文件大小不超过20MB
              </div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="uploadDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitUpload">确认上传</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Document, DataLine, ChatDotRound, Refresh } from '@element-plus/icons-vue'
import LearningAssistant from '@/components/chat/LearningAssistant.vue'
import { ElMessage } from 'element-plus'
import { getPaperList, uploadPaper, startGrading, getGradingResult } from '@/api/paper'

// 当前选中的菜单项
const activeMenu = ref('1')
// 当前显示的内容
const currentContent = ref('overview')

// 切换内容显示
const switchContent = (content) => {
  currentContent.value = content
  activeMenu.value = content === 'overview' ? '1' : content === 'grading' ? '2' : '3'

  // 如果切换到智能阅卷标签，重新获取试卷列表
  if (content === 'grading') {
    fetchPaperList()
  }
}

// 试卷列表数据
const paperList = ref([])

// 获取试卷列表
const fetchPaperList = async () => {
  const response = await getPaperList();

  const papers = Array.isArray(response) ? response : [];

  paperList.value = papers.map(item => {
    const uploadTime = item.uploadTime;
    let formattedTime = '';

    if (uploadTime) {
      if (typeof uploadTime === 'object') {
        // 处理 LocalDateTime 对象
        formattedTime = `${uploadTime.year}-${String(uploadTime.monthValue).padStart(2, '0')}-${String(uploadTime.dayOfMonth).padStart(2, '0')} ${String(uploadTime.hour).padStart(2, '0')}:${String(uploadTime.minute).padStart(2, '0')}`;
      } else if (typeof uploadTime === 'string') {
        // 处理字符串格式的日期
        formattedTime = uploadTime;
      }
    }

    const paper = {
      id: item.id,
      name: item.name,
      subject: item.subject,
      uploadTime: formattedTime,
      status: item.status || '未批改'
    };
    return paper;
  })
}

// 上传对话框相关
const uploadDialogVisible = ref(false)
  const uploadForm = reactive({
    name: '',
    subject: '',
    file: null
  })

  // 处理上传按钮点击
  const handleUpload = () => {
    uploadDialogVisible.value = true
  }

  // 文件上传前的验证
  const beforeFileUpload = (file) => {
    const allowedTypes = ['image/jpeg', 'image/png', 'application/pdf']
    if (!allowedTypes.includes(file.type)) {
      ElMessage.warning('只支持上传jpg/png/pdf格式的文件')
      return false
    }
    const maxSize = 20 * 1024 * 1024
    if (file.size > maxSize) {
      ElMessage.warning('文件大小不能超过20MB')
      return false
    }
    return true
  }

  // 处理文件上传
  const handleFileUpload = async (options) => {
    const { file } = options
    uploadForm.file = file
  }

  // 提交上传
  const submitUpload = async () => {
    if (!uploadForm.name || !uploadForm.subject || !uploadForm.file) {
      ElMessage.warning('请填写完整信息')
      return
    }

    try {
      await uploadPaper(uploadForm.file, uploadForm.name, uploadForm.subject)
      ElMessage.success('上传成功')
      uploadDialogVisible.value = false
      fetchPaperList() // 刷新试卷列表
    } catch (error) {
      ElMessage.error(error.message || '上传失败')
    }
  }


  async function handleStartGrading(row) {
    try {
      await startGrading(row.id)
      ElMessage.success('智能批改完成')
      fetchPaperList()
    } catch (error) {
      ElMessage.error(error.message || '批改失败')
    }
  }

  // 查看批改结果
  const viewResult = async (paper) => {
    try {
      const { data } = await getGradingResult(paper.id)
      ElMessage.success('获取结果成功')
    } catch (error) {
      ElMessage.error(error.message || '获取结果失败')
    }
  }

  // 刷新试卷列表
  const refreshPaperList = async () => {
    try {
      await fetchPaperList()
      ElMessage.success('试卷列表刷新成功')
    } catch (error) {
      ElMessage.error('刷新试卷列表失败')
    }
  }

  // 在组件挂载时获取数据
  onMounted(() => {
    if (currentContent.value === 'grading') {
      fetchPaperList()
    }
  })

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