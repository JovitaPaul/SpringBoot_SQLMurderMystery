import apiClient from './client'

export const listQuizzes = () =>
  apiClient.get('/api/quizzes').then((r) => r.data)

export const getQuiz = (quizId) =>
  apiClient.get(`/api/quizzes/${quizId}`).then((r) => r.data)

export const submitAttempt = (quizId, answers, timeTakenSeconds) =>
  apiClient
    .post(`/api/quizzes/${quizId}/attempts`, { answers, timeTakenSeconds })
    .then((r) => r.data)

export const getAttemptHistory = (quizId) =>
  apiClient.get(`/api/quizzes/${quizId}/attempts`).then((r) => r.data)
