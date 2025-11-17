import React, { useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import { Link } from 'react-router-dom'
import { getConsolidada } from '../services/bancaApi'

export default function Home(){
  const { state } = useAuth()
  const { setUserAccounts } = useAuth()
  
  useEffect(() => {
    const loadAccounts = async () => {
      const id = state.user && state.user.identificacion
      console.log('🔍 Home - Identificacion del usuario:', id)
      console.log('🔍 Home - State completo:', state.user)
      if (!id) {
        console.warn('⚠️ No hay identificación, no se cargan cuentas')
        return
      }
      try {
        console.log('📡 Llamando a /api/cuentas/consolidada/' + id)
        const cuentas = await getConsolidada(id)
        console.log('✅ Cuentas recibidas:', cuentas)
        const mapped = (cuentas || []).map(c => ({ 
          id: String(c.idCuenta), 
          number: c.numeroCuenta, 
          type: c.tipoCuenta, 
          balance: Number(c.saldoActual || 0) 
        }))
        console.log('✅ Cuentas mapeadas:', mapped)
        setUserAccounts(mapped)
      } catch (e) {
        console.error('❌ Error cargando cuentas:', e.body || e.message)
      }
    }
    loadAccounts()
  }, [state.user.identificacion])
  return (
    <div>
      <div className="header-inline">
        <h1>Inicio</h1>
        <div className="small">Bienvenido, {state.user.name}</div>
      </div>

      <div style={{display:'grid', gridTemplateColumns:'repeat(auto-fit,minmax(220px,1fr))', gap:12}}>
        {state.user.accounts.map(a => (
          <div className="card" key={a.id}>
            <div className="small">{a.type} | N°. {a.number}</div>
            <div style={{fontSize:20,fontWeight:700}}>${a.balance.toFixed(2)}</div>
            <div style={{marginTop:8}}>
              <Link to={`/movimientos`} className="small">Ver movimientos</Link>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
