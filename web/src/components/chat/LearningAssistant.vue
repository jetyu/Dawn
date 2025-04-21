<template>
  <div class="learning-assistant">
    <div class="chat-container" ref="chatContainer">
      <div v-for="(message, index) in messages" :key="index" class="message" :class="message.type">
        <div class="message-content">
          {{ message.content }}
        </div>
      </div>
    </div>
    <div class="input-container">
      <el-input
        v-model="userInput"
        type="textarea"
        :rows="2"
        placeholder="请输入您的问题..."
        @keyup.enter.native.exact="handleSend"
      />
      <el-button type="primary" @click="handleSend" :loading="loading">
        发送
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { newMessage } from '@/api/chat'

const messages = ref([])
const userInput = ref('')
const loading = ref(false)
const chatContainer = ref(null)

const scrollToBottom = async () => {
  await nextTick()
  if (chatContainer.value) {
    chatContainer.value.scrollTop = chatContainer.value.scrollHeight
  }
}

const handleSend = async () => {
  if (!userInput.value.trim()) {
    ElMessage.warning('请输入内容')
    return
  }

  // 添加用户消息
  messages.value.push({
    type: 'user',
    content: userInput.value
  })

  loading.value = true
  try {
    const response = await newMessage({
      userInputContent: userInput.value
    })
    
    messages.value.push({
      type: 'assistant',
      content: response.result.output.content
    })
    
    userInput.value = ''
    await scrollToBottom()
  } catch (error) {
    ElMessage.error('获取AI回复失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.learning-assistant {
  height: 100%;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
  border-radius: 8px;
  padding: 20px;
}

.chat-container {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  margin-bottom: 20px;
}

.message {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}

.message.user {
  align-items: flex-end;
}

.message.assistant {
  align-items: flex-start;
}

.message-content {
  max-width: 80%;
  padding: 10px 15px;
  border-radius: 8px;
  word-wrap: break-word;
}

.user .message-content {
  background-color: #409eff;
  color: white;
}

.assistant .message-content {
  background-color: white;
  color: #333;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.input-container {
  display: flex;
  gap: 10px;
}

.input-container .el-textarea {
  flex: 1;
}
</style>