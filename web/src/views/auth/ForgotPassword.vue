<template>
  <div class="forgot-password-container">
    <el-card class="forgot-password-card">
      <template #header>
        <h2 class="forgot-password-title">重置密码</h2>
      </template>
      <el-form :model="resetForm" :rules="rules" ref="resetFormRef">
        <el-form-item prop="phone">
          <el-input
            v-model="resetForm.phone"
            placeholder="请输入注册手机号"
            :prefix-icon="Message"
          />
        </el-form-item>
        <el-form-item prop="verificationCode">
          <div class="verification-code-container">
            <el-input
              v-model="resetForm.verificationCode"
              placeholder="请输入验证码"
              :prefix-icon="Key"
            />
            <el-button
              type="primary"
              :disabled="isCodeSent"
              @click="handleSendCode"
            >{{ codeButtonText }}</el-button>
          </div>
        </el-form-item>
        <el-form-item prop="newPassword">
          <el-input
            v-model="resetForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="resetForm.confirmPassword"
            type="password"
            placeholder="请确认新密码"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="reset-button" @click="handleReset">重置密码</el-button>
        </el-form-item>
        <div class="reset-links">
          <el-link type="primary" :underline="false" @click="handleBackToLogin">返回登录</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { Message, Key, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { forgotPasswordRules } from '@/assets/utils/validation'


const router = useRouter()
const resetFormRef = ref(null)
const isCodeSent = ref(false)
const countdown = ref(60)
const codeButtonText = ref('发送验证码')

const resetForm = reactive({
  phone: '',
  verificationCode: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = forgotPasswordRules

const startCountdown = () => {
  isCodeSent.value = true
  const timer = setInterval(() => {
    countdown.value--
    codeButtonText.value = `${countdown.value}秒后重试`
    if (countdown.value <= 0) {
      clearInterval(timer)
      isCodeSent.value = false
      countdown.value = 60
      codeButtonText.value = '发送验证码'
    }
  }, 1000)
}

const handleSendCode = async () => {
  // TODO: 实现发送验证码逻辑
  ElMessage.success('验证码已发送到邮箱')
  startCountdown()
}

const handleReset = async () => {
  if (!resetFormRef.value) return

  await resetFormRef.value.validate((valid) => {
    if (valid) {
      // TODO: 实现重置密码逻辑
      ElMessage.success('密码重置成功')
      router.push('/login')
    }
  })
}

const handleBackToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.forgot-password-container {
  min-height: calc(100vh - 160px);
  padding: 20px 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
}

.forgot-password-card {
  width: 400px;
}

.forgot-password-title {
  text-align: center;
  margin: 0;
  color: #303133;
}

.verification-code-container {
  display: flex;
  gap: 10px;
}

.verification-code-container .el-input {
  flex: 1;
}

.reset-button {
  width: 100%;
}

.reset-links {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}
</style>@/assets/utils/validation@/assets/utils/validation