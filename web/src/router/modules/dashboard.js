// 管理相关路由
import i18n from '../../i18n'

export default [
  {
    path: '/TeacherDashboard',
    name: 'TeacherDashboard',
    component: () => import('../../views/dashboard/TeacherDashboard.vue'),
    meta: {
      title: '教师仪表盘',
      requiresAuth: true,
      roles: ['Teacher']
    }
  },
  {
    path: '/AdminDashboard',
    name: 'AdminDashboard',
    component: () => import('../../views/dashboard/AdminDashboard.vue'),
    meta: {
      title: `${i18n.global.t('pageTitle.adminDashboardPageTitle')} `
    }
  }
 
]