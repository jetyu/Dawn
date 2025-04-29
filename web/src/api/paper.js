import api from '../utils/axiosConfig';

// 获取试卷列表
export const getPaperList = async () => {
  try {
    const response = await api.get('/api/grading/list');
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 上传试卷
// file, name, subject 三个参数都必须传
export const uploadPaper = async (file, name, subject) => {
  try {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('name', name);
    formData.append('subject', subject);
    const response = await api.post('/api/grading/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    return response.data;
  } catch (error) {
    if (error.response) {
      const errorMessage = error.response.data.message || '上传失败，请稍后重试';
      if (errorMessage.includes('文件大小超过限制')) {
        throw new Error('文件大小超过限制，请确保文件大小不超过20MB');
      }
      throw new Error(errorMessage);
    }
    throw new Error('网络异常，请检查网络连接');
  }
};

// 开始批改试卷
// paperId: 试卷ID，userId: 用户ID
export const startGrading = async (paperId) => {
  try {
    const response = await api.post(`/api/grading/grade/${paperId}`);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.message || '批改失败，请稍后重试');
    }
    throw new Error('网络异常，请检查网络连接2');
  }
};

// 获取批改结果
export const getGradingResult = async (paperId) => {
  try {
    const response = await api.get(`/api/grading/result/${paperId}`);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.message || '获取批改结果失败');
    }
    throw new Error('网络异常，请检查网络连接3');
  }
};