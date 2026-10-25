import React, { useEffect, useState } from 'react'
import { getMatches } from '../api/matches'
import { createSession } from '../api/sessions'
import { useAuth } from '../context/AuthContext'
import Spinner from '../components/Spinner'
import EmptyState from '../components/EmptyState'
import ErrorBanner from '../components/ErrorBanner'

export default function Home() {
  const { user } = useAuth()
  const [matches, setMatches] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [bookingFor, setBookingFor] = useState(null) // match currently being booked
  const [scheduledAt, setScheduledAt] = useState('')
  const [bookingMsg, setBookingMsg] = useState('')

  useEffect(() => {
    loadMatches()
  }, [])

  const loadMatches = async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await getMatches()
      setMatches(data)
    } catch (err) {
      setError(err.response?.data?.message || 'Could not load matches')
    } finally {
      setLoading(false)
    }
  }

  const openBooking = (match) => {
    setBookingFor(match)
    setScheduledAt('')
    setBookingMsg('')
  }

  const submitBooking = async (e) => {
    e.preventDefault()
    setBookingMsg('')
    try {
      await createSession({
        teacherId: bookingFor.teacherId,
        skillId: bookingFor.skillId,
        scheduledAt: new Date(scheduledAt).toISOString(),
      })
      setBookingMsg('Request sent! Check the Sessions tab.')
      setTimeout(() => setBookingFor(null), 1200)
    } catch (err) {
      setBookingMsg(err.response?.data?.message || 'Could not book this session')
    }
  }

  return (
    <div className="max-w-5xl mx-auto px-4 py-6">
      <div className="flex items-center justify-between mb-1">
        <h1 className="text-xl font-semibold">Namaste, {user?.name?.split(' ')[0]} 👋</h1>
      </div>
      <p className="text-gray-500 text-sm mb-6">People who can teach what you want to learn</p>

      <ErrorBanner message={error} />

      {loading ? (
        <div className="flex justify-center py-16"><Spinner /></div>
      ) : matches.length === 0 ? (
        <EmptyState
          icon="🔍"
          title="No matches yet"
          subtitle="Add skills you want to learn on your profile — we'll find teachers for you."
        />
      ) : (
        <div className="grid sm:grid-cols-2 gap-4">
          {matches.map((m, idx) => (
            <div key={idx} className="bg-white border border-gray-100 rounded-xl p-4 shadow-sm">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-10 h-10 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center font-medium">
                  {m.teacherName?.slice(0, 2).toUpperCase()}
                </div>
                <div>
                  <p className="font-medium text-sm">{m.teacherName} teaches {m.skillName}</p>
                  <p className="text-xs text-gray-400">
                    {m.level?.toLowerCase()} {m.teacherRating ? `· ⭐ ${m.teacherRating}` : ''}
                  </p>
                </div>
              </div>
              {m.teacherBio && <p className="text-xs text-gray-500 mb-3 line-clamp-2">{m.teacherBio}</p>}
              <button
                onClick={() => openBooking(m)}
                className="w-full bg-brand-600 hover:bg-brand-700 text-white text-sm rounded-lg py-2 font-medium"
              >
                Request session
              </button>
            </div>
          ))}
        </div>
      )}

      {bookingFor && (
        <div className="fixed inset-0 bg-black/30 flex items-center justify-center px-4 z-20">
          <div className="bg-white rounded-xl p-6 w-full max-w-sm">
            <h3 className="font-medium mb-1">Book with {bookingFor.teacherName}</h3>
            <p className="text-sm text-gray-500 mb-4">{bookingFor.skillName} · costs 2 credits</p>
            <form onSubmit={submitBooking}>
              <label className="block text-sm text-gray-600 mb-1">Date & time</label>
              <input
                type="datetime-local" required value={scheduledAt}
                onChange={e => setScheduledAt(e.target.value)}
                className="w-full border border-gray-200 rounded-lg px-3 py-2 mb-4 text-sm"
              />
              {bookingMsg && <p className="text-sm text-brand-600 mb-3">{bookingMsg}</p>}
              <div className="flex gap-2">
                <button type="button" onClick={() => setBookingFor(null)} className="flex-1 border border-gray-200 rounded-lg py-2 text-sm">Cancel</button>
                <button type="submit" className="flex-1 bg-brand-600 text-white rounded-lg py-2 text-sm font-medium">Send request</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
