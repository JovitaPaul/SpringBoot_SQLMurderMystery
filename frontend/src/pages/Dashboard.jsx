import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { getMyProgress } from '../api/progressApi'
import { getMyRank } from '../api/leaderboardApi'
import { extractErrorMessage } from '../api/client'

export default function Dashboard() {
  const { user } = useAuth()
  const [progress, setProgress] = useState(null)
  const [rank, setRank] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([getMyProgress(), getMyRank()])
      .then(([progressData, rankData]) => {
        setProgress(progressData)
        setRank(rankData)
      })
      .catch((err) => setError(extractErrorMessage(err, 'Could not load your progress.')))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="min-h-screen photo-shell bg-photo-topics">
      <div className="photo-overlay" />
      <div className="max-w-4xl mx-auto px-4 py-10 page-shell relative z-10">
      <p className="eyebrow">Case file</p>
      <h1 className="font-display text-3xl text-brass mb-1">Welcome, {user?.username}</h1>
      <p className="text-parchment/60 mb-8">Here's where your investigation stands.</p>

      {loading && (
        <p className="text-parchment/60 flex items-center gap-2">
          <span className="h-4 w-4 rounded-full border-2 border-brass/30 border-t-brass animate-spin" />
          Loading your case file...
        </p>
      )}
      {error && <p className="text-rust bg-rust/10 border border-rust/30 rounded-md px-3 py-2">{error}</p>}

      {progress && (
        <>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
            <StatCard icon="⭐" label="Total points" value={progress.totalPoints} />
            <StatCard icon="📘" label="Quizzes passed" value={progress.quizzesPassed} />
            <StatCard icon="🔎" label="Cases solved" value={progress.casesSolved} />
            <StatCard
              icon="🔥"
              label="Current streak"
              value={`${progress.currentStreak} day${progress.currentStreak === 1 ? '' : 's'}`}
            />
          </div>

          {rank && (
            <div className="card mb-8 flex items-center justify-between">
              <div>
                <p className="text-parchment/60 text-sm">Leaderboard rank</p>
                <p className="text-2xl text-brass font-display">
                  {rank.rank ? `#${rank.rank}` : 'Unranked'}
                </p>
              </div>
              <Link to="/leaderboard" className="btn-secondary">View leaderboard</Link>
            </div>
          )}

          <div className="grid md:grid-cols-2 gap-4 mb-8">
            <Link to="/learning" className="card interactive group">
              <div className="flex items-center gap-3 mb-2">
                <span className="w-9 h-9 rounded-md bg-brass/10 border border-brass/30 flex items-center justify-center text-lg">📘</span>
                <h3 className="font-display text-xl text-brass">Learning Phase</h3>
              </div>
              <p className="text-parchment/60 text-sm">
                Sharpen your SQL with timed quizzes on SELECT, JOINs, GROUP BY and more.
              </p>
              <span className="text-brass/70 text-xs mt-3 inline-block group-hover:translate-x-1 transition-transform">
                Enter &rarr;
              </span>
            </Link>
            <Link to="/cases" className="card interactive group">
              <div className="flex items-center gap-3 mb-2">
                <span className="w-9 h-9 rounded-md bg-rust/10 border border-rust/30 flex items-center justify-center text-lg">🔎</span>
                <h3 className="font-display text-xl text-brass">Case-Solving Phase</h3>
              </div>
              <p className="text-parchment/60 text-sm">
                Query real mystery databases and accuse the culprit.
              </p>
              <span className="text-brass/70 text-xs mt-3 inline-block group-hover:translate-x-1 transition-transform">
                Enter &rarr;
              </span>
            </Link>
          </div>

          <div className="card">
            <h3 className="font-display text-lg text-brass mb-4">Recent activity</h3>
            {progress.recentActivity.length === 0 ? (
              <p className="text-parchment/50 text-sm">
                Nothing yet — go pass a quiz or crack a case.
              </p>
            ) : (
              <ul className="divide-y divide-brass/10">
                {progress.recentActivity.map((activity, i) => (
                  <li key={i} className="py-3 flex items-center justify-between text-sm">
                    <div>
                      <span className="text-parchment/40 mr-2">
                        {activity.type === 'CASE' ? '🔎' : '📘'}
                      </span>
                      {activity.referenceTitle || activity.type}
                    </div>
                    <span className="text-brass font-semibold">+{activity.pointsAwarded} pts</span>
                  </li>
                ))}
              </ul>
            )}
          </div>
        </>
      )}
      </div>
    </div>
  )
}

function StatCard({ icon, label, value }) {
  return (
    <div className="stat-tile">
      <p className="text-xl mb-1" aria-hidden="true">{icon}</p>
      <p className="text-2xl font-display text-brass">{value}</p>
      <p className="text-xs text-parchment/50 mt-1 uppercase tracking-wide">{label}</p>
    </div>
  )
}
