import api from '../utils/axiosConfig';


export const newMessage = async (request) => {
  try {
    const response = await api.post('/api/chat/newMessage', {
      userInputContent: request.userInputContent,
      grade: request.grade,
      subject: request.subject,
      mode: request.mode
    });
    return response.data;
  } catch (error) {
    throw error;
  }
};
