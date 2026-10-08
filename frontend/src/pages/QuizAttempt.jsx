// import React, { useEffect, useMemo, useState } from 'react'
// import { useNavigate, useParams } from 'react-router-dom'
// import { getQuiz, submitAttempt } from '../api/quizApi'
// import { recordCompletion } from '../api/progressApi'
// import { extractErrorMessage } from '../api/client'

// export default function QuizAttempt() {
//   const { quizId } = useParams()
//   const navigate = useNavigate()

//   const [quiz, setQuiz] = useState(null)
//   const [answers, setAnswers] = useState({})   // questionId -> selectedOptionId
//   const [secondsLeft, setSecondsLeft] = useState(null)
//   const [result, setResult] = useState(null)
//   const [error, setError] = useState('')
//   const [loading, setLoading] = useState(true)
//   const [submitting, setSubmitting] = useState(false)

//   const startedAt = useMemo(() => Date.now(), [quizId])

//   useEffect(() => {
//     getQuiz(quizId)
//       .then((data) => {
//         setQuiz(data)
//         setSecondsLeft(data.timeLimitSeconds)
//       })
//       .catch((err) => setError(extractErrorMessage(err, 'Could not load this quiz.')))
//       .finally(() => setLoading(false))
//   }, [quizId])

//   useEffect(() => {
//     if (secondsLeft === null || result || submitting) return
//     if (secondsLeft <= 0) {
//       handleSubmit()
//       return
//     }
//     const timer = setTimeout(() => setSecondsLeft((s) => s - 1), 1000)
//     return () => clearTimeout(timer)
//     // eslint-disable-next-line react-hooks/exhaustive-deps
//   }, [secondsLeft, result, submitting])

//   const selectOption = (questionId, optionId) => {
//     setAnswers((prev) => ({ ...prev, [questionId]: optionId }))
//   }

//   const handleSubmit = async () => {
//     if (submitting || result) return
//     setSubmitting(true)
//     setError('')
//     try {
//       const timeTakenSeconds = Math.round((Date.now() - startedAt) / 1000)
//       const payload = quiz.questions.map((q) => ({
//         questionId: q.id,
//         selectedOptionId: answers[q.id] ?? null,
//       }))
//       const attemptResult = await submitAttempt(quizId, payload, timeTakenSeconds)
//       setResult(attemptResult)

//       if (attemptResult.passed) {
//         await recordCompletion('QUIZ', quiz.id, quiz.topic, attemptResult.score).catch(() => {
//           // Progress recording is best-effort from the UI's point of view too — the
//           // quiz result itself is already saved server-side either way.
//         })
//       }
//     } catch (err) {
//       setError(extractErrorMessage(err, 'Could not submit your attempt.'))
//     } finally {
//       setSubmitting(false)
//     }
//   }

//   if (loading) {
//     return (
//       <div className="max-w-2xl mx-auto px-4 py-10 text-parchment/60 flex items-center gap-2">
//         <span className="h-4 w-4 rounded-full border-2 border-brass/30 border-t-brass animate-spin" />
//         Loading quiz...
//       </div>
//     )
//   }
//   if (error && !quiz) {
//     return (
//       <div className="max-w-2xl mx-auto px-4 py-10 text-rust bg-rust/10 border border-rust/30 rounded-md">
//         {error}
//       </div>
//     )
//   }
//   if (!quiz) return null

//   if (result) {
//     return (
//       <div className="max-w-2xl mx-auto px-4 py-10 page-shell">
//         <div className="card text-center">
//           <div className="text-4xl mb-3" aria-hidden="true">
//             {result.passed ? '🕵️‍♂️' : '🧩'}
//           </div>
//           <h1 className="font-display text-3xl text-brass mb-2">
//             {result.passed ? 'Case closed — you passed!' : 'Not quite there yet'}
//           </h1>
//           <p className="text-parchment/70 mb-4">
//             {result.correctCount} / {result.totalQuestions} correct · {result.score} points
//           </p>
//           <div className="flex justify-center gap-3">
//             <button className="btn-secondary" onClick={() => navigate('/learning')}>
//               Back to Learning Phase
//             </button>
//             {!result.passed && (
//               <button className="btn-primary" onClick={() => window.location.reload()}>
//                 Try again
//               </button>
//             )}
//           </div>
//         </div>
//       </div>
//     )
//   }

