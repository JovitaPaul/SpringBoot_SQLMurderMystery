import apiClient from './client'

export const listCases = () =>
  apiClient.get('/api/cases').then((r) => r.data)

export const getCase = (caseId) =>
  apiClient.get(`/api/cases/${caseId}`).then((r) => r.data)

export const submitAccusation = (caseId, suspectId, reasoning) =>
  apiClient
    .post(`/api/cases/${caseId}/accusations`, { suspectId, reasoning })
    .then((r) => r.data)
