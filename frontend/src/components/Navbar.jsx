import React from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <nav className="bg-white border-b border-gray-200 sticky top-0 z-10">
      <div className="max-w-5xl mx-auto px-4 flex items-center justify-between h-14">
        <Link to="/" className="font-semibold text-brand-700 text-lg">
          Skill<span className="text-brand-500">Swap</span>
        </Link>

        {user && (
          <div className="flex items-center gap-5 text-sm">
            <Link to="/" className="text-gray-600 hover:text-brand-700">Browse</Link>
            <Link to="/sessions" className="text-gray-600 hover:text-brand-700">Sessions</Link>
            <Link to="/credits" className="text-gray-600 hover:text-brand-700">Credits</Link>
            <Link to="/profile" className="text-gray-600 hover:text-brand-700">Profile</Link>

            <span className="flex items-center gap-1 bg-brand-50 text-brand-700 px-3 py-1 rounded-full font-medium">
              ⭐ {user.creditsBalance ?? 0}
            </span>

            <button
              onClick={handleLogout}
              className="text-gray-400 hover:text-red-500"
              title="Logout"
            >
              Logout
            </button>
          </div>
        )}
      </div>
    </nav>
  )
}
