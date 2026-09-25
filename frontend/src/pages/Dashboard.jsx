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
    <div className="max-w-4xl mx-auto px-4 py-10">
      <h1 className="font-display text-3xl text-brass mb-1">Welcome, {user?.username}</h1>
      <p className="text-parchment/60 mb-8">Here's where your investigation stands.</p>

      {loading && <p className="text-parchment/60">Loading your case file...</p>}
      {error && <p className="text-rust">{error}</p>}

      {progress && (
        <>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
            <StatCard label="Total points" value={progress.totalPoints} />
            <StatCard label="Quizzes passed" value={progress.quizzesPassed} />
            <StatCard label="Cases solved" value={progress.casesSolved} />
            <StatCard
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
            <Link to="/learning" className="card hover:border-brass transition-colors">
              <h3 className="font-display text-xl text-brass mb-2">Learning Phase</h3>
              <p className="text-parchment/60 text-sm">
                Sharpen your SQL with timed quizzes on SELECT, JOINs, GROUP BY and more.
              </p>
            </Link>
            <Link to="/cases" className="card hover:border-brass transition-colors">
              <h3 className="font-display text-xl text-brass mb-2">Case-Solving Phase</h3>
              <p className="text-parchment/60 text-sm">
                Query real mystery databases and accuse the culprit.
              </p>
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
                    <span className="text-brass">+{activity.pointsAwarded} pts</span>
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

function StatCard({ label, value }) {
  return (
    <div className="card text-center">
      <p className="text-2xl font-display text-brass">{value}</p>
      <p className="text-xs text-parchment/50 mt-1 uppercase tracking-wide">{label}</p>
    </div>
  )
}
