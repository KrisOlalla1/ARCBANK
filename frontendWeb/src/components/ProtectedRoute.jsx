import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function ProtectedRoute({ children, allowAnonymous = false }){
  const { loggedIn } = useAuth()
  if (allowAnonymous) return children
  if (!loggedIn) return <Navigate to="/login" replace />
  return children
}
