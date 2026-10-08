import React, { useEffect, useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import { executeQuery } from '../api/queryApi'
import { extractErrorMessage } from '../api/client'

const CASE_SCHEMA = 'defaultdb'
const PAGE_SIZE = 12

export default function CaseList() {
  const [reports, setReports] = useState([])
  const [truncated, setTruncated] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(true)
  const [filter, setFilter] = useState('')
  const [visible, setVisible] = useState(PAGE_SIZE)

  useEffect(() => {
    executeQuery(CASE_SCHEMA, 'SELECT * FROM crime_scene_report ORDER BY case_id')
      .then((res) => {
        const cols = res.columns.map((c) => String(c).toLowerCase())
        const get = (row, name) => {
          const i = cols.indexOf(name)
          return i === -1 ? null : row[i]
        }
        setTruncated(!!res.truncated)
        setReports(
          res.rows.map((row) => ({
            caseId: get(row, 'case_id'),
            date: get(row, 'date'),
            type: get(row, 'type'),
            city: get(row, 'city'),
            description: get(row, 'description') || '',
          }))
        )
      })
      .catch((err) => setError(extractErrorMessage(err, 'Could not load crime scene reports.')))
      .finally(() => setLoading(false))
  }, [])

  const filtered = useMemo(() => {
    const f = filter.trim().toLowerCase()
    if (!f) return reports
    return reports.filter((r) =>
      [r.caseId, r.date, r.type, r.city, r.description].some((v) =>
        String(v ?? '').toLowerCase().includes(f)
      )
    )
  }, [reports, filter])

  return (
    <div className="min-h-screen photo-shell bg-photo-schema">
      <div className="photo-overlay" />
      <div className="max-w-3xl mx-auto px-4 py-10 page-shell relative z-10">
        <p className="eyebrow">Open investigations</p>
        <h1 className="font-display text-3xl text-brass mb-1">Case-Solving Phase</h1>
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

        {reports.length > 0 && (
          <>
            <input
              type="text"
              className="input-field mb-2"
              placeholder="Filter by type, city, date or keyword"
              value={filter}
              onChange={(e) => {
                setFilter(e.target.value)
                setVisible(PAGE_SIZE)
              }}
            />
            <p className="text-xs text-parchment/40 mb-3">
              Showing {Math.min(visible, filtered.length)} of {filtered.length} reports
              {truncated && ' · list truncated by the query service'}
            </p>

            <div className="space-y-3">
              {filtered.slice(0, visible).map((r) => (
                <Link key={r.caseId} to={`/cases/${r.caseId}`} className="card interactive block">
                  <div className="flex items-center justify-between gap-3 mb-1">
                    <h3 className="font-display text-lg text-brass flex items-center gap-2">
                      <span aria-hidden="true">🔎</span>
                      {r.type || 'Crime scene report'}
                    </h3>
                    <span className="pill shrink-0 text-parchment/70 border-parchment/30">
                      #{r.caseId}
                    </span>
                  </div>
                  <p className="text-xs text-parchment/40 mb-2">
                    {r.date ? String(r.date).slice(0, 10) : 'Unknown date'}
                    {r.city ? ` · ${r.city}` : ''}
                  </p>
                  <p className="text-parchment/70 text-sm">
                    {r.description.length > 180 ? `${r.description.slice(0, 180)}...` : r.description}
                  </p>
                </Link>
              ))}
            </div>

            {visible < filtered.length && (
              <button
                className="btn-secondary w-full mt-4"
                onClick={() => setVisible((v) => v + PAGE_SIZE)}
              >
                Show more
              </button>
            )}
          </>
        )}

        {!loading && !error && reports.length === 0 && (
          <p className="text-parchment/50 text-sm">No crime scene reports found.</p>
        )}
      </div>
    </div>
  )
}