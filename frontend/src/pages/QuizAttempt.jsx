import React, { useEffect, useMemo, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getQuiz, submitAttempt } from '../api/quizApi'
import { recordCompletion } from '../api/progressApi'
import { extractErrorMessage } from '../api/client'

export default function QuizAttempt() {
  const { quizId } = useParams()
  const navigate = useNavigate()

  const [quiz, setQuiz] = useState(null)
  const [answers, setAnswers] = useState({})   // questionId -> selectedOptionId
  const [secondsLeft, setSecondsLeft] = useState(null)
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)

  const startedAt = useMemo(() => Date.now(), [quizId])

  useEffect(() => {
    getQuiz(quizId)
      .then((data) => {
        setQuiz(data)
        setSecondsLeft(data.timeLimitSeconds)
      })
      .catch((err) => setError(extractErrorMessage(err, 'Could not load this quiz.')))
      .finally(() => setLoading(false))
  }, [quizId])

  useEffect(() => {
    if (secondsLeft === null || result || submitting) return
    if (secondsLeft <= 0) {
      handleSubmit()
      return
    }
    const timer = setTimeout(() => setSecondsLeft((s) => s - 1), 1000)
    return () => clearTimeout(timer)
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [secondsLeft, result, submitting])

  const selectOption = (questionId, optionId) => {
    setAnswers((prev) => ({ ...prev, [questionId]: optionId }))
  }

  const handleSubmit = async () => {
    if (submitting || result) return
    setSubmitting(true)
    setError('')
    try {
      const timeTakenSeconds = Math.round((Date.now() - startedAt) / 1000)
      const payload = quiz.questions.map((q) => ({
        questionId: q.id,
        selectedOptionId: answers[q.id] ?? null,
      }))
      const attemptResult = await submitAttempt(quizId, payload, timeTakenSeconds)
      setResult(attemptResult)

      if (attemptResult.passed) {
        await recordCompletion('QUIZ', quiz.id, quiz.topic, attemptResult.score).catch(() => {
          // Progress recording is best-effort from the UI's point of view too — the
          // quiz result itself is already saved server-side either way.
        })
      }
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not submit your attempt.'))
    } finally {
      setSubmitting(false)
    }
  }

  if (loading) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-10 text-parchment/60 flex items-center gap-2">
        <span className="h-4 w-4 rounded-full border-2 border-brass/30 border-t-brass animate-spin" />
        Loading quiz...
      </div>
    )
  }
  if (error && !quiz) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-10 text-rust bg-rust/10 border border-rust/30 rounded-md">
        {error}
      </div>
    )
  }
  if (!quiz) return null

  if (result) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-10 page-shell">
        <div className="card text-center">
          <div className="text-4xl mb-3" aria-hidden="true">
            {result.passed ? '🕵️‍♂️' : '🧩'}
          </div>
          <h1 className="font-display text-3xl text-brass mb-2">
            {result.passed ? 'Case closed — you passed!' : 'Not quite there yet'}
          </h1>
          <p className="text-parchment/70 mb-4">
            {result.correctCount} / {result.totalQuestions} correct · {result.score} points
          </p>
          <div className="flex justify-center gap-3">
            <button className="btn-secondary" onClick={() => navigate('/learning')}>
              Back to Learning Phase
            </button>
            {!result.passed && (
              <button className="btn-primary" onClick={() => window.location.reload()}>
                Try again
              </button>
            )}
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className="max-w-2xl mx-auto px-4 py-10 page-shell">
      <div className="flex items-center justify-between mb-6">
        <h1 className="font-display text-2xl text-brass">{quiz.topic}</h1>
        <span
          className={`font-mono px-3 py-1 rounded-md border text-sm ${
            secondsLeft <= 10
              ? 'text-rust border-rust/40 bg-rust/10 animate-pulse'
              : 'text-parchment/70 border-brass/20 bg-black/20'
          }`}
        >
          ⏱ {Math.floor(secondsLeft / 60)}:{String(secondsLeft % 60).padStart(2, '0')}
        </span>
      </div>

      {error && <p className="text-rust bg-rust/10 border border-rust/30 rounded-md px-3 py-2 mb-4">{error}</p>}

      <div className="space-y-6">
        {quiz.questions.map((q, idx) => (
          <div key={q.id} className="card">
            <p className="text-parchment/90 mb-2">
              <span className="text-brass mr-2 font-display">{idx + 1}.</span>
              {q.questionText}
            </p>
            {q.codeSnippet && (
              <pre className="bg-black/40 border border-brass/10 rounded-md p-3 text-sm text-brass/90 overflow-x-auto mb-3 font-mono">
                {q.codeSnippet}
              </pre>
            )}
            <div className="space-y-2">
              {q.options.map((opt) => (
                <label
                  key={opt.id}
                  className={`block px-3 py-2 rounded-md border cursor-pointer transition-colors ${
                    answers[q.id] === opt.id
                      ? 'border-brass bg-brass/10'
                      : 'border-brass/20 hover:border-brass/50'
                  }`}
                >
                  <input
                    type="radio"
                    name={`q-${q.id}`}
                    className="mr-2 accent-brass"
                    checked={answers[q.id] === opt.id}
                    onChange={() => selectOption(q.id, opt.id)}
                  />
                  {opt.optionText}
                </label>
              ))}
            </div>
          </div>
        ))}
      </div>

      <button className="btn-primary w-full mt-6" onClick={handleSubmit} disabled={submitting}>
        {submitting ? 'Submitting...' : 'Submit answers'}
      </button>
    </div>
  )
}
