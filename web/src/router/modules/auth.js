// 认证相关路由
import i18n from '../../i18n'

export default [
  {
    path: '/login',
    name: 'login',
    component: () => import('../../views/auth/Login.vue'),
    meta: {
      title: `${i18n.global.t('global.systemTitle')} - ${i18n.global.t('pageTitle.loginPageTitle')} `
    }
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../../views/auth/Register.vue'),
    meta: {
      title: `${i18n.global.t('global.systemTitle')} - ${i18n.global.t('pageTitle.registerPageTitle')} `
    }
  },
  {
    path: '/forgot-password',
    name: 'forgot-password',
    component: () => import('../../views/auth/ForgotPassword.vue'),
    meta: {
      title: `${i18n.global.t('global.systemTitle')} - ${i18n.global.t('pageTitle.forgotPasswordPageTitle')} `
    }
  }
]