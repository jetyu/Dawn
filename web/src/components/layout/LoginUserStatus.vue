<template>
  <div class="header">
    <div class="header-right">
      <el-dropdown v-if="userInfo" @command="handleCommand" trigger="click">
        <div class="user-info">
          <div class="avatar">
            <span class="avatar-text">{{ userInfo.name.charAt(0).toUpperCase() }}</span>
          </div>
          <span class="username">{{ userInfo.name }}</span>
          <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu class="modern-dropdown">
            <el-dropdown-item command="settings" class="dropdown-item">
              <el-icon><setting /></el-icon>
              <span>设置</span>
            </el-dropdown-item>
            <el-dropdown-item divided command="logout" class="dropdown-item logout">
              <el-icon><switch-button /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ArrowDown, User, Setting, SwitchButton } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { storeToRefs } from 'pinia'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const { userInfo } = storeToRefs(userStore) // 直接使用响应式引用

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    ElMessage.success('退出成功')
    router.push('/login')
  }
}
</script>

<style scoped>
.header {
  height: 60px;
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  background: linear-gradient(135deg, #1e88e5 0%, #1565c0 100%);
  padding: 6px 12px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.user-info:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(30, 136, 229, 0.2);
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 8px;
}

.avatar-text {
  color: #ffffff;
  font-size: 16px;
  font-weight: 500;
}

.username {
  color: #ffffff;
  font-size: 14px;
  font-weight: 500;
  margin-right: 4px;
}

.el-icon--right {
  color: #ffffff;
  font-size: 12px;
  margin-left: 4px;
  transition: transform 0.3s ease;
}

.el-dropdown-menu.modern-dropdown {
  border-radius: 8px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  padding: 8px;
}

.dropdown-item {
  display: flex;
  align-items: center;
  padding: 10px 16px;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.dropdown-item:hover {
  background-color: #f5f7fa;
}

.dropdown-item .el-icon {
  margin-right: 8px;
  font-size: 16px;
}

.dropdown-item.logout {
  color: #f56c6c;
}

.dropdown-item.logout:hover {
  background-color: #fff5f5;
}
</style>