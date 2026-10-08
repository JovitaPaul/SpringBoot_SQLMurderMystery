import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { submitAccusation } from '../api/caseApi'
import { executeQuery } from '../api/queryApi'
import { recordCompletion } from '../api/progressApi'
import { extractErrorMessage } from '../api/client'

const CASE_SCHEMA = 'defaultdb'

const reportSql = (id) =>
  `SELECT * FROM crime_scene_report WHERE case_id = ${id} LIMIT 1`

export default function CaseSolving() {
  const { caseId } = useParams()
  const navigate = useNavigate()

  const [caseData, setCaseData] = useState(null)
  const [loadError, setLoadError] = useState('')

  const [sql, setSql] = useState('SELECT * FROM person;')
  const [queryResult, setQueryResult] = useState(null)
  const [queryError, setQueryError] = useState('')
  const [running, setRunning] = useState(false)

  const [suspectId, setSuspectId] = useState('')
  const [reasoning, setReasoning] = useState('')
  const [accusationResult, setAccusationResult] = useState(null)
  const [accusationError, setAccusationError] = useState('')
  const [accusing, setAccusing] = useState(false)

  useEffect(() => {
    let cancelled = false

    async function loadCase() {
      try {
        setLoadError('')
        setCaseData(null)

        const numericCaseId = Number(caseId)
        if (!Number.isInteger(numericCaseId)) throw new Error('Invalid case ID.')

        const result = await executeQuery(CASE_SCHEMA, reportSql(numericCaseId))
        if (cancelled) return

        if (!result.rows || result.rows.length === 0) {
          throw new Error(`Case ${caseId} not found`)
        }

        const columns = result.columns.map((c) => String(c).toLowerCase())
        const row = result.rows[0]
        const get = (name) => {
          const i = columns.indexOf(name)
          return i === -1 ? null : row[i]
        }

        setCaseData({
          id: get('case_id'),
          caseId: get('case_id'),
          date: get('date'),
          type: get('type'),
          city: get('city'),
          description: get('description'),
          targetSchema: CASE_SCHEMA,
          title: get('type') || `Case #${get('case_id')}`,
          briefing: get('description') || 'No description available.',
        })
      } catch (err) {
        if (!cancelled) setLoadError(extractErrorMessage(err, 'Could not load this case.'))
      }
    }

    loadCase()
    return () => {
      cancelled = true
    }
  }, [caseId])

  const runQuery = async () => {
    setRunning(true)
    setQueryError('')
    setQueryResult(null)
    try {
      setQueryResult(await executeQuery(CASE_SCHEMA, sql))
    } catch (err) {
      setQueryError(extractErrorMessage(err, 'Query failed.'))
    } finally {
      setRunning(false)
    }
  }

  const handleAccuse = async (e) => {
    e.preventDefault()
    setAccusing(true)
    setAccusationError('')
    try {
      const result = await submitAccusation(caseId, Number(suspectId), reasoning)
      setAccusationResult(result)
      if (result.correct) {
        await recordCompletion('CASE', caseData.id, caseData.title, result.pointsAwarded).catch(() => {})
      }
    } catch (err) {
      setAccusationError(extractErrorMessage(err, 'Could not submit your accusation.'))
    } finally {
      setAccusing(false)
    }
  }

  if (loadError) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-10 text-rust bg-rust/10 border border-rust/30 rounded-md">
        {loadError}
      </div>
    )
  }
  if (!caseData) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-10 text-parchment/60 flex items-center gap-2">
        <span className="h-4 w-4 rounded-full border-2 border-brass/30 border-t-brass animate-spin" />
        Loading case file...
      </div>
    )
  }

  return (
    <div className="min-h-screen photo-shell bg-photo-schema">
      <div className="photo-overlay" />
      <div className="max-w-4xl mx-auto px-4 py-10 space-y-6 page-shell relative z-10">
        {/* BRIEFING */}
        <div className="card">
          <p className="eyebrow">Briefing</p>
          <h1 className="font-display text-3xl text-brass mb-2">{caseData.title}</h1>
          <p className="text-xs text-parchment/40 mb-3">
            Case #{caseData.caseId}
            {caseData.date ? ` · ${String(caseData.date).slice(0, 10)}` : ''}
            {caseData.city ? ` · ${caseData.city}` : ''}
          </p>
          <p className="text-parchment/80 leading-relaxed whitespace-pre-line">{caseData.briefing}</p>
          <p className="text-xs text-parchment/40 mt-3">
            Querying schema: <span className="font-mono text-brass/80">{caseData.targetSchema}</span>
          </p>
        </div>

        {/* QUERY EDITOR */}
        <div className="card">
          <h2 className="font-display text-lg text-brass mb-3 flex items-center gap-2">
            <span aria-hidden="true">🧾</span> Query editor
          </h2>
          <textarea
            className="input-field font-mono text-sm h-32 resize-y"
            value={sql}
            onChange={(e) => setSql(e.target.value)}
            spellCheck={false}
          />
          <button className="btn-primary mt-3" onClick={runQuery} disabled={running || !sql.trim()}>
            {running ? 'Running...' : '▶ Run query'}
          </button>

          {queryError && (
            <p className="text-rust text-sm mt-3 bg-rust/10 border border-rust/30 rounded-md px-3 py-2">
              {queryError}
            </p>
          )}

          {queryResult && (
            <div className="mt-4 overflow-x-auto">
              <p className="text-xs text-parchment/40 mb-2">
                {queryResult.rowCount} {queryResult.rowCount === 1 ? 'row' : 'rows'} ·{' '}
                {queryResult.executionTimeMs}ms
                {queryResult.truncated && ' · results truncated'}
              </p>
              <table className="table-noir">
                <thead>
                  <tr>
                    {queryResult.columns.map((col) => (
                      <th key={col}>{col}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {queryResult.rows.map((row, i) => (
                    <tr key={i}>
                      {row.map((cell, j) => (
                        <td key={j}>
                          {cell === null ? <span className="text-parchment/30">NULL</span> : String(cell)}
                        </td>
                      ))}
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* ACCUSATION */}
        <div className="card">
          <h2 className="font-display text-lg text-brass mb-3 flex items-center gap-2">
            <span aria-hidden="true">⚖️</span> Make your accusation
          </h2>

          {accusationResult ? (
            <div>
              <p className={`text-lg mb-2 ${accusationResult.correct ? 'text-green-400' : 'text-rust'}`}>
                {accusationResult.message}
              </p>
              {accusationResult.correct ? (
                <>
                  <p className="text-parchment/70 text-sm mb-4 whitespace-pre-line">
                    {accusationResult.solutionExplanation}
                  </p>
                  <div className="flex gap-3">
                    <button className="btn-primary" onClick={() => navigate('/cases')}>
                      Back to cases
                    </button>
                    <button className="btn-secondary" onClick={() => navigate('/dashboard')}>
                      View dashboard
                    </button>
                  </div>
                </>
              ) : (
                <button className="btn-secondary" onClick={() => setAccusationResult(null)}>
                  Try a different suspect
                </button>
              )}
            </div>
          ) : (
            <form onSubmit={handleAccuse} className="space-y-3">
              <div>
                <label className="block text-sm mb-1 text-parchment/80">
                  Suspect ID (from the case's <span className="font-mono">person</span> table)
                </label>
                <input
                  type="number"
                  className="input-field max-w-xs"
                  value={suspectId}
                  onChange={(e) => setSuspectId(e.target.value)}
                  required
                />
              </div>
              <div>
                <label className="block text-sm mb-1 text-parchment/80">Your reasoning (optional)</label>
                <textarea
                  className="input-field h-20 resize-y"
                  value={reasoning}
                  onChange={(e) => setReasoning(e.target.value)}
                />
              </div>
              {accusationError && (
                <p className="text-rust text-sm bg-rust/10 border border-rust/30 rounded-md px-3 py-2">
                  {accusationError}
                </p>
              )}
              <button type="submit" className="btn-primary" disabled={accusing}>
                {accusing ? 'Submitting...' : 'Accuse'}
              </button>
            </form>
          )}
        </div>
      </div>
    </div>
  )
}