import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { getCase, submitAccusation } from '../api/caseApi'
import { executeQuery } from '../api/queryApi'
import { recordCompletion } from '../api/progressApi'
import { extractErrorMessage } from '../api/client'

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
    getCase(caseId)
      .then(setCaseData)
      .catch((err) => setLoadError(extractErrorMessage(err, 'Could not load this case.')))
  }, [caseId])

  const runQuery = async () => {
    setRunning(true)
    setQueryError('')
    setQueryResult(null)
    try {
      const result = await executeQuery(caseData.targetSchema, sql)
      setQueryResult(result)
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

  if (loadError) return <div className="max-w-4xl mx-auto px-4 py-10 text-rust">{loadError}</div>
  if (!caseData) return <div className="max-w-4xl mx-auto px-4 py-10 text-parchment/60">Loading case file...</div>

  return (
    <div className="max-w-4xl mx-auto px-4 py-10 space-y-6">
      <div className="card">
        <h1 className="font-display text-3xl text-brass mb-2">{caseData.title}</h1>
        <p className="text-parchment/80 leading-relaxed whitespace-pre-line">{caseData.briefing}</p>
        <p className="text-xs text-parchment/40 mt-3">
          Querying schema: <span className="font-mono">{caseData.targetSchema}</span>
        </p>
      </div>

      <div className="card">
        <h2 className="font-display text-lg text-brass mb-3">Query editor</h2>
        <textarea
          className="input-field font-mono text-sm h-32 resize-y"
          value={sql}
          onChange={(e) => setSql(e.target.value)}
          spellCheck={false}
        />
        <button className="btn-primary mt-3" onClick={runQuery} disabled={running || !sql.trim()}>
          {running ? 'Running...' : 'Run query'}
        </button>

        {queryError && <p className="text-rust text-sm mt-3">{queryError}</p>}

        {queryResult && (
          <div className="mt-4 overflow-x-auto">
            <p className="text-xs text-parchment/40 mb-2">
              {queryResult.rowCount} row{queryResult.rowCount === 1 ? '' : 's'} · {queryResult.executionTimeMs}ms
              {queryResult.truncated && ' · results truncated'}
            </p>
            <table className="w-full text-sm border-collapse">
              <thead>
                <tr className="border-b border-brass/30 text-brass text-left">
                  {queryResult.columns.map((col) => (
                    <th key={col} className="py-2 pr-4 font-normal">{col}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {queryResult.rows.map((row, i) => (
                  <tr key={i} className="border-b border-brass/10">
                    {row.map((cell, j) => (
                      <td key={j} className="py-2 pr-4 text-parchment/80">
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

      <div className="card">
        <h2 className="font-display text-lg text-brass mb-3">Make your accusation</h2>

        {accusationResult ? (
          <div>
            <p className={`text-lg mb-2 ${accusationResult.correct ? 'text-green-400' : 'text-rust'}`}>
              {accusationResult.message}
            </p>
            {accusationResult.correct && (
              <>
                <p className="text-parchment/70 text-sm mb-4">{accusationResult.solutionExplanation}</p>
                <div className="flex gap-3">
                  <button className="btn-primary" onClick={() => navigate('/cases')}>
                    Back to cases
                  </button>
                  <button className="btn-secondary" onClick={() => navigate('/dashboard')}>
                    View dashboard
                  </button>
                </div>
              </>
            )}
            {!accusationResult.correct && (
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
            {accusationError && <p className="text-rust text-sm">{accusationError}</p>}
            <button type="submit" className="btn-primary" disabled={accusing}>
              {accusing ? 'Submitting...' : 'Accuse'}
            </button>
          </form>
        )}
      </div>
    </div>
  )
}