//   return (
//     <div className="max-w-2xl mx-auto px-4 py-10 page-shell">
//       <div className="flex items-center justify-between mb-6">
//         <h1 className="font-display text-2xl text-brass">{quiz.topic}</h1>
//         <span
//           className={`font-mono px-3 py-1 rounded-md border text-sm ${
//             secondsLeft <= 10
//               ? 'text-rust border-rust/40 bg-rust/10 animate-pulse'
//               : 'text-parchment/70 border-brass/20 bg-black/20'
//           }`}
//         >
//           ⏱ {Math.floor(secondsLeft / 60)}:{String(secondsLeft % 60).padStart(2, '0')}
//         </span>
//       </div>

//       {error && <p className="text-rust bg-rust/10 border border-rust/30 rounded-md px-3 py-2 mb-4">{error}</p>}

//       <div className="space-y-6">
//         {quiz.questions.map((q, idx) => (
//           <div key={q.id} className="card">
//             <p className="text-parchment/90 mb-2">
//               <span className="text-brass mr-2 font-display">{idx + 1}.</span>
//               {q.questionText}
//             </p>
//             {q.codeSnippet && (
//               <pre className="bg-black/40 border border-brass/10 rounded-md p-3 text-sm text-brass/90 overflow-x-auto mb-3 font-mono">
//                 {q.codeSnippet}
//               </pre>
//             )}
//             <div className="space-y-2">
//               {q.options.map((opt) => (
//                 <label
//                   key={opt.id}
//                   className={`block px-3 py-2 rounded-md border cursor-pointer transition-colors ${
//                     answers[q.id] === opt.id
//                       ? 'border-brass bg-brass/10'
//                       : 'border-brass/20 hover:border-brass/50'
//                   }`}
//                 >
//                   <input
//                     type="radio"
//                     name={`q-${q.id}`}
//                     className="mr-2 accent-brass"
//                     checked={answers[q.id] === opt.id}
//                     onChange={() => selectOption(q.id, opt.id)}
//                   />
//                   {opt.optionText}
//                 </label>
//               ))}
//             </div>
//           </div>
//         ))}
//       </div>

//       <button className="btn-primary w-full mt-6" onClick={handleSubmit} disabled={submitting}>
//         {submitting ? 'Submitting...' : 'Submit answers'}
//       </button>
//     </div>
//   )
// }

import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getQuiz, submitAttempt } from '../api/quizApi'
import { recordCompletion } from '../api/progressApi'
import { extractErrorMessage } from '../api/client'

const formatTime = (s) => `${Math.floor(s / 60)}:${String(s % 60).padStart(2, '0')}`

