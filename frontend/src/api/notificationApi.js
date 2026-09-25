import apiClient from './client'

export const listNotifications = () =>
  apiClient.get('/api/notifications').then((r) => r.data)

export const markNotificationRead = (id) =>
  apiClient.patch(`/api/notifications/${id}/read`).then((r) => r.data)
