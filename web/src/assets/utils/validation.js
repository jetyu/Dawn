// 身份证号码验证规则
export const idCardRules = [
  { required: true, message: '请输入身份证号码', trigger: 'blur' },
  { pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/, message: '请输入正确的身份证号码', trigger: 'blur' }
]

// 电话号码验证规则
export const phoneRules = [
  { required: true, message: '请输入电话号码', trigger: 'blur' },
  { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的电话号码', trigger: 'blur' }
]

// 姓名验证规则
export const nameRules = [
  { required: true, message: '请输入姓名', trigger: 'blur' },
  { min: 2, max: 100, message: '姓名长度应在2-100个字符之间', trigger: 'blur' }
]

// 地址验证规则
export const addressRules = [
  { required: true, message: '请输入地址', trigger: 'blur' },
  { min: 3, max: 100, message: '地址长度应在3-100个字符之间', trigger: 'blur' }
]

// 性别验证规则
export const genderRules = [
  { required: true, message: '请选择性别', trigger: 'change' }
]

// 用户名验证规则
export const usernameRules = [
  { required: true, message: '请输入用户名', trigger: 'blur' },
  { min: 4, max: 20, message: '登录用户名长度应在4-20个字符之间', trigger: 'blur' }
]

// 密码验证规则
export const passwordRules = [
  { required: true, message: '请输入密码', trigger: 'blur' },
  { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' }
]

// 邮箱验证规则
export const emailRules = [
  { required: false, message: '请输入邮箱地址', trigger: 'blur' },
  { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
]

// 验证码验证规则
export const verificationCodeRules = [
  { required: true, message: '请输入验证码', trigger: 'blur' },
  { len: 6, message: '验证码长度应为6位', trigger: 'blur' }
]

// 确认密码验证规则
export const validateConfirmPassword = (password) => {
  return (rule, value, callback) => {
    if (value === '') {
      callback(new Error('请再次输入密码'))
    } else if (value !== password) {
      callback(new Error('两次输入密码不一致'))
    } else {
      callback()
    }
  }
}

  
// 登录信息表单验证规则
export const loginRules = {
  username: usernameRules,
  password: passwordRules
}
// 用户信息表单验证规则
export const userRules = {
  username: usernameRules,
  name: nameRules,
  email: emailRules,
  phone: phoneRules,
}
// 注册表单验证规则
export const registerRules = {
  username: usernameRules,
  password: passwordRules,
  email: emailRules,
  phone: phoneRules,
  confirmPassword: [{ validator: (rule, value, callback) => validateConfirmPassword(value)(rule, value, callback), trigger: 'blur' }]
}

// 验证规则
export const forgotPasswordRules = {
  verificationCode: usernameRules,
  newPassword: passwordRules,
  email: emailRules,
  confirmPassword: [{ validator: (rule, value, callback) => validateConfirmPassword(value)(rule, value, callback), trigger: 'blur' }]
}
