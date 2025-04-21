// 管理相关路由
import i18n from '../../i18n'

export default [
    {
        path: '/Home',
        name: 'Home',
        component: () => import('../../views/common/Home.vue'),
        meta: {
            title: `${i18n.global.t('global.systemTitle')} - ${i18n.global.t('pageTitle.homePageTitle')} `
        }
    },
    {
        path: '/404',
        name: 'PageNotFound',
        component: () => import('../../views/common/PageNotFound.vue'),
        meta: {
            title: `${i18n.global.t('global.systemTitle')} - ${i18n.global.t('pageTitle.pageNotFoundPageTitle')} `
        }
    },
    {
        path: '/PermissionRestrictions',
        name: 'PermissionRestrictions',
        component: () => import('../../views/common/PermissionRestrictions.vue'),
        meta: {
            title: `${i18n.global.t('global.systemTitle')} - ${i18n.global.t('pageTitle.permissionRestrictionsPageTitle')} `
        }
    }
]