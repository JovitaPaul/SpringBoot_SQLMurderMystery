import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listCases } from '../api/caseApi'
import { extractErrorMessage } from '../api/client'

const difficultyColor = {
  BEGINNER: 'text-green-400',
  INTERMEDIATE: 'text-brass',
  ADVANCED: 'text-rust',
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
    <div className="max-w-3xl mx-auto px-4 py-10">
      <h1 className="font-display text-3xl text-brass mb-1">Case-Solving Phase</h1>
      <p className="text-parchment/60 mb-8">
        Query real mystery databases, follow the evidence, and accuse the culprit.
      </p>

      {loading && <p className="text-parchment/60">Loading cases...</p>}
      {error && <p className="text-rust">{error}</p>}

      <div className="space-y-3">
        {cases.map((c) => (
          <Link
            key={c.id}
            to={`/cases/${c.id}`}
            className="card block hover:border-brass transition-colors"
          >
            <div className="flex items-center justify-between mb-1">
              <h3 className="font-display text-lg text-brass">{c.title}</h3>
              <span className={`text-sm ${difficultyColor[c.difficulty] || 'text-parchment'}`}>
                {c.difficulty}
              </span>
            </div>
            <p className="text-parchment/60 text-sm mb-2">{c.briefingPreview}</p>
            <p className="text-brass text-sm">{c.pointsReward} points</p>
          </Link>
        ))}
      </div>
    </div>
  )
}
