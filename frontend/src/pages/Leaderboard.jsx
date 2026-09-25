import React, { useEffect, useState } from 'react'
import { useAuth } from '../context/AuthContext'
import { getTopLeaderboard, getMyRank } from '../api/leaderboardApi'
import { extractErrorMessage } from '../api/client'

export default function Leaderboard() {
  const { user } = useAuth()
  const [entries, setEntries] = useState([])
  const [myRank, setMyRank] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const calls = [getTopLeaderboard(20)]
    if (user) calls.push(getMyRank())

    Promise.all(calls)
      .then(([top, mine]) => {
        setEntries(top)
        if (mine) setMyRank(mine)
      })
      .catch((err) => setError(extractErrorMessage(err, 'Could not load the leaderboard.')))
      .finally(() => setLoading(false))
  }, [user])

  return (
    <div className="max-w-2xl mx-auto px-4 py-10">
      <h1 className="font-display text-3xl text-brass mb-1">Leaderboard</h1>
      <p className="text-parchment/60 mb-8">Top detectives, ranked in real time.</p>

      {loading && <p className="text-parchment/60">Loading...</p>}
      {error && <p className="text-rust">{error}</p>}

      {myRank && (
        <div className="card mb-6 flex items-center justify-between">
          <span className="text-parchment/70">Your rank</span>
          <span className="text-brass font-display text-xl">
            {myRank.rank ? `#${myRank.rank} · ${myRank.score} pts` : 'Unranked — go earn some points!'}
          </span>
        </div>
      )}

      <div className="card">
        {entries.length === 0 && !loading ? (
          <p className="text-parchment/50 text-sm">No scores yet. Be the first to solve a case!</p>
        ) : (
          <ol className="divide-y divide-brass/10">
            {entries.map((entry) => (
              <li
                key={entry.username}
                className={`py-3 flex items-center justify-between ${
                  user && entry.username === user.username ? 'text-brass' : 'text-parchment/80'
                }`}
              >
                <span>
                  <span className="inline-block w-8 text-parchment/40">#{entry.rank}</span>
                  {entry.username}
                </span>
                <span>{entry.score} pts</span>
              </li>
            ))}
          </ol>
        )}
      </div>
    </div>
  )
}
