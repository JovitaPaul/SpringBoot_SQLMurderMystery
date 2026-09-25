import apiClient from './client'

export const register = (username, email, password) =>
  apiClient.post('/api/auth/register', { username, email, password }).then((r) => r.data)

export const login = (usernameOrEmail, password) =>
  apiClient.post('/api/auth/login', { usernameOrEmail, password }).then((r) => r.data)

export const getCurrentUser = () =>
  apiClient.get('/api/auth/me').then((r) => r.data)
