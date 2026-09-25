import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

const linkClass = 'px-3 py-2 rounded-md hover:bg-brass/10 transition-colors'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="bg-black/40 border-b border-brass/30 px-6 py-4 flex items-center justify-between">
      <Link to="/" className="font-display text-xl text-brass tracking-wide">
        SQL Murder Mystery
      </Link>

      {user && (
        <div className="flex items-center gap-1 text-sm">
          <Link to="/dashboard" className={linkClass}>Dashboard</Link>
          <Link to="/learning" className={linkClass}>Learning Phase</Link>
          <Link to="/cases" className={linkClass}>Case-Solving</Link>
          <Link to="/leaderboard" className={linkClass}>Leaderboard</Link>
          <span className="mx-2 text-parchment/50">|</span>
          <span className="text-parchment/70">{user.username}</span>
          <button onClick={handleLogout} className={linkClass}>Log out</button>
        </div>
      )}
    </nav>
  )
}
