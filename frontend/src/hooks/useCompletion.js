import { useEffect, useState } from 'react'
import { getMyProgress } from '../api/progressApi'
import { getTotalCases, getTotalQuizzes } from '../api/catalogApi'

/**
 * Everything needed to show "solved" marks and "x/total" counters:
 * which cases/quizzes the learner has completed, plus how many exist in total.
 * Each piece loads independently, so one failing call doesn't blank the others.
 */
export function useCompletion() {
  const [state, setState] = useState({
    loading: true,
    progress: null,
    solvedCases: new Set(),
    passedQuizzes: new Set(),
    totalCases: null,
    totalQuizzes: null,
  })

  useEffect(() => {
    let cancelled = false
    Promise.allSettled([getMyProgress(), getTotalCases(), getTotalQuizzes()]).then(
      ([progressRes, casesRes, quizzesRes]) => {
        if (cancelled) return
        const progress = progressRes.status === 'fulfilled' ? progressRes.value : null
        setState({
          loading: false,
          progress,
          solvedCases: new Set((progress?.solvedCaseIds ?? []).map(Number)),
          passedQuizzes: new Set((progress?.passedQuizIds ?? []).map(Number)),
          totalCases: casesRes.status === 'fulfilled' ? casesRes.value : null,
          totalQuizzes: quizzesRes.status === 'fulfilled' ? quizzesRes.value : null,
        })
      },
    )
    return () => {
      cancelled = true
    }
  }, [])

  return state
}

/** "1/60", or "1/?" if the total couldn't be loaded. */
export const fraction = (done, total) => `${done}/${total ?? '?'}`
