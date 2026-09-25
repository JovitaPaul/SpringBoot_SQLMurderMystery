import apiClient from './client'

export const getTopLeaderboard = (limit = 10) =>
  apiClient.get(`/api/leaderboard/top?limit=${limit}`).then((r) => r.data)

export const getMyRank = () =>
  apiClient.get('/api/leaderboard/me').then((r) => r.data)
