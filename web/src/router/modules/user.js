// 用户管理相关路由
import i18n from '../../i18n'

export default [
  {
    path: '/UserManagementList',
    name: 'userManagementList',
    component: () => import('../../views/user/UserManagementList.vue'),
    meta: {
      title: `${i18n.global.t('pageTitle.userManagementListPageTitle')} - ${i18n.global.t('global.systemTitle')} `
    }
  }
]