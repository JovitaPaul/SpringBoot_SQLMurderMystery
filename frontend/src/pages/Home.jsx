import React from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { Navigate } from 'react-router-dom'

export default function Home() {
  const { user, loading } = useAuth()

  if (loading) return null
  if (user) return <Navigate to="/dashboard" replace />

  return (
    <div className="min-h-screen flex items-center justify-center px-4 text-center relative overflow-hidden photo-shell bg-photo-evidence">
      <div className="photo-overlay" />

      {/* decorative floating particles — purely visual */}
      <div className="absolute inset-0 pointer-events-none">
        {[...Array(10)].map((_, i) => (
          <span
            key={i}
            className="floating-dot"
            style={{
              width: `${4 + (i % 3) * 3}px`,
              height: `${4 + (i % 3) * 3}px`,
              left: `${(i * 37) % 100}%`,
              top: `${(i * 53) % 100}%`,
              animationDelay: `${i * 0.7}s`,
              animationDuration: `${8 + (i % 5)}s`,
            }}
          />
        ))}
      </div>

      <div className="max-w-xl page-shell relative z-10">
        <div className="w-16 h-16 mx-auto mb-6 relative">
          <span className="absolute inset-0 rounded-full border-2 border-brass/40 animate-pulse" />
          <span className="absolute inset-2 bg-black/50 rounded-full flex items-center justify-center text-2xl">
            🔎
          </span>
        </div>

        <p className="eyebrow justify-center">Open case files</p>
        <h1 className="font-display text-5xl text-brass mb-4 leading-tight">
          SQL Murder Mystery
        </h1>
        <p className="text-parchment/70 mb-10 leading-relaxed">
          Learn SQL by solving mysteries. Take timed quizzes to sharpen your skills, then
          write real queries against evidence databases to crack the case.
        </p>
        <div className="flex justify-center gap-4">
          <Link to="/login" className="btn-secondary">Sign in</Link>
          <Link to="/register" className="btn-primary">Get started</Link>
        </div>

        <div className="divider-dashed max-w-xs mx-auto" />
        <p className="text-xs uppercase tracking-[0.2em] text-parchment/40">
          Quizzes &middot; Real databases &middot; Leaderboard
        </p>
      </div>
    </div>
  )
}
