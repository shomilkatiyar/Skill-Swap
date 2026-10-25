import React, { useEffect, useState } from 'react'
import { getMyProfile, updateProfile, addOfferedSkill, addWantedSkill, removeOfferedSkill, removeWantedSkill } from '../api/user'
import { useAuth } from '../context/AuthContext'
import Spinner from '../components/Spinner'
import ErrorBanner from '../components/ErrorBanner'

const LEVELS = ['BEGINNER', 'INTERMEDIATE', 'EXPERT']

export default function Profile() {
  const { refreshUser } = useAuth()
  const [profile, setProfile] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)

  const [bio, setBio] = useState('')
  const [location, setLocation] = useState('')
  const [savingBio, setSavingBio] = useState(false)

  const [offeredName, setOfferedName] = useState('')
  const [offeredLevel, setOfferedLevel] = useState('INTERMEDIATE')
  const [wantedName, setWantedName] = useState('')

  useEffect(() => { load() }, [])

  const load = async () => {
    setLoading(true)
    setError(null)
    try {
      const data = await getMyProfile()
      setProfile(data)
      setBio(data.bio || '')
      setLocation(data.location || '')
      refreshUser(data)
    } catch (err) {
      setError(err.response?.data?.message || 'Could not load profile')
    } finally {
      setLoading(false)
    }
  }

  const saveBio = async (e) => {
    e.preventDefault()
    setSavingBio(true)
    try {
      await updateProfile({ bio, location })
      await load()
    } finally {
      setSavingBio(false)
    }
  }

  const addOffered = async (e) => {
    e.preventDefault()
    if (!offeredName.trim()) return
    try {
      await addOfferedSkill({ skillName: offeredName, level: offeredLevel })
      setOfferedName('')
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not add skill')
    }
  }

  const addWanted = async (e) => {
    e.preventDefault()
    if (!wantedName.trim()) return
    try {
      await addWantedSkill({ skillName: wantedName })
      setWantedName('')
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not add skill')
    }
  }

  if (loading) return <div className="flex justify-center py-16"><Spinner /></div>

  return (
    <div className="max-w-2xl mx-auto px-4 py-6">
      <h1 className="text-xl font-semibold mb-1">Your profile</h1>
      <p className="text-gray-500 text-sm mb-6">Manage your bio and skills</p>

      <ErrorBanner message={error} />

      <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-sm mb-5">
        <div className="flex items-center gap-3 mb-4">
          <div className="w-12 h-12 rounded-full bg-brand-100 text-brand-700 flex items-center justify-center font-medium text-lg">
            {profile?.name?.slice(0, 2).toUpperCase()}
          </div>
          <div>
            <p className="font-medium">{profile?.name}</p>
            <p className="text-xs text-gray-400">{profile?.email}</p>
          </div>
          <span className="ml-auto text-sm bg-brand-50 text-brand-700 px-3 py-1 rounded-full font-medium">
            ⭐ {profile?.creditsBalance} credits
          </span>
        </div>

        <form onSubmit={saveBio}>
          <label className="block text-sm text-gray-600 mb-1">Bio</label>
          <textarea
            value={bio} onChange={e => setBio(e.target.value)}
            placeholder="Tell others what you're about..."
            className="w-full border border-gray-200 rounded-lg px-3 py-2 mb-3 text-sm h-20"
          />
          <label className="block text-sm text-gray-600 mb-1">Location</label>
          <input
            value={location} onChange={e => setLocation(e.target.value)}
            placeholder="e.g. Mumbai"
            className="w-full border border-gray-200 rounded-lg px-3 py-2 mb-3 text-sm"
          />
          <button type="submit" disabled={savingBio} className="bg-brand-600 hover:bg-brand-700 text-white text-sm rounded-lg px-4 py-2 font-medium disabled:opacity-60">
            Save profile
          </button>
        </form>
      </div>

      <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-sm mb-5">
        <h3 className="font-medium mb-3">Skills I can teach</h3>
        <div className="flex flex-wrap gap-2 mb-4">
          {profile?.skillsOffered?.length === 0 && <p className="text-sm text-gray-400">None added yet</p>}
          {profile?.skillsOffered?.map(s => (
            <span key={s.id} className="flex items-center gap-1 bg-brand-50 text-brand-700 text-xs px-3 py-1.5 rounded-full">
              {s.name} · {s.level?.toLowerCase()}
              <button onClick={() => removeOfferedSkill(s.id).then(load)} className="text-brand-400 hover:text-red-500 ml-1">✕</button>
            </span>
          ))}
        </div>
        <form onSubmit={addOffered} className="flex gap-2">
          <input
            value={offeredName} onChange={e => setOfferedName(e.target.value)}
            placeholder="e.g. Guitar"
            className="flex-1 border border-gray-200 rounded-lg px-3 py-2 text-sm"
          />
          <select value={offeredLevel} onChange={e => setOfferedLevel(e.target.value)} className="border border-gray-200 rounded-lg px-2 py-2 text-sm">
            {LEVELS.map(l => <option key={l} value={l}>{l.toLowerCase()}</option>)}
          </select>
          <button type="submit" className="bg-brand-600 text-white text-sm rounded-lg px-4 py-2 font-medium">Add</button>
        </form>
      </div>

      <div className="bg-white border border-gray-100 rounded-xl p-5 shadow-sm">
        <h3 className="font-medium mb-3">Skills I want to learn</h3>
        <div className="flex flex-wrap gap-2 mb-4">
          {profile?.skillsWanted?.length === 0 && <p className="text-sm text-gray-400">None added yet</p>}
          {profile?.skillsWanted?.map(s => (
            <span key={s.id} className="flex items-center gap-1 bg-gray-100 text-gray-700 text-xs px-3 py-1.5 rounded-full">
              {s.name}
              <button onClick={() => removeWantedSkill(s.id).then(load)} className="text-gray-400 hover:text-red-500 ml-1">✕</button>
            </span>
          ))}
        </div>
        <form onSubmit={addWanted} className="flex gap-2">
          <input
            value={wantedName} onChange={e => setWantedName(e.target.value)}
            placeholder="e.g. Excel"
            className="flex-1 border border-gray-200 rounded-lg px-3 py-2 text-sm"
          />
          <button type="submit" className="bg-brand-600 text-white text-sm rounded-lg px-4 py-2 font-medium">Add</button>
        </form>
      </div>
    </div>
  )
}
