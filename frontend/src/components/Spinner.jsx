import React from 'react'

export default function Spinner({ small }) {
  return (
    <div className={`animate-spin rounded-full border-brand-500 border-t-transparent ${small ? 'h-4 w-4 border-2' : 'h-8 w-8 border-2'}`} />
  )
}
