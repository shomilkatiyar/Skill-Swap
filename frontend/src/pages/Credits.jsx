import React, { useEffect, useState } from 'react'
import { getCreditHistory } from '../api/credits'
import Spinner from '../components/Spinner'
import EmptyState from '../components/EmptyState'
import ErrorBanner from '../components/ErrorBanner'

const TYPE_STYLES = {
  EARNED: 'text-green-600',
  SPENT: 'text-red-500',
  BONUS: 'text-brand-600',
}

export default function Credits() {
  const [history, setHistory] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  useEffect(() => {
    getCreditHistory()
      .then(setHistory)
      .catch(err => setError(err.response?.data?.message || 'Could not load history'))
      .finally(() => setLoading(false))
  }, [])

  return (
    <div className="max-w-2xl mx-auto px-4 py-6">
      <h1 className="text-xl font-semibold mb-1">Credit history</h1>
      <p className="text-gray-500 text-sm mb-6">Every credit earned or spent</p>

      <ErrorBanner message={error} />

      {loading ? (
        <div className="flex justify-center py-16"><Spinner /></div>
      ) : history.length === 0 ? (
        <EmptyState icon="⭐" title="No transactions yet" />
      ) : (
        <div className="bg-white border border-gray-100 rounded-xl divide-y divide-gray-100 shadow-sm">
          {history.map(t => (
            <div key={t.id} className="flex items-center justify-between px-4 py-3">
              <div>
                <p className="text-sm font-medium">{t.description}</p>
                <p className="text-xs text-gray-400">{new Date(t.createdAt).toLocaleString()}</p>
              </div>
              <span className={`text-sm font-semibold ${TYPE_STYLES[t.type]}`}>
                {t.amount > 0 ? '+' : ''}{t.amount}
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
