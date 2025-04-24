<template>
  <div class="learning-assistant-with-history">
    <aside class="history-sidebar">
      <div class="history-header">
        <span>历史对话</span>
        <el-button type="primary" size="small" @click="createNewConversation">新建对话</el-button>
      </div>
      <el-scrollbar class="history-list">
        <div v-for="(conv, idx) in conversations" :key="conv.id"
          :class="['history-item', { active: idx === currentConversationIndex }]" @click="switchConversation(idx)">
          <div class="history-title">
            <el-input v-if="conv.editing" v-model="conv.title" size="small" @blur="finishRename(idx)"
              @keyup.enter="finishRename(idx)" />
            <span v-else @dblclick="startRename(idx)">{{ conv.title || '未命名会话' }}</span>
          </div>
          <div class="history-actions">
            <el-button icon="el-icon-delete" size="small" circle @click.stop="deleteConversation(idx)" />
          </div>
        </div>
      </el-scrollbar>
    </aside>
    <main class="learning-assistant">
      <div class="settings-container">
        <div class="setting-item">
          <label class="setting-label">年级：</label>
          <el-select v-model="grade" placeholder="选择年级" class="setting-select">
            <el-option label="小学" value="primary"></el-option>
            <el-option label="初中" value="junior"></el-option>
            <el-option label="高中" value="senior"></el-option>
          </el-select>
        </div>
        <div class="setting-item">
          <label class="setting-label">学科：</label>
          <el-select v-model="subject" placeholder="选择学科" class="setting-select">
            <el-option label="语文" value="语文"></el-option>
            <el-option label="数学" value="数学"></el-option>
            <el-option label="英语" value="英语"></el-option>
          </el-select>
        </div>
        <div class="setting-item">
          <label class="setting-label">模式：</label>
          <el-select v-model="mode" placeholder="选择模式" class="setting-select">
            <el-option label="简洁模式" value="simple"></el-option>
            <el-option label="详细模式" value="detailed"></el-option>
          </el-select>
        </div>
      </div>
      <div class="chat-container" ref="chatContainer">
        <div v-for="(message, index) in currentConversation.messages" :key="index" class="message"
          :class="message.type">
          <div class="message-content">
            {{ message.content }}
          </div>
        </div>
      </div>
      <div class="input-container">
        <el-input v-model="userInput" type="textarea" :rows="2" placeholder="请输入您的问题..."
          @keyup.enter.native.exact="handleSend" />
        <el-button type="primary" @click="handleSend" :loading="loading">
          发送
        </el-button>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { newMessage } from '@/api/chat'

let conversationIdSeed = 1
function createDefaultConversation() {
  return {
    id: Date.now() + Math.random(),
    title: '新会话',
    messages: [
      {
        type: 'assistant',
        content: '您好，我是AI学习助手，有什么可以帮您？'
      }
    ],
    editing: false,
    isNew: true // 标记为新建
  }
}

const conversations = ref([])
const currentConversationIndex = ref(0)
const currentConversation = computed(() => conversations.value[currentConversationIndex.value])

// 恢复历史
const savedConvs = localStorage.getItem('ai_conversations')
const savedIdx = localStorage.getItem('ai_conversations_idx')
if (savedConvs) {
  try {
    conversations.value = JSON.parse(savedConvs)
    currentConversationIndex.value = savedIdx ? Number(savedIdx) : 0
    // 防止越界
    if (currentConversationIndex.value >= conversations.value.length) {
      currentConversationIndex.value = 0
    }
  } catch {
    conversations.value = [createDefaultConversation()]
    currentConversationIndex.value = 0
  }
} else {
  conversations.value = [createDefaultConversation()]
}

// 监听保存
import { watch } from 'vue'
watch(conversations, (val) => {
  localStorage.setItem('ai_conversations', JSON.stringify(val))
}, { deep: true })
watch(currentConversationIndex, (val) => {
  localStorage.setItem('ai_conversations_idx', String(val))
})

const userInput = ref('')
const loading = ref(false)
const chatContainer = ref(null)

const grade = ref('通用')
const subject = ref('通用')
const mode = ref('通用')

const scrollToBottom = async () => {
  await nextTick()
  if (chatContainer.value) {
    chatContainer.value.scrollTop = chatContainer.value.scrollHeight
  }
}

function createNewConversation() {
  conversations.value.unshift(createDefaultConversation())
  currentConversationIndex.value = 0
  userInput.value = ''
}

function switchConversation(idx) {
  currentConversationIndex.value = idx
  userInput.value = ''
  nextTick(scrollToBottom)
}

