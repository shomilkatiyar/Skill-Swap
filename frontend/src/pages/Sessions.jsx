import React, { useEffect, useState } from 'react'
import { getMySessions, updateSessionStatus } from '../api/sessions'
import { createReview } from '../api/reviews'
import { useAuth } from '../context/AuthContext'
import Spinner from '../components/Spinner'
import EmptyState from '../components/EmptyState'
import ErrorBanner from '../components/ErrorBanner'

const STATUS_STYLES = {
  PENDING: 'bg-yellow-50 text-yellow-700',
  CONFIRMED: 'bg-blue-50 text-blue-700',
  COMPLETED: 'bg-green-50 text-green-700',
  REJECTED: 'bg-red-50 text-red-700',
  CANCELLED: 'bg-gray-100 text-gray-500',
}

export default function Sessions() {
  const { user } = useAuth()
  const [sessions, setSessions] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [reviewFor, setReviewFor] = useState(null)
  const [rating, setRating] = useState(5)
  const [comment, setComment] = useState('')

  useEffect(() => { load() }, [])

  const load = async () => {
    setLoading(true)
    setError(null)
    try {
      setSessions(await getMySessions())
    } catch (err) {
      setError(err.response?.data?.message || 'Could not load sessions')
    } finally {
      setLoading(false)
    }
  }

  const act = async (id, status) => {
    try {
      await updateSessionStatus(id, status)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Action failed')
    }
  }

  const submitReview = async (e) => {
    e.preventDefault()
    try {
      await createReview({ sessionId: reviewFor.id, rating, comment })
      setReviewFor(null)
      setComment('')
      setRating(5)
    } catch (err) {
      setError(err.response?.data?.message || 'Could not submit review')
    }
  }

  return (
    <div className="max-w-3xl mx-auto px-4 py-6">
      <h1 className="text-xl font-semibold mb-1">My sessions</h1>
      <p className="text-gray-500 text-sm mb-6">Sessions you're teaching or learning</p>

      <ErrorBanner message={error} />

      {loading ? (
        <div className="flex justify-center py-16"><Spinner /></div>
      ) : sessions.length === 0 ? (
        <EmptyState icon="📅" title="No sessions yet" subtitle="Book one from the Browse tab." />
      ) : (
        <div className="space-y-3">
          {sessions.map(s => {
            const isTeacher = s.teacherId === user.id
            const otherName = isTeacher ? s.learnerName : s.teacherName
            const role = isTeacher ? 'Teaching' : 'Learning'
            return (
              <div key={s.id} className="bg-white border border-gray-100 rounded-xl p-4 shadow-sm">
                <div className="flex items-center justify-between mb-2">
                  <p className="font-medium text-sm">{s.skillName} — {role} {isTeacher ? '→' : '←'} {otherName}</p>
                  <span className={`text-xs px-2 py-0.5 rounded-full font-medium ${STATUS_STYLES[s.status]}`}>{s.status}</span>
                </div>
                <p className="text-xs text-gray-500 mb-3">
                  {new Date(s.scheduledAt).toLocaleString()} · {s.creditCost} credits
                </p>

                <div className="flex gap-2 flex-wrap">
                  {s.status === 'PENDING' && isTeacher && (
                    <>
                      <button onClick={() => act(s.id, 'CONFIRMED')} className="text-xs bg-brand-600 text-white rounded-lg px-3 py-1.5 font-medium">Accept</button>
                      <button onClick={() => act(s.id, 'REJECTED')} className="text-xs border border-gray-200 rounded-lg px-3 py-1.5">Decline</button>
                    </>
                  )}
                  {s.status === 'PENDING' && !isTeacher && (
                    <button onClick={() => act(s.id, 'CANCELLED')} className="text-xs border border-gray-200 rounded-lg px-3 py-1.5">Cancel request</button>
                  )}
                  {s.status === 'CONFIRMED' && (
                    <button onClick={() => act(s.id, 'COMPLETED')} className="text-xs bg-brand-600 text-white rounded-lg px-3 py-1.5 font-medium">Mark completed</button>
                  )}
                  {s.status === 'COMPLETED' && (
                    <button onClick={() => setReviewFor(s)} className="text-xs border border-brand-300 text-brand-700 rounded-lg px-3 py-1.5">Leave review</button>
                  )}
                </div>
              </div>
            )
          })}
        </div>
      )}

      {reviewFor && (
        <div className="fixed inset-0 bg-black/30 flex items-center justify-center px-4 z-20">
          <div className="bg-white rounded-xl p-6 w-full max-w-sm">
            <h3 className="font-medium mb-4">Review your session</h3>
            <form onSubmit={submitReview}>
              <div className="flex gap-1 mb-4">
                {[1, 2, 3, 4, 5].map(n => (
                  <button type="button" key={n} onClick={() => setRating(n)} className="text-2xl">
                    {n <= rating ? '⭐' : '☆'}
                  </button>
                ))}
              </div>
              <textarea
                value={comment} onChange={e => setComment(e.target.value)}
                placeholder="How did it go?"
                className="w-full border border-gray-200 rounded-lg px-3 py-2 mb-4 text-sm h-20"
              />
              <div className="flex gap-2">
                <button type="button" onClick={() => setReviewFor(null)} className="flex-1 border border-gray-200 rounded-lg py-2 text-sm">Cancel</button>
                <button type="submit" className="flex-1 bg-brand-600 text-white rounded-lg py-2 text-sm font-medium">Submit</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
