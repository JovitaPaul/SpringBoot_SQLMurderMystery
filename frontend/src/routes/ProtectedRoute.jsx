import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function ProtectedRoute({ children }) {
  const { user, loading } = useAuth()

  if (loading) {
    return (
      <div className="flex flex-col items-center justify-center h-screen text-brass gap-3">
        <span className="h-8 w-8 rounded-full border-2 border-brass/30 border-t-brass animate-spin" />
        <span className="text-sm text-parchment/60 tracking-wide">Loading case file...</span>
      </div>
    )
  }
  if (!user) {
    return <Navigate to="/login" replace />
  }
  return children
}
