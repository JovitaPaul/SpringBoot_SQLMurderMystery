import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { extractErrorMessage } from '../api/client'

export default function Register() {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const { register } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSubmitting(true)
    try {
      await register(username, email, password)
      navigate('/dashboard')
    } catch (err) {
      setError(extractErrorMessage(err, 'Registration failed.'))
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
              left: `${(i * 47) % 100}%`,
              top: `${(i * 31) % 100}%`,
              animationDelay: `${i * 0.5}s`,
            }}
          />
        ))}
      </div>

      <div className="card w-full max-w-sm page-shell relative z-10">
        <div className="w-12 h-12 mx-auto mb-4 relative">
          <span className="absolute inset-0 rounded-full border-2 border-brass/40 animate-pulse" />
          <span className="absolute inset-1.5 bg-black/50 rounded-full flex items-center justify-center text-lg">
            🗂️
          </span>
        </div>

        <h1 className="font-display text-2xl text-brass mb-1 text-center">Join the investigation</h1>
        <p className="text-parchment/60 text-sm mb-6 text-center">Create an account to start solving cases.</p>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm mb-1 text-parchment/80">Username</label>
            <input
              className="input-field"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              minLength={3}
              maxLength={50}
              required
              autoFocus
            />
          </div>
          <div>
            <label className="block text-sm mb-1 text-parchment/80">Email</label>
            <input
              type="email"
              className="input-field"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>
          <div>
            <label className="block text-sm mb-1 text-parchment/80">Password</label>
            <input
              type="password"
              className="input-field"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              minLength={8}
              required
            />
            <p className="text-xs text-parchment/40 mt-1">At least 8 characters.</p>
          </div>

          {error && (
            <p className="text-rust text-sm bg-rust/10 border border-rust/30 rounded-md px-3 py-2">{error}</p>
          )}

          <button type="submit" className="btn-primary w-full" disabled={submitting}>
            {submitting ? 'Creating account...' : 'Create account'}
          </button>
        </form>

        <p className="text-sm text-parchment/60 mt-6 text-center">
          Already have an account?{' '}
          <Link to="/login" className="text-brass hover:underline">
            Sign in
          </Link>
        </p>
      </div>
    </div>
  )
}
