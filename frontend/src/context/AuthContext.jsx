import React, { createContext, useContext, useEffect, useState } from 'react'
import * as authApi from '../api/authApi'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const stored = localStorage.getItem('smm_user')
    return stored ? JSON.parse(stored) : null
  })
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem('smm_token')
    if (!token) {
      setLoading(false)
      return
    }
    // Re-validate the stored token against auth-service on load, rather than trusting
    // whatever was last cached in localStorage.
    authApi
      .getCurrentUser()
      .then((freshUser) => {
        setUser(freshUser)
        localStorage.setItem('smm_user', JSON.stringify(freshUser))
      })
      .catch(() => {
        localStorage.removeItem('smm_token')
        localStorage.removeItem('smm_user')
        setUser(null)
      })
      .finally(() => setLoading(false))
  }, [])

  const login = async (username, password) => {
    const { token, user: userData } = await authApi.login(username, password)
    localStorage.setItem('smm_token', token)
    localStorage.setItem('smm_user', JSON.stringify(userData))
    setUser(userData)
    return userData
  }

  const register = async (username, email, password) => {
    const { token, user: userData } = await authApi.register(username, email, password)
    localStorage.setItem('smm_token', token)
    localStorage.setItem('smm_user', JSON.stringify(userData))
    setUser(userData)
    return userData
  }

  const logout = () => {
    localStorage.removeItem('smm_token')
    localStorage.removeItem('smm_user')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within an AuthProvider')
  return ctx
}