function deleteConversation(idx) {
  if (conversations.value.length === 1) {
    ElMessage.warning('至少保留一个会话')
    return
  }
  conversations.value.splice(idx, 1)
  if (currentConversationIndex.value >= conversations.value.length) {
    currentConversationIndex.value = conversations.value.length - 1
  }
}

function startRename(idx) {
  conversations.value[idx].editing = true
  nextTick(() => {
    const input = document.querySelectorAll('.history-title input')[idx]
    if (input) input.focus()
  })
}

function finishRename(idx) {
  conversations.value[idx].editing = false
  if (!conversations.value[idx].title.trim()) {
    conversations.value[idx].title = '未命名会话'
  }
}

const handleSend = async () => {
  // 如果当前是新建会话且没有用户消息，则用第一个问题作为标题
  if (
    currentConversation.value.isNew &&
    currentConversation.value.messages.length === 1 &&
    currentConversation.value.messages[0].type === 'assistant' &&
    userInput.value.trim()
  ) {
    let title = userInput.value.trim().slice(0, 15)
    if (userInput.value.trim().length > 15) title += '...'
    currentConversation.value.title = title
    delete currentConversation.value.isNew
  }
  if (!userInput.value.trim()) {
    ElMessage.warning('请输入内容')
    return
  }
  if (!grade.value || !subject.value || !mode.value) {
    ElMessage.warning('请先选择年级、学科和模式')
    return
  }
  // 新会话：如果当前会话只有欢迎语且用户输入为空，则自动新建会话
  if (currentConversation.value.messages.length === 1 && currentConversation.value.messages[0].type === 'assistant' && !userInput.value.trim()) {
    createNewConversation()
  }
  // 保存输入内容，先清空输入框
  const toSendContent = userInput.value
  userInput.value = ''
  currentConversation.value.messages.push({
    type: 'user',
    content: toSendContent
  })
  loading.value = true
  try {
    const response = await newMessage({
      userInputContent: toSendContent,
      grade: grade.value,
      subject: subject.value,
      mode: mode.value
    })
    console.log('AI response:', response)
    let aiContent = ''
    if (response?.result?.output?.content) {
      aiContent = response.result.output.content
    } else if (response?.output?.content) {
      aiContent = response.output.content
    } else if (response?.content) {
      aiContent = response.content
    } else if (typeof response === 'string') {
      aiContent = response
    } else {
      aiContent = JSON.stringify(response)
    }
    currentConversation.value.messages.push({
      type: 'assistant',
      content: aiContent
    })
    userInput.value = ''
    await scrollToBottom()
  } catch (error) {
    console.error('AI error:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.learning-assistant-with-history {
  display: flex;
  height: 100%;
  min-height: 300px;
  max-height: 100%;
  background: #f5f7fa;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.06);

}

html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: auto;
}

#app {
  height: 100%;
  min-height: 80vh;
  overflow: auto;
}

.history-sidebar {
  width: 220px;
  background: #fff;
  border-radius: 8px 0 0 8px;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
  padding: 18px 8px 8px 8px;
}

.history-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
  font-weight: bold;
  font-size: 15px;
}

.history-list {
  flex: 1;
  overflow-y: auto;
}

.history-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 6px;
  border-radius: 6px;
  cursor: pointer;
  margin-bottom: 4px;
  transition: background 0.2s;
}

.history-item.active {
  background: #e6f0fa;
}

.history-title {
  flex: 1;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.history-actions {
  display: flex;
  align-items: center;
  margin-left: 4px;
}

.learning-assistant {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
  border-radius: 0 8px 8px 0;
  padding: 20px;
}

.settings-container {
  display: flex;
  gap: 24px;
  margin-bottom: 15px;
}

.setting-item {
  display: flex;
  align-items: center;
  min-width: 180px;
}

.setting-label {
  min-width: 48px;
  margin-right: 10px;
  color: #333;
  font-size: 15px;
  white-space: nowrap;
}

.setting-select {
  min-width: 120px;
  width: 100%;
  max-width: 180px;
}

.chat-container {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  margin-bottom: 1px;
}

.input-container {
  display: flex;
  gap: 10px;
  margin-top: 18px;
  padding-top: 10px;
  border-top: 1.5px solid #f0f0f0;
  background: #fafbfc;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.03);
}

.input-container .el-textarea {
  flex: 1;
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

@media (max-width: 900px) {
  .learning-assistant-with-history {
    flex-direction: column;
  }

  .history-sidebar {
    width: 100%;
    border-radius: 8px 8px 0 0;
    flex-direction: row;
    padding: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  }

  .learning-assistant {
    border-radius: 0 0 8px 8px;
    padding: 10px;
  }
}
</style>