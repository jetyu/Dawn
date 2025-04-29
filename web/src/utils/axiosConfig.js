import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router';
import { useUserStore } from '@/stores/user';


const API_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // 处理 token 过期
    if (error.response && error.response.status === 401) {
      // 清除 Pinia store 的 userInfo
      try {
        const userStore = useUserStore();
        userStore.logout && userStore.logout();
      } catch (e) {
        // Pinia 未初始化时清理 localStorage
        localStorage.removeItem('userInfo');
        localStorage.removeItem('token');
      }
      // 显示提示信息
      ElMessage.error('登录已过期，请重新登录');
      // 跳转到登录页
      router.push('/login');
      return Promise.reject(new Error('登录已过期，请重新登录'));
    }

    // 获取错误信息
    let errorMessage = '系统异常错误';
    if (error.response && error.response.data) {
      if (typeof error.response.data === 'string') {
        errorMessage = error.response.data;
      } else if (error.response.data.message) {
        errorMessage = error.response.data.message;
      }
    } else if (error.message) {
      errorMessage = error.message;
    }
    return Promise.reject(new Error(errorMessage));
  }
);

export default api;