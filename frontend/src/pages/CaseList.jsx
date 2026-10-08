
import React, { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { executeQuery } from '../api/queryApi'
import { extractErrorMessage } from '../api/client'

const CASE_SCHEMA = 'defaultdb'

const REPORT_SQL = `
  SELECT case_id, date, type, city, description
  FROM crime_scene_report
  ORDER BY date, case_id
`

export default function CaseList() {
  const [reports, setReports] = useState([])
  const [truncated, setTruncated] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState('')
  const [expanded, setExpanded] = useState({})

  useEffect(() => {
    let cancelled = false

    async function loadReports() {
      try {
        setLoading(true)
        setError('')

        const res = await executeQuery(CASE_SCHEMA, REPORT_SQL)

        if (cancelled) return

        const columns = res.columns.map((c) => String(c).toLowerCase())

        const getValue = (row, name) => {
          const index = columns.indexOf(name)
          return index === -1 ? null : row[index]
        }

        const mapped = res.rows.map((row, index) => ({
          key: `${CASE_SCHEMA}-${getValue(row, 'case_id') ?? index}`,
          caseId: getValue(row, 'case_id'),
          date: getValue(row, 'date'),
          type: getValue(row, 'type'),
          city: getValue(row, 'city'),
          description: getValue(row, 'description'),
        }))

        setReports(mapped)
        setTruncated(Boolean(res.truncated))
      } catch (err) {
        if (!cancelled) {
          setError(
            extractErrorMessage(
              err,
              'Could not load crime scene reports.'
            )
          )
        }
      } finally {
        if (!cancelled) setLoading(false)
      }
    }

    loadReports()

    return () => {
      cancelled = true
    }
  }, [])

  const filtered = useMemo(() => {
    const f = filter.trim().toLowerCase()

    if (!f) return reports

    return reports.filter((r) =>
      [
        r.caseId,
        r.date,
        r.type,
        r.city,
        r.description,
      ].some((value) =>
        String(value ?? '').toLowerCase().includes(f)
      )
    )
  }, [reports, filter])

  return (
    <div className="min-h-screen photo-shell bg-photo-schema">
      <div className="photo-overlay" />

      <div className="max-w-3xl mx-auto px-4 py-10 page-shell relative z-10">

        <p className="eyebrow">Open investigations</p>

        <h1 className="font-display text-3xl text-brass mb-1">
          Case-Solving Phase
        </h1>

        <p className="text-parchment/60 mb-6">
          Read the crime scene reports, follow the evidence, and accuse the culprit.
        </p>

        {loading && (
          <p className="text-parchment/60 flex items-center gap-2">
            <span className="h-4 w-4 rounded-full border-2 border-brass/30 border-t-brass animate-spin" />
            Loading reports...
          </p>
        )}

        {error && (
          <p className="text-rust text-sm bg-rust/10 border border-rust/30 rounded-md px-3 py-2 mb-3">
            {error}
          </p>
        )}

        {!loading && reports.length > 0 && (
          <>
            <input
              type="text"
              className="input-field mb-2"
              placeholder="Filter by case ID, type, city, date or keyword"
              value={filter}
              onChange={(e) => {
                setFilter(e.target.value)
              }}
            />

            <p className="text-xs text-parchment/40 mb-3">
              Showing {filtered.length} of {reports.length} cases
              {truncated && ' · list truncated by the query service'}
            </p>

            <div className="space-y-3">
              {filtered.map((r) => {
                const open = expanded[r.key]
                const text = r.description || ''
                const long = text.length > 180

                return (
                  <div key={r.key} className="card">

                    <div className="flex items-center justify-between gap-3 mb-1">
                      <h3 className="font-display text-lg text-brass flex items-center gap-2">
                        <span aria-hidden="true">🔎</span>
                        {r.type || 'Crime scene report'}
                      </h3>

                      {r.caseId != null && (
                        <span className="pill shrink-0 text-parchment/70 border-parchment/30">
                          Case #{r.caseId}
                        </span>
                      )}
                    </div>

                    <p className="text-xs text-parchment/40 mb-2">
                      {r.date
                        ? String(r.date).slice(0, 10)
                        : 'Unknown date'}

                      {r.city ? ` · ${r.city}` : ''}
                    </p>

                    <p className="text-parchment/70 text-sm whitespace-pre-line">
                      {open || !long
                        ? text
                        : `${text.slice(0, 180)}...`}
                    </p>

                    <div className="flex items-center gap-4 mt-3">

                      {long && (
                        <button
                          className="text-sm text-brass hover:underline"
                          onClick={() =>
                            setExpanded((previous) => ({
                              ...previous,
                              [r.key]: !open,
                            }))
                          }
                        >
                          {open ? 'Show less' : 'Read more'}
                        </button>
                      )}

                      <Link
                        to={`/cases/${r.caseId}`}
                        className="text-sm text-brass hover:underline"
                      >
                        Investigate →
                      </Link>

                    </div>
                  </div>
                )
              })}
            </div>
          </>
        )}

        {!loading && !error && reports.length === 0 && (
          <p className="text-parchment/50 text-sm">
            No crime scene reports found.
          </p>
        )}

      </div>
    </div>
  )
}
