import { apiFetch } from '../context/AuthContext'

// Cliente sencillo para endpoints de bancaweb
export async function getConsolidada(identificacion) {
  return await apiFetch(`/api/cuentas/consolidada/${encodeURIComponent(identificacion)}`)
}

export async function getMovimientos(numeroCuenta, fechaInicio, fechaFin) {
  let url = `/api/cuentas/movimientos/${encodeURIComponent(numeroCuenta)}`
  const params = []
  if (fechaInicio) params.push(`fechaInicio=${encodeURIComponent(fechaInicio)}`)
  if (fechaFin) params.push(`fechaFin=${encodeURIComponent(fechaFin)}`)
  if (params.length) url += `?${params.join('&')}`
  return await apiFetch(url)
}

export async function realizarTransferencia(request) {
  return await apiFetch('/api/transferencias', { method: 'POST', body: JSON.stringify(request) })
}

const bancaApi = {
  getConsolidada,
  getMovimientos,
  realizarTransferencia
}

export default bancaApi
