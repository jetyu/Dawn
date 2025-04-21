import api from '../utils/axiosConfig';

// 获取试卷列表
export const getPaperList = async () => {
  try {
    const response = await api.post('/api/grading/papers');
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 上传试卷
export const uploadPaper = async (data) => {
  try {
    const response = await api.post('/api/grading/upload', data, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.message || '上传失败，请稍后重试');
    }
    throw new Error('网络异常，请检查网络连接');
  }
};

// 开始批改试卷
export const startGrading = async (paperId) => {
  try {
    const response = await api.post('/api/grading/grade/${paperId}',paperId);
    return response.data;
  } catch (error) {
    throw error;
  }
};


// 获取批改结果
export const getGradingResult = async (paperId) => {
  try {
    const response = await api.post('/api/grading/result/${paperId}',paperId);
    return response.data;
  } catch (error) {
    throw error;
  }
};