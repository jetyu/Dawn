<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2 class="login-title">{{ $t('pageTitle.userloginPageTitle') }}</h2>
      </template>
      <el-form class="login-form" :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" :placeholder="$t('placeholder.username')" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" :placeholder="$t('placeholder.password')"
            :prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="submit-button" @click="handleLogin">{{ $t('button.login') }}</el-button>
        </el-form-item>
        <div class="login-links">
          <el-link type="primary" :underline="false" @click="handleRegister">{{ $t('button.registerAccount')
          }}</el-link>
          <el-link type="primary" :underline="false" @click="handleForgotPassword">{{
            $t('button.forgotPassword') }}</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { loginRules } from '@/assets/utils/validation'
import { useUserStore } from '@/stores/user'
import { useI18n } from 'vue-i18n'
import { login } from '@/api/auth'

const router = useRouter()
const userStore = useUserStore()
const { t } = useI18n()
const loginFormRef = ref(null)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = loginRules

const handleLogin = async () => {
  if (!loginFormRef.value) return
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const { token, ...userData } = await login(loginForm)
        localStorage.setItem('token', token)
        userStore.login(userData)

        ElMessage.success(t('successMessage.loginSuccess'))

        // 根据用户角色跳转到不同的仪表盘
        switch (userData.role) {
          case 'Admin':
            router.push('/AdminDashboard')
            break   
          case 'Parent':
            router.push('/ParentDashboard')
            break
          case 'Teacher':
            router.push('/TeacherDashboard')
            break
          case 'Student':
            router.push('/StudentDashboard')
            break
          default:
            router.push('/PermissionRestrictions')
        }
      } catch (error) {
        if (error && error.message) {
          ElMessage.error(error.message);
        } else if (error) {
          if (typeof error === 'object' && error.message) {
            ElMessage.error(error.message);
          } else {
            ElMessage.error(t('errorMessage.loginFailed'));
          }
        } else {
          ElMessage.error(t('errorMessage.loginFailed'));
        }
      }
    }
  })
}

const handleRegister = () => {
  router.push('/register')
}

const handleForgotPassword = () => {
  router.push('/forgot-password')
}
</script>
<style scoped>
.login-form {
  width: 100%;
  max-width: 400px;
  margin: 0 auto;
}

.login-form .el-form-item {
  margin-bottom: 20px;
}

.login-form .el-button {
  width: 100%;
}

.login-form .login-links {
  display: flex;
  justify-content: space-between;
  margin-top: 16px;
}

.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 160px);
  padding: 20px;
  background-color: #f5f7fa;
}

.login-card {
  width: 100%;
  max-width: 400px;
}

.login-title {
  text-align: center;
  color: #2196F3;
  margin: 0;
}
</style>
