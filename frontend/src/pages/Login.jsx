import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { extractErrorMessage } from '../api/client'

export default function Login() {
  const [usernameOrEmail, setUsernameOrEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      await login(usernameOrEmail, password)
      navigate('/dashboard')
    } catch (err) {
      setError(extractErrorMessage(err, 'Login failed — check your credentials.'))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="auth-shell photo-shell bg-photo-evidence">
      <div className="photo-overlay" />
      <div className="absolute inset-0 pointer-events-none">
        {[...Array(8)].map((_, i) => (
          <span
            key={i}
            className="floating-dot"
            style={{
              width: `${3 + (i % 3) * 2}px`,
              height: `${3 + (i % 3) * 2}px`,
              left: `${(i * 41) % 100}%`,
              top: `${(i * 29) % 100}%`,
              animationDelay: `${i * 0.6}s`,
            }}
          />
        ))}
      </div>

      <div className="card w-full max-w-sm page-shell relative z-10">
        <div className="w-12 h-12 mx-auto mb-4 relative">
          <span className="absolute inset-0 rounded-full border-2 border-brass/40 animate-pulse" />
          <span className="absolute inset-1.5 bg-black/50 rounded-full flex items-center justify-center text-lg">
            🕵️
          </span>
        </div>

        <h1 className="font-display text-2xl text-brass mb-1 text-center">Welcome back, Detective</h1>
        <p className="text-parchment/60 text-sm mb-6 text-center">Sign in to continue your investigation.</p>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm mb-1 text-parchment/80">Username or email</label>
            <input
              className="input-field"
              value={usernameOrEmail}
              onChange={(e) => setUsernameOrEmail(e.target.value)}
              required
              autoFocus
            />
          </div>
          <div>
            <label className="block text-sm mb-1 text-parchment/80">Password</label>
            <input
              type="password"
              className="input-field"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          {error && (
            <p className="text-rust text-sm bg-rust/10 border border-rust/30 rounded-md px-3 py-2">{error}</p>
          )}

          <button type="submit" className="btn-primary w-full" disabled={submitting}>
            {submitting ? 'Signing in...' : 'Sign in'}
          </button>
        </form>

        <p className="text-sm text-parchment/60 mt-6 text-center">
          New here?{' '}
          <Link to="/register" className="text-brass hover:underline">
            Create an account
          </Link>
        </p>
      </div>
    </div>
  )
}
