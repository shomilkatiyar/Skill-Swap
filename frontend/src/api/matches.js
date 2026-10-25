import api from './axios'

export const getMatches = () => api.get('/matches').then(r => r.data)
