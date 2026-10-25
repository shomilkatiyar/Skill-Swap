import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import ErrorBanner from '../components/ErrorBanner'
import Spinner from '../components/Spinner'

export default function Register() {
  const { register, loading, error } = useAuth()
  const navigate = useNavigate()
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const handleSubmit = async (e) => {
    e.preventDefault()
    const ok = await register(name, email, password)
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
          <h2 className="text-lg font-medium mb-4">Create your account</h2>
          <ErrorBanner message={error} />

          <label className="block text-sm text-gray-600 mb-1">Name</label>
          <input
            required value={name} onChange={e => setName(e.target.value)}
            className="w-full border border-gray-200 rounded-lg px-3 py-2 mb-4 text-sm focus:outline-none focus:ring-2 focus:ring-brand-300"
            placeholder="Riya Sharma"
          />

          <label className="block text-sm text-gray-600 mb-1">Email</label>
          <input
            type="email" required value={email} onChange={e => setEmail(e.target.value)}
            className="w-full border border-gray-200 rounded-lg px-3 py-2 mb-4 text-sm focus:outline-none focus:ring-2 focus:ring-brand-300"
            placeholder="you@example.com"
          />

          <label className="block text-sm text-gray-600 mb-1">Password</label>
          <input
            type="password" required minLength={6} value={password} onChange={e => setPassword(e.target.value)}
            className="w-full border border-gray-200 rounded-lg px-3 py-2 mb-1 text-sm focus:outline-none focus:ring-2 focus:ring-brand-300"
            placeholder="At least 6 characters"
          />
          <p className="text-xs text-gray-400 mb-5">You'll get 5 free credits to start swapping.</p>

          <button
            type="submit" disabled={loading}
            className="w-full bg-brand-600 hover:bg-brand-700 text-white rounded-lg py-2 text-sm font-medium flex items-center justify-center gap-2 disabled:opacity-60"
          >
            {loading && <Spinner small />}
            Create account
          </button>

          <p className="text-center text-sm text-gray-500 mt-4">
            Already have an account? <Link to="/login" className="text-brand-600 font-medium">Log in</Link>
          </p>
        </form>
      </div>
    </div>
  )
}
