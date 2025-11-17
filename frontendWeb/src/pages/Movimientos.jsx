import React, { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import { getMovimientos } from '../services/bancaApi'

export default function Movimientos(){
  const { state } = useAuth()
  const [filterDate, setFilterDate] = useState('')
  const [fechaInicio, setFechaInicio] = useState('')
  const [fechaFin, setFechaFin] = useState('')
  const [selectedAcc, setSelectedAcc] = useState(state.user.accounts[0]?.number || '')
  const [transactions, setTransactions] = useState(state.transactions || [])
  const filtered = filterDate ? transactions.filter(t => t.date === filterDate) : transactions

  const loadMovements = async () => {
    if (!selectedAcc) return alert('Seleccione una cuenta')
    try {
      // Si el usuario proporcionó fechas, solicitar ese rango; si no, traer todos los movimientos disponibles
      const useDates = fechaInicio || fechaFin
      const resp = await getMovimientos(selectedAcc, fechaInicio || null, fechaFin || null)
      // Mapear correctamente desde MovimientosResponse (MovimientoDetalle)
      const movsAll = (resp && resp.movimientos) ? resp.movimientos.map((m, i) => ({ 
        id: 'mv-'+i, 
        date: m.fechaHora ? m.fechaHora.split('T')[0] : '', 
        desc: m.descripcion || m.tipo || '', 
        name: m.referencia || '', 
        amount: Number(m.monto || 0) * (m.tipoMovimiento === 'DEBITO' ? -1 : 1)
      })) : []
      if (useDates) {
        setTransactions(movsAll)
      } else {
        // ordenar por fecha descendente y limitar a 10
        const sorted = movsAll.slice().sort((a,b)=> (b.date || '').localeCompare(a.date || ''))
        setTransactions(sorted.slice(0,10))
      }
    } catch (e) {
      alert(e.body || e.message || 'Error cargando movimientos')
    }
  }

  // Cargar movimientos automáticamente al entrar a la página
  useEffect(() => {
    if (selectedAcc) {
      loadMovements()
    }
  }, [])

  return (
    <div>
      <h2>Movimientos</h2>
      <div style={{display:'flex', gap:8, alignItems:'center', marginTop:8, marginBottom:12}}>
        <label className="small">Cuenta</label>
        <select value={selectedAcc} onChange={e=>setSelectedAcc(e.target.value)} className="input" style={{width:200}}>
          {state.user.accounts.map(a => <option key={a.id} value={a.number}>{a.number} — ${a.balance.toFixed(2)}</option>)}
        </select>
        <button className="small" onClick={loadMovements} style={{padding:'6px 10px'}}>Cargar movimientos</button>
        <label className="small" style={{marginLeft: 16}}>Desde</label>
        <input type="date" className="input" value={fechaInicio} onChange={e=>setFechaInicio(e.target.value)} />
        <label className="small">Hasta</label>
        <input type="date" className="input" value={fechaFin} onChange={e=>setFechaFin(e.target.value)} />
        <label className="small" style={{marginLeft: 16}}>Filtrar por fecha exacta</label>
        <input type="date" className="input" value={filterDate} onChange={e=>setFilterDate(e.target.value)} />
      </div>

      <div className="card">
        {filtered.length===0 && <div className="small">No hay movimientos</div>}
        <ul style={{listStyle:'none', padding:0}}>
          {filtered.map(tx => (
            <li key={tx.id} style={{display:'flex', justifyContent:'space-between', padding:'8px 0', borderBottom:'1px solid #f3f4f6'}}>
              <div>
                <div style={{fontWeight:600}}>{tx.desc}</div>
                <div className="small">{tx.date} — {tx.name}</div>
              </div>
              <div style={{fontWeight:700}}>{tx.amount >= 0 ? `+$${tx.amount.toFixed(2)}` : `-$${Math.abs(tx.amount).toFixed(2)}`}</div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  )
}
