import React from 'react'
import { Link, useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

const navItems = [
  { to: '/dashboard', label: 'Dashboard' },
  { to: '/learning', label: 'Learning Phase' },
  { to: '/cases', label: 'Case-Solving' },
  { to: '/leaderboard', label: 'Leaderboard' },
]

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="glass-nav sticky top-0 z-50 px-6 py-3 flex items-center justify-between">
      <Link to="/" className="flex items-center gap-2 font-display text-xl text-brass tracking-wide">
        <span aria-hidden="true">🔎</span>
        <span>
          SQL <span className="text-parchment">Murder Mystery</span>
        </span>
      </Link>

      {user && (
        <div className="flex items-center gap-1 text-sm">
          {navItems.map((item) => {
            const active = location.pathname.startsWith(item.to)
            return (
              <Link
                key={item.to}
                to={item.to}
                className={`px-3 py-2 rounded-md transition-colors ${
                  active ? 'text-brass bg-brass/10' : 'hover:bg-brass/10 hover:text-brass'
                }`}
              >
                {item.label}
              </Link>
            )
          })}
          <span className="mx-2 h-5 w-px bg-brass/20" />
          <span className="flex items-center gap-2 text-parchment/70 px-2">
            <span className="w-6 h-6 rounded-full bg-brass/15 border border-brass/40 flex items-center justify-center text-[11px] text-brass font-semibold uppercase">
              {user.username?.charAt(0)}
            </span>
            {user.username}
          </span>
          <button
            onClick={handleLogout}
            className="px-3 py-2 rounded-md hover:bg-rust/10 hover:text-rust transition-colors"
          >
            Log out
          </button>
        </div>
      )}
    </nav>
  )
}
