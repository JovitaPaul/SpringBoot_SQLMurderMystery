import apiClient from './client'

export const getMyProgress = () =>
  apiClient.get('/api/progress/me').then((r) => r.data)

export const recordCompletion = (type, referenceId, referenceTitle, points) =>
  apiClient
    .post('/api/progress/events', { type, referenceId, referenceTitle, points })
    .then((r) => r.data)
