import api from '../utils/axiosConfig';


export const newMessage = async (request) => {
  try {
    const response = await api.post('/api/chat/newMessage', request.userInputContent);
    return response.data;
  } catch (error) {
    throw error;
  }
};
