import apiClient from './client'

export const executeQuery = (targetSchema, sql) =>
  apiClient.post('/api/query/execute', { targetSchema, sql }).then((r) => r.data)
