import { executeQuery } from './queryApi'
import { listQuizzes } from './quizApi'

const CASE_SCHEMA = 'defaultdb'

/** Total number of cases = rows in crime_scene_report. */
export const getTotalCases = () =>
  executeQuery(CASE_SCHEMA, 'SELECT COUNT(*) AS total FROM crime_scene_report').then((res) => {
    const n = Number(res.rows?.[0]?.[0])
    return Number.isFinite(n) ? n : null
  })

/** Total number of quizzes = number of topics returned by the quiz list. */
export const getTotalQuizzes = () => listQuizzes().then((quizzes) => quizzes.length)
