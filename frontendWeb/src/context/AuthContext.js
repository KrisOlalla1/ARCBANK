import React, { createContext, useContext, useEffect, useState } from 'react'


export const API_BASE = (process.env.REACT_APP_API_BASE_URL || '').replace(/\/$/, '')

export async function apiFetch(path, options = {}) {
  const base = API_BASE || ''
  const url = path.startsWith('http') ? path : `${base}${path.startsWith('/') ? path : '/' + path}`

  const opts = {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    credentials: 'include', 
    ...options
  }

  const res = await fetch(url, opts)
  const text = await res.text()
  let data = null
  try { data = text ? JSON.parse(text) : null } catch (e) { data = text }
  if (!res.ok) {
    const error = new Error(res.statusText || 'HTTP error')
    error.status = res.status
    error.body = data
    throw error
  }
  return data
}

const AuthContext = createContext()

const initialUser = {
  id: 'user-1',
  name: 'JUAN',
  phone: '0980094965',
  email: 'juan@example.com',
  address: 'Av. Principal 123',
  accounts: []
}

function readState() {
  const s = localStorage.getItem('namca_state')
  if (s) return JSON.parse(s)

  const state = { user: initialUser, transactions: [] }
  localStorage.setItem('namca_state', JSON.stringify(state))
  return state
}

export function AuthProvider({ children }) {

  const [state, setState] = useState(() => {
    const saved = readState()

    const storedName = localStorage.getItem("namca_username")
    if (storedName) {
      saved.user.name = storedName
    }

    const storedIdent = localStorage.getItem('namca_identificacion')
    const storedIdUsuarioWeb = localStorage.getItem('namca_idUsuarioWeb')
    if (storedIdent) saved.user.identificacion = storedIdent
    if (storedIdUsuarioWeb) saved.user.idUsuarioWeb = Number(storedIdUsuarioWeb)

    return saved
  })

  const [loggedIn, setLoggedIn] = useState(() => !!localStorage.getItem('namca_logged'))

  useEffect(() => {
    localStorage.setItem('namca_state', JSON.stringify(state))
  }, [state])

  const login = async (username, password) => {
    if (!username || !password) return { ok: false, error: 'Usuario y contraseña requeridos' }

    try {
      const body = await apiFetch('/api/usuarios/login', {
        method: 'POST',
        body: JSON.stringify({ nombreUsuario: username, clave: password })
      })

      localStorage.setItem('namca_logged', '1')
      localStorage.setItem('namca_username', username.toUpperCase())

      // Extraer datos directamente de la respuesta del login
      const identificacion = body?.identificacion || null
      const idUsuarioWeb = body?.idUsuario || null
      
      console.log('✅ Login exitoso, datos:', { identificacion, idUsuarioWeb })
      
      if (identificacion) localStorage.setItem('namca_identificacion', identificacion)
      if (idUsuarioWeb) localStorage.setItem('namca_idUsuarioWeb', String(idUsuarioWeb))

      setState(s => ({ ...s, user: { ...s.user, name: username.toUpperCase(), identificacion, idUsuarioWeb } }))
      setLoggedIn(true)
      return { ok: true, body }
    } catch (err) {
      return { ok: false, error: err.body || err.message || 'Error de red' }
    }
  }

  const logout = () => {
    localStorage.removeItem('namca_logged')
    localStorage.removeItem('namca_username')
    localStorage.removeItem('namca_identificacion')
    localStorage.removeItem('namca_idUsuarioWeb')
    localStorage.removeItem('namca_state')
    setState({ user: { ...initialUser, accounts: [] }, transactions: [] })
    setLoggedIn(false)
  }

  const updateUser = (patch) => {
    setState(s => ({ 
      ...s, 
      user: { ...s.user, ...patch } 
    }))
  }

  const persistIdentification = (identificacion, idUsuarioWeb) => {
    if (identificacion) localStorage.setItem('namca_identificacion', identificacion)
    if (idUsuarioWeb) localStorage.setItem('namca_idUsuarioWeb', String(idUsuarioWeb))
    updateUser({ identificacion, idUsuarioWeb })
  }

  const setUserAccounts = (accounts) => {
    setState(s => ({ ...s, user: { ...s.user, accounts } }))
  }

  const addTransaction = (tx) => {
    setState(s => {
      const txs = [tx, ...(s.transactions || [])]
      const accounts = s.user.accounts.map(a =>
        a.id === tx.accId 
          ? { ...a, balance: Number((a.balance + tx.amount).toFixed(2)) }
          : a
      )
      return { ...s, transactions: txs, user: { ...s.user, accounts } }
    })
  }

  return (
    <AuthContext.Provider value={{
      state,
      login,
      logout,
      loggedIn,
      updateUser,
      persistIdentification,
      setUserAccounts,
      addTransaction
    }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
