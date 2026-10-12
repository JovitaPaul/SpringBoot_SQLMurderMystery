import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { getMyActivity } from '../api/progressApi'
import { getMyRank } from '../api/leaderboardApi'
import { useCompletion, fraction } from '../hooks/useCompletion'

const plural = (n, word) => `${n} ${word}${n === 1 ? '' : 's'}`

export default function Profile() {
  const { user } = useAuth()
  const { loading, progress, totalCases, totalQuizzes } = useCompletion()
  const [rank, setRank] = useState(null)
  const [activity, setActivity] = useState([])

  useEffect(() => {
    getMyRank().then(setRank).catch(() => {})
    getMyActivity(50).then(setActivity).catch(() => {})
  }, [])

  const solved = progress?.casesSolved ?? 0
  const passed = progress?.quizzesPassed ?? 0

  return (
    <div className="max-w-4xl mx-auto px-4 py-10">
      <div className="card mb-8 flex items-center gap-5">
        <div className="h-16 w-16 rounded-full bg-brass/20 border border-brass/60 flex items-center justify-center text-3xl font-display text-brass">
          {user?.username?.[0]?.toUpperCase()}
        </div>
        <div className="min-w-0">
          <h1 className="font-display text-3xl text-brass truncate">{user?.username}</h1>
          <p className="text-parchment/60 text-sm">
            {user?.email}
            {user?.role ? ` · ${String(user.role).toLowerCase()}` : ''}
          </p>
        </div>
        {rank?.rank && (
          <Link to="/leaderboard" className="ml-auto text-right hover:opacity-80">
            <p className="text-xs uppercase tracking-wide text-parchment/50">Leaderboard</p>
            <p className="text-2xl font-display text-brass">#{rank.rank}</p>
          </Link>
        )}
      </div>

      {loading && <p className="text-parchment/60">Loading your case file...</p>}
      {!loading && !progress && <p className="text-rust">Could not load your progress.</p>}

      {progress && (
        <>
          <div className="grid grid-cols-2 md:grid-cols-3 gap-4 mb-8">
            <Stat label="Total points" value={progress.totalPoints} />
            <Stat label="Cases solved" value={fraction(solved, totalCases)} />
            <Stat label="Quizzes passed" value={fraction(passed, totalQuizzes)} />
            <Stat label="Current streak" value={plural(progress.currentStreak, 'day')} />
            <Stat label="Longest streak" value={plural(progress.longestStreak, 'day')} highlight />
            <Stat label="Last active" value={progress.lastActivityDate ?? '—'} />
          </div>

          <div className="grid md:grid-cols-2 gap-4 mb-8">
            <ProgressBar label="Cases" done={solved} total={totalCases} to="/cases" />
            <ProgressBar label="Quizzes" done={passed} total={totalQuizzes} to="/learning" />
          </div>

          <div className="card">
            <h3 className="font-display text-lg text-brass mb-4">Activity history</h3>
            {activity.length === 0 ? (
              <p className="text-parchment/50 text-sm">Nothing yet — go pass a quiz or crack a case.</p>
            ) : (
              <ul className="divide-y divide-brass/10">
                {activity.map((a, i) => (
                  <li key={i} className="py-3 flex items-center justify-between gap-3 text-sm">
                    <div className="min-w-0">
                      <span className="mr-2">{a.type === 'CASE' ? '🔎' : '📘'}</span>
                      <span className="text-parchment/90">{a.referenceTitle || a.type}</span>
                      <span className="block text-xs text-parchment/40 ml-6">
                        {a.type === 'CASE' ? 'Case solved' : 'Quiz passed'} ·{' '}
                        {a.occurredAt ? new Date(a.occurredAt).toLocaleString() : ''}
                      </span>
                    </div>
                    <span className="text-brass shrink-0">+{a.pointsAwarded} pts</span>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </>
      )}
    </div>
  )
}

function Stat({ label, value, highlight }) {
  return (
    <div className={`card text-center ${highlight ? 'border-brass' : ''}`}>
      <p className="text-2xl font-display text-brass">{value}</p>
      <p className="text-xs text-parchment/50 mt-1 uppercase tracking-wide">{label}</p>
    </div>
  )
}

function ProgressBar({ label, done, total, to }) {
  const pct = total ? Math.min(100, Math.round((done / total) * 100)) : 0
  return (
    <Link to={to} className="card hover:border-brass transition-colors block">
      <div className="flex items-center justify-between text-sm mb-2">
        <span className="text-parchment/70">{label}</span>
        <span className="text-brass">{fraction(done, total)}</span>
      </div>
      <div className="h-2 rounded-full bg-black/40 overflow-hidden">
        <div className="h-full bg-brass" style={{ width: `${pct}%` }} />
      </div>
    </Link>
  )
}
