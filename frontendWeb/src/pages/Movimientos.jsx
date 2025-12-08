import React, { useState, useEffect } from 'react'
import { useAuth } from '../context/AuthContext'
import { getMovimientos } from '../services/bancaApi'
import './Movimientos.css'

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
      const useDates = fechaInicio || fechaFin
      const resp = await getMovimientos(selectedAcc, fechaInicio || null, fechaFin || null)
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
        const sorted = movsAll.slice().sort((a,b)=> (b.date || '').localeCompare(a.date || ''))
        setTransactions(sorted.slice(0,10))
      }
    } catch (e) {
      alert(e.body || e.message || 'Error cargando movimientos')
    }
  }

  useEffect(() => {
    if (selectedAcc) {
      loadMovements()
    }
  }, [])

  return (
    <div className="mov-page">
      <div className="mov-container">
        <h2 className="mov-title">Movimientos</h2>

        <div className="mov-controls">
          <label className="small">Cuenta</label>
          <select value={selectedAcc} onChange={e=>setSelectedAcc(e.target.value)} className="input" style={{minWidth:220}}>
            {state.user.accounts.map(a => <option key={a.id} value={a.number}>{a.number} — ${a.balance.toFixed(2)}</option>)}
          </select>

          <label className="small">Desde</label>
          <input type="date" className="input" value={fechaInicio} onChange={e=>setFechaInicio(e.target.value)} />

          <label className="small">Hasta</label>
          <input type="date" className="input" value={fechaFin} onChange={e=>setFechaFin(e.target.value)} />

          <label className="small">Filtrar por fecha</label>
          <input type="date" className="input" value={filterDate} onChange={e=>setFilterDate(e.target.value)} />

          <button className="btn-primary" onClick={loadMovements}>Cargar movimientos</button>
        </div>

        <div className="card" style={{marginTop: 6}}>
          {filtered.length===0 && <div className="small" style={{padding:16}}>No hay movimientos</div>}
          <ul className="mov-list">
            {filtered.map(tx => (
              <li key={tx.id} className="mov-item">
                <div>
                  <div className="mov-desc">{tx.desc}</div>
                  <div className="mov-meta">{tx.date} — {tx.name}</div>
                </div>
                <div className={`mov-amount ${tx.amount >= 0 ? 'amount-credit' : 'amount-debit'}`}>
                  {tx.amount >= 0 ? `+$${tx.amount.toFixed(2)}` : `-$${Math.abs(tx.amount).toFixed(2)}`}
                </div>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  )
}
