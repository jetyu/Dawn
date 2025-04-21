import api from '../utils/axiosConfig';

export const getAllUsers = async (page, size, query = '') => {
  try {
    const response = await api.get('/users', {
      params: {
        page,
        size,
        query
      }
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 根据ID获取用户信息
export const getUserById = async (id) => {
  try {
    const response = await api.get(`/users/${id}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 创建用户
export const createUser = async (data) => {
  try {
    const response = await api.post('/users', data);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 更新用户信息
export const updateUser = async (id, data) => {
  try {
    const response = await api.put(`/users/${id}`, data);
    return response.data;
  } catch (error) {
    throw error;
  }
};

// 删除用户
export const deleteUser = async (id) => {
  try {
    await api.delete(`/api/users/${id}`);
    return true;
  } catch (error) {
    throw error;
  }
};