import axios from 'axios';

const API_BASE_URL = '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data),
};

// User API
export const userAPI = {
  getMe: () => api.get('/users/me'),
  updateMe: (data) => api.put('/users/me', data),
  getUser: (userId) => api.get(`/users/${userId}`),
  searchUsers: (name) => api.get(`/users/search?name=${name}`),
  getMatches: () => api.get('/users/matches'),
};

// Skill API
export const skillAPI = {
  getAll: () => api.get('/skills/browse'),
  getById: (id) => api.get(`/skills/browse/${id}`),
  getCategories: () => api.get('/skills/categories'),
  getByCategory: (category) => api.get(`/skills/browse/category/${category}`),
  search: (query) => api.get(`/skills/search?q=${query}`),
  create: (data) => api.post('/skills', data),
  addToProfile: (data) => api.post('/skills/me', data),
  removeFromProfile: (skillId, type) => api.delete(`/skills/me/${skillId}?type=${type}`),
  getTeachers: (skillId) => api.get(`/skills/${skillId}/teachers`),
  getLearners: (skillId) => api.get(`/skills/${skillId}/learners`),
  getRecommendations: () => api.get('/skills/recommendations'),
};

export default api;