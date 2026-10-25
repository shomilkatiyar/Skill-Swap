import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})

// Add token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('skillswap_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// Handle errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Logout on unauthorized
      localStorage.removeItem('skillswap_token')
      localStorage.removeItem('skillswap_user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default api
