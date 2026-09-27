import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listCases } from '../api/caseApi'
import { extractErrorMessage } from '../api/client'

const difficultyColor = {
  BEGINNER: 'text-green-400 border-green-400/40 bg-green-400/10',
  INTERMEDIATE: 'text-brass border-brass/40 bg-brass/10',
  ADVANCED: 'text-rust border-rust/40 bg-rust/10',
}

export default function CaseList() {
  const [cases, setCases] = useState([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    listCases()
      .then(setCases)
      .catch((err) => setError(extractErrorMessage(err, 'Could not load cases.')))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="min-h-screen photo-shell bg-photo-schema">
      <div className="photo-overlay" />
      <div className="max-w-3xl mx-auto px-4 py-10 page-shell relative z-10">
      <p className="eyebrow">Open investigations</p>
      <h1 className="font-display text-3xl text-brass mb-1">Case-Solving Phase</h1>
      <p className="text-parchment/60 mb-8">
        Query real mystery databases, follow the evidence, and accuse the culprit.
      </p>

      {loading && (
        <p className="text-parchment/60 flex items-center gap-2">
          <span className="h-4 w-4 rounded-full border-2 border-brass/30 border-t-brass animate-spin" />
          Loading cases...
        </p>
      )}
      {error && <p className="text-rust bg-rust/10 border border-rust/30 rounded-md px-3 py-2">{error}</p>}

      <div className="space-y-3">
        {cases.map((c) => (
          <Link
            key={c.id}
            to={`/cases/${c.id}`}
            className="card interactive block"
          >
            <div className="flex items-center justify-between mb-1 gap-3">
              <h3 className="font-display text-lg text-brass flex items-center gap-2">
                <span aria-hidden="true">🗃️</span>
                {c.title}
              </h3>
              <span className={`pill shrink-0 ${difficultyColor[c.difficulty] || 'text-parchment border-parchment/30'}`}>
                {c.difficulty}
              </span>
            </div>
            <p className="text-parchment/60 text-sm mb-2">{c.briefingPreview}</p>
            <p className="text-brass text-sm font-semibold">⭐ {c.pointsReward} points</p>
          </Link>
        ))}
      </div>
      </div>
    </div>
  )
}
