import React from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { Navigate } from 'react-router-dom'

export default function Home() {
  const { user, loading } = useAuth()

  if (loading) return null
  if (user) return <Navigate to="/dashboard" replace />

  return (
    <div className="min-h-screen flex items-center justify-center px-4 text-center">
      <div className="max-w-lg">
        <h1 className="font-display text-4xl text-brass mb-4">SQL Murder Mystery</h1>
        <p className="text-parchment/70 mb-8">
          Learn SQL by solving mysteries. Take timed quizzes to sharpen your skills, then
          write real queries against evidence databases to crack the case.
        </p>
        <div className="flex justify-center gap-4">
          <Link to="/login" className="btn-secondary">Sign in</Link>
          <Link to="/register" className="btn-primary">Get started</Link>
        </div>
      </div>
    </div>
  )
}
