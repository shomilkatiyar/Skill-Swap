import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import ErrorBanner from '../components/ErrorBanner'
import Spinner from '../components/Spinner'

export default function Login() {
  const { login, loading, error } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    const ok = await login(email, password)
    if (ok) navigate('/')
  }

  return (
    <div className="min-h-[80vh] flex items-center justify-center px-4">
      <div className="w-full max-w-sm">
        <div className="text-center mb-8">
          <h1 className="text-2xl font-semibold text-brand-700">Skill<span className="text-brand-500">Swap</span></h1>
          <p className="text-gray-500 text-sm mt-1">Trade skills, not money</p>
        </div>

        <form onSubmit={handleSubmit} className="bg-white shadow-sm border border-gray-100 rounded-xl p-6">
          <h2 className="text-lg font-medium mb-4">Log in</h2>
          <ErrorBanner message={error} />

          <label className="block text-sm text-gray-600 mb-1">Email</label>
          <input
            type="email" required value={email} onChange={e => setEmail(e.target.value)}
            className="w-full border border-gray-200 rounded-lg px-3 py-2 mb-4 text-sm focus:outline-none focus:ring-2 focus:ring-brand-300"
            placeholder="you@example.com"
          />

          <label className="block text-sm text-gray-600 mb-1">Password</label>
          <input
            type="password" required value={password} onChange={e => setPassword(e.target.value)}
            className="w-full border border-gray-200 rounded-lg px-3 py-2 mb-6 text-sm focus:outline-none focus:ring-2 focus:ring-brand-300"
            placeholder="••••••••"
          />

          <button
            type="submit" disabled={loading}
            className="w-full bg-brand-600 hover:bg-brand-700 text-white rounded-lg py-2 text-sm font-medium flex items-center justify-center gap-2 disabled:opacity-60"
          >
            {loading && <Spinner small />}
            Log in
          </button>

          <p className="text-center text-sm text-gray-500 mt-4">
            No account? <Link to="/register" className="text-brand-600 font-medium">Sign up</Link>
          </p>
        </form>
      </div>
    </div>
  )
}
