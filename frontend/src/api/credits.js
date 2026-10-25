import api from './axios'

export const getCreditHistory = () => api.get('/credits/history').then(r => r.data)