export default function QuizAttempt() {
  const { quizId } = useParams()
  const navigate = useNavigate()

  const [quiz, setQuiz] = useState(null)
  const [answers, setAnswers] = useState({}) // questionId -> selectedOptionId
  const [started, setStarted] = useState(false)
  const [elapsed, setElapsed] = useState(0) // counts up from 0
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    getQuiz(quizId)
      .then((data) => setQuiz(data))
      .catch((err) => setError(extractErrorMessage(err, 'Could not load this quiz.')))
      .finally(() => setLoading(false))
  }, [quizId])

  // Stopwatch: runs only after Start, stops on submit/result
  useEffect(() => {
    if (!started || result || submitting) return
    const timer = setInterval(() => setElapsed((s) => s + 1), 1000)
    return () => clearInterval(timer)
  }, [started, result, submitting])

  const selectOption = (questionId, optionId) => {
    setAnswers((prev) => ({ ...prev, [questionId]: optionId }))
  }

  const handleSubmit = async () => {
    if (submitting || result) return
    setSubmitting(true)
    setError('')
    try {
      const payload = quiz.questions.map((q) => ({
        questionId: q.id,
        selectedOptionId: answers[q.id] ?? null,
      }))
      const attemptResult = await submitAttempt(quizId, payload, elapsed)
      setResult(attemptResult)

      if (attemptResult.passed) {
        await recordCompletion('QUIZ', quiz.id, quiz.topic, attemptResult.score).catch(() => {
          // Best-effort: the quiz result is already saved server-side.
        })
      }
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not submit your attempt.'))
    } finally {
      setSubmitting(false)
    }
  }

  // Finds the per-question breakdown list in the response by its shape,
  // so it works whatever the backend names that field.
  const getCorrectOptionId = (q) => {
    const list =
      Object.values(result || {}).find(
        (v) => Array.isArray(v) && v.length > 0 && v[0]?.questionId !== undefined
      ) || []
    const item = list.find((r) => r.questionId === q.id)
    return item?.correctOptionId ?? item?.correctOption ?? null
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

  // ---------- START SCREEN ----------
  if (!started) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-10">
        <div className="card text-center">
          <h1 className="font-display text-3xl text-brass mb-2">{quiz.topic}</h1>
          <p className="text-parchment/70 mb-1">{quiz.questions.length} questions</p>
          <p className="text-parchment/50 text-sm mb-6">
            The timer starts as soon as you click Start.
          </p>
          <button className="btn-primary" onClick={() => setStarted(true)}>
            Start quiz
          </button>
        </div>
      </div>
    )
  }

  // ---------- RESULT + REVIEW SCREEN ----------
  if (result) {
    return (
      <div className="max-w-2xl mx-auto px-4 py-10 page-shell">
        <div className="card text-center mb-6">
          <div className="text-4xl mb-3" aria-hidden="true">
            {result.passed ? '🕵️‍♂️' : '🧩'}
          </div>
          <h1 className="font-display text-3xl text-brass mb-2">
            {result.passed ? 'Case closed — you passed!' : 'Not quite there yet'}
          </h1>
          <p className="text-2xl text-parchment mb-1">
            {result.correctCount} / {result.totalQuestions} correct
          </p>
          <p className="text-parchment/70 mb-1">{result.score} points</p>
          <p className="text-parchment/50 text-sm mb-4">Time taken: {formatTime(elapsed)}</p>
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

        <div className="space-y-6">
          {quiz.questions.map((q, idx) => {
            const correctId = getCorrectOptionId(q)
            const selectedId = answers[q.id]
            return (
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
                  {q.options.map((opt) => {
                    const isCorrect = opt.id === correctId
                    const isWrongPick = opt.id === selectedId && !isCorrect
                    return (
                      <div
                        key={opt.id}
                        className={`px-3 py-2 rounded-md border ${
                          isCorrect
                            ? 'border-green-500 bg-green-500/15 text-green-300'
                            : isWrongPick
                            ? 'border-red-500 bg-red-500/15 text-red-300'
                            : 'border-brass/20 opacity-70'
                        }`}
                      >
                        {opt.optionText}
                        {isCorrect && <span className="ml-2">✓ Correct answer</span>}
                        {isWrongPick && <span className="ml-2">✗ Your answer</span>}
                      </div>
                    )
                  })}
                </div>
                {selectedId == null && (
                  <p className="text-sm text-parchment/50 mt-2">You didn't answer this one.</p>
                )}
              </div>
            )
          })}
        </div>
      </div>
    )
  }

  // ---------- QUIZ SCREEN ----------
  return (
    <div className="max-w-2xl mx-auto px-4 py-10">
      <div className="flex items-center justify-between mb-6">
        <h1 className="font-display text-2xl text-brass">{quiz.topic}</h1>
        <span className="font-mono px-3 py-1 rounded-md border text-sm text-parchment/70 border-brass/20 bg-black/20">
          ⏱ {formatTime(elapsed)}
        </span>
      </div>

      {error && (
        <p className="text-rust bg-rust/10 border border-rust/30 rounded-md px-3 py-2 mb-4">
          {error}
        </p>
      )}

      <div className="space-y-6">
        {quiz.questions.map((q, idx) => (
          <div key={q.id} className="card">
            <p className="text-parchment/90 mb-2">
              <span className="text-brass mr-2">{idx + 1}.</span>
              {q.questionText}
            </p>
            {q.codeSnippet && (
              <pre className="bg-black/40 rounded-md p-3 text-sm text-brass/90 overflow-x-auto mb-3">
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
                    className="mr-2"
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
