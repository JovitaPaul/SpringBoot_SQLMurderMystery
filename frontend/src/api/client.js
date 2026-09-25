import axios from 'axios'

// The gateway is the single entry point (see README's "Ports & Gateway routes" table) —
// the frontend never talks to a backend service directly.
const GATEWAY_URL = import.meta.env.VITE_API_GATEWAY_URL || 'http://localhost:8080'

export const apiClient = axios.create({
  baseURL: GATEWAY_URL,
  headers: { 'Content-Type': 'application/json' },
})

apiClient.interceptors.request.use((config) => {
  const token = localStorage.getItem('smm_token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token missing/expired — clear it so ProtectedRoute sends the learner back to
      // Login instead of silently retrying with a dead token.
      localStorage.removeItem('smm_token')
      localStorage.removeItem('smm_user')
    }
    return Promise.reject(error)
  },
)

/** Pulls a readable message out of a Spring Boot GlobalExceptionHandler error body,
 *  falling back gracefully for network errors etc. Every backend service in this repo
 *  returns { timestamp, status, error, message } on failure. */
export function extractErrorMessage(error, fallback = 'Something went wrong. Please try again.') {
  return error?.response?.data?.message || error?.message || fallback
}

export default apiClient
