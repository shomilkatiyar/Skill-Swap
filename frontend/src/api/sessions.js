import api from './axios'

export const createSession = (data) => api.post('/sessions', data).then(r => r.data)
export const getMySessions = () => api.get('/sessions').then(r => r.data)
export const updateSessionStatus = (id, status) => api.patch(`/sessions/${id}/status`, { status }).then(r => r.data)
