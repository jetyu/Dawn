<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <h2 class="register-title">{{ $t('pageTitle.registerPageTitle') }}</h2>
      </template>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef">
        <el-form-item prop="role">
          <el-select v-model="registerForm.role" placeholder="请选择用户角色">
            <template #prefix>
              <el-icon>
                <Avatar />
              </el-icon>
            </template>
            <el-option label="老师" value="Teacher" />
            <el-option label="学生" value="Student" />
            <el-option label="家长" value="Parent" />
          </el-select>
        </el-form-item>
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入登录用户名" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input v-model="registerForm.nickname" placeholder="请输入昵称" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" :prefix-icon="Lock"
            show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请确认密码" :prefix-icon="Lock"
            show-password />
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号" :prefix-icon="Iphone" />
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱" :prefix-icon="Message" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" class="register-button" @click="handleRegister">{{ $t('button.register')
            }}</el-button>
        </el-form-item>
        <div class="register-links">
          <el-link type="primary" :underline="false" @click="handleLogin">{{ $t('button.returnLogin') }}</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { User, Lock, Message, Iphone, Avatar } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { registerRules } from '@/assets/utils/validation'

const router = useRouter()
const registerFormRef = ref(null)

const registerForm = reactive({
  username: '',
  nickname: '',
  password: '',
  confirmPassword: '',
  phone: '',
  email: '',
  role: 'Teacher' // 默认角色为患者
})

const rules = computed(() => ({
  ...registerRules,
  confirmPassword: [{
    validator: (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== registerForm.password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    },
    trigger: 'blur'
  }]
}))

const handleRegister = async () => {
  if (!registerFormRef.value) return

  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const { confirmPassword, ...userData } = registerForm
        const response = await fetch('/api/auth/register', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          },
          body: JSON.stringify(userData)
        })

        if (!response.ok) {
          const errorData = await response.text()
          try {
            const parsedError = JSON.parse(errorData)
            throw new Error(parsedError.message || '注册失败');
          } catch (parseError) {
            throw new Error(errorData || '注册失败');
          }
        }
        const data = await response.json()
        ElMessage.success('注册成功')
        router.push('/login')
      } catch (error) {
        let errorMessage = '注册失败'
        if (error.message === 'Failed to fetch') {
          errorMessage = '无法连接到服务器，请确保服务器已启动'
        } else {
          errorMessage = error.message || '注册请求失败，请检查网络连接或稍后重试'
        }
        ElMessage.error(errorMessage)
      }
    }
  })
}

const handleLogin = () => {
  router.push('/login')
}
</script>



<style scoped>
.register-container {
  min-height: calc(100vh - 160px);
  padding: 20px 0;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
}

.register-card {
  width: 400px;
}

.register-title {
  text-align: center;
  margin: 0;
  color: #303133;
}

.register-button {
  width: 100%;
}

.register-links {
  display: flex;
  justify-content: center;
  margin-top: 16px;
}
</style>