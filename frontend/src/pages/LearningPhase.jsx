import React, { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { listQuizzes } from '../api/quizApi'
import { extractErrorMessage } from '../api/client'

const difficultyColor = {
  BEGINNER: 'text-green-400',
  INTERMEDIATE: 'text-brass',
  ADVANCED: 'text-rust',
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
    <div className="max-w-3xl mx-auto px-4 py-10">
      <h1 className="font-display text-3xl text-brass mb-1">Learning Phase</h1>
      <p className="text-parchment/60 mb-8">
        Timed quizzes on core SQL topics. Pass one to earn points and keep your streak alive.
      </p>

      {loading && <p className="text-parchment/60">Loading topics...</p>}
      {error && <p className="text-rust">{error}</p>}

      <div className="space-y-3">
        {quizzes.map((quiz) => (
          <Link
            key={quiz.id}
            to={`/learning/${quiz.id}`}
            className="card flex items-center justify-between hover:border-brass transition-colors"
          >
            <div>
              <h3 className="font-display text-lg text-brass">{quiz.topic}</h3>
              <p className="text-parchment/60 text-sm">{quiz.description}</p>
            </div>
            <div className="text-right text-sm shrink-0 ml-4">
              <p className={difficultyColor[quiz.difficulty] || 'text-parchment'}>{quiz.difficulty}</p>
              <p className="text-parchment/50">{quiz.questionCount} questions · {quiz.timeLimitSeconds}s</p>
            </div>
          </Link>
        ))}
      </div>
    </div>
  )
}
