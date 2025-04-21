<template>
  <el-container class="app-wrapper">
    <el-header class="app-header">
      <div class="header-content">
          <h1 class="system-title">{{ $t('global.systemTitle') }}</h1>
          <div class="user-info-container">
            <login-user-status />
          </div>
      </div>
    </el-header>
    <el-main class="app-main">
      <router-view />
    </el-main>
  </el-container>
  <div class="copyright">
        {{ $t('global.copyrightPrefix') }} {{ new Date().getFullYear() }} {{ $t('global.copyrightName') }}
    </div>
</template>

<script setup>
import '@/assets/styles/global.css'
import LoginUserStatus from '@/components/layout/LoginUserStatus.vue'
</script>

<style scoped>
.user-info-container {
  margin-left: auto;
}
</style>
