import { createRouter, createWebHistory } from 'vue-router'
import authRoutes from './modules/auth'
import userRoutes from './modules/user'
import dashboardRoutes from './modules/dashboard'

const router = createRouter({
	history: createWebHistory(import.meta.env.BASE_URL),
	routes: [
		{
			path: '/',
			redirect: '/login'
		},
		...authRoutes,
		...userRoutes,
		...dashboardRoutes,

	]
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
	// 设置页面标题
	if (to.meta && to.meta.title) {
		document.title = to.meta.title
	}

	// 检查用户登录状态
	const token = localStorage.getItem('token')
	const userInfo = JSON.parse(localStorage.getItem('userInfo'))

	// 如果路由需要认证
	if (to.meta.requiresAuth) {
		// 如果用户未登录，重定向到登录页
		if (!token || !userInfo) {
			next('/login')
			return
		}

		// 如果路由需要特定角色权限
		if (to.meta.roles && !to.meta.roles.includes(userInfo.role)) {
			next('/PermissionRestrictions')
			return
		}
	}

	// 如果用户已登录且尝试访问登录页
	if (token && userInfo && (to.path === '/login' || to.path === '/')) {
		// 根据用户角色重定向到对应仪表盘
		switch (userInfo.role) {
			case 'Admin':
				next('/AdminDashboard')
				break
			case 'Parent':
				next('/ParentDashboard')
				break
			case 'Teacher':
				next('/TeacherDashboard')
				break
			case 'Student':
				next('/StudentDashboard')
				break
			default:
				next('/PermissionRestrictions')
		}
		return
	}

	next()
})


export default router