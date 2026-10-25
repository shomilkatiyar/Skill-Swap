import api from './axios'

export const searchSkills = (q) => api.get('/skills', { params: { q } }).then(r => r.data)
