import api from './axios'

export const getMyProfile = () => api.get('/users/me').then(r => r.data)
export const getUserProfile = (id) => api.get(`/users/${id}`).then(r => r.data)
export const updateProfile = (data) => api.put('/users/me', data).then(r => r.data)
export const addOfferedSkill = (data) => api.post('/users/me/skills-offered', data).then(r => r.data)
export const addWantedSkill = (data) => api.post('/users/me/skills-wanted', data).then(r => r.data)
export const removeOfferedSkill = (skillId) => api.delete(`/users/me/skills-offered/${skillId}`).then(r => r.data)
export const removeWantedSkill = (skillId) => api.delete(`/users/me/skills-wanted/${skillId}`).then(r => r.data)
