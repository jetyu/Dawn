import i18n from '../i18n'

/**
 * 将状态值转换为对应的中文显示文本
 * @param {string} status - 状态值
 * @returns {string} - 对应的中文显示文本
 */
export const formatStatus = (status) => {
  const statusKey = status.toLowerCase()
  return i18n.global.t(`label.${statusKey}`) || status
}

/**
 * 将角色值转换为对应的中文显示文本
 * @param {string} role - 角色值
 * @returns {string} - 对应的中文显示文本
 */
export const formatRole = (role) => {
  const roleKey = role.toLowerCase()
  return i18n.global.t(`label.${roleKey}`) || role
}

/**
 * 将角色值转换为对应的中文显示文本
 * @param {string} gender - 角色值
 * @returns {string} - 对应的中文显示文本
 */
export const formatGender = (gender) => {
  const genderKey = gender.toLowerCase()
  return i18n.global.t(`label.${genderKey}`) || gender
}

/**
 * 格式化日期时间为本地字符串
 * @param {string|number|Date} date - 要格式化的日期
 * @returns {string} 格式化后的日期字符串
 */
export const formatDateTime = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  }).replace(/\//g, '-')
}