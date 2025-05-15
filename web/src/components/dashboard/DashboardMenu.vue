<template>
  <el-menu class="dashboard-menu" :default-active="activeMenu">
    <el-menu-item
      v-for="item in menuItems"
      :key="item.index"
      :index="item.index"
      @click="() => $emit('switch', item.key)"
    >
      <el-icon>
        <component :is="item.icon" />
      </el-icon>
      <span>{{ item.label }}</span>
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
const props = defineProps({
  menuItems: { type: Array, required: true },
  activeMenu: { type: String, required: true }
})
defineEmits(['switch'])
</script>

<style scoped>
.dashboard-menu {
  height: 100%;
}
</style>
