import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listQuizzes } from '../api/quizApi'
import { extractErrorMessage } from '../api/client'

const difficultyColor = {
  BEGINNER: 'text-green-400 border-green-400/40 bg-green-400/10',
  INTERMEDIATE: 'text-brass border-brass/40 bg-brass/10',
  ADVANCED: 'text-rust border-rust/40 bg-rust/10',
}

export default function LearningPhase() {
  const [quizzes, setQuizzes] = useState([])
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    listQuizzes()
      .then(setQuizzes)
      .catch((err) => setError(extractErrorMessage(err, 'Could not load quizzes.')))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="min-h-screen photo-shell bg-photo-topics">
      <div className="photo-overlay" />
      <div className="max-w-3xl mx-auto px-4 py-10 page-shell relative z-10">
      <p className="eyebrow">Sharpen your skills</p>
      <h1 className="font-display text-3xl text-brass mb-1">Learning Phase</h1>
      <p className="text-parchment/60 mb-8">
        Timed quizzes on core SQL topics. Pass one to earn points and keep your streak alive.
      </p>

      {loading && (
        <p className="text-parchment/60 flex items-center gap-2">
          <span className="h-4 w-4 rounded-full border-2 border-brass/30 border-t-brass animate-spin" />
          Loading topics...
        </p>
      )}
      {error && <p className="text-rust bg-rust/10 border border-rust/30 rounded-md px-3 py-2">{error}</p>}

      <div className="space-y-3">
        {quizzes.map((quiz) => (
          <Link
            key={quiz.id}
            to={`/learning/${quiz.id}`}
            className="card interactive flex items-center justify-between gap-4"
          >
            <div className="flex items-center gap-3">
              <span className="w-9 h-9 shrink-0 rounded-md bg-brass/10 border border-brass/30 flex items-center justify-center text-lg">📘</span>
              <div>
                <h3 className="font-display text-lg text-brass">{quiz.topic}</h3>
                <p className="text-parchment/60 text-sm">{quiz.description}</p>
              </div>
            </div>
            <div className="text-right text-sm shrink-0">
              <span className={`pill ${difficultyColor[quiz.difficulty] || 'text-parchment border-parchment/30'}`}>
                {quiz.difficulty}
              </span>
              <p className="text-parchment/50 mt-1.5">{quiz.questionCount} questions · {quiz.timeLimitSeconds}s</p>
            </div>
          </Link>
        ))}
      </div>
      </div>
    </div>
  )
}
