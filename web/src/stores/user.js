// stores/user.js
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || null,
  }),
  actions: {
    login(userData) {
      this.$patch({ userInfo: userData })
      localStorage.setItem('userInfo', JSON.stringify(userData))
    },
    logout() {
      this.$patch({ userInfo: null })
      localStorage.removeItem('userInfo')
      localStorage.removeItem('token')
    }
  }
})