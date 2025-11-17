import { useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { cuentas } from '../../services/api';
import '../BuscarCuenta/BuscarCuenta.css';

export default function BuscarCuenta(){
  const [params] = useSearchParams();
  const tipo = params.get('tipo');
  const [numero,setNumero] = useState('');
  const [error,setError] = useState('');
  const navigate = useNavigate();

  const buscar = async ()=>{
    setError('');
    try{
      await cuentas.getCuenta(numero);
      navigate(`/datos?tipo=${tipo}&cuenta=${numero}`);
    }catch(e){
      setError('Cuenta no encontrada');
    }
  };

  return (
    <>
      <div className="header">
        <div className="left">Inicio</div>
        <div className="right"><button className="btn-logout" onClick={()=>{localStorage.removeItem('cajero'); window.location.href='/'}}>Cerrar sesión</button></div>
      </div>

      <div className="screen">
        <div className="card" style={{maxWidth:520, margin:'0 auto'}}>
          <h3>{tipo} - Buscar</h3>
          <label>Número de cédula / cuenta</label>
          <input value={numero} onChange={e=>setNumero(e.target.value)} placeholder="1726689095 o 2258102613" />
          {error && <div className="error">{error}</div>}
          <div style={{display:'flex',gap:8, marginTop:12}}>
            <button className="btn-primary" onClick={buscar}>Buscar</button>
            <button className="btn-secondary" onClick={()=>history.back()}>Volver</button>
          </div>
        </div>
      </div>
    </>
  );
}
