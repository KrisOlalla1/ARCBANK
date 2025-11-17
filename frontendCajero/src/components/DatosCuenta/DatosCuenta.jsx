import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { cuentas } from '../../services/api';
import '../DatosCuenta/DatosCuenta.css';

export default function DatosCuenta(){
  const [params] = useSearchParams();
  const tipo = params.get('tipo');
  const cuenta = params.get('cuenta');
  const nav = useNavigate();
  const [info,setInfo] = useState(null);
  const [err,setErr] = useState('');

  useEffect(()=>{
    cuentas.getCuenta(cuenta).then(setInfo).catch(()=>setErr('Error cargando cuenta'));
  },[cuenta]);

  if(err) return <p className="screen error">{err}</p>;
  if(!info) return <p className="screen">Cargando...</p>;

  return (
    <>
      <div className="header">
        <div className="left">Inicio</div>
        <div className="right"><button className="btn-logout" onClick={()=>{localStorage.removeItem('cajero'); window.location.href='/'}}>Cerrar sesión</button></div>
      </div>

      <div className="screen">
        <div className="card" style={{maxWidth:620, margin:'0 auto'}}>
          <div style={{display:'flex',justifyContent:'space-between',alignItems:'center'}}>
            <div>
              <p className="small-muted">Nombres</p>
              <h3>{info.nombre}</h3>
            </div>
            <div style={{textAlign:'right'}}>
              <p className="small-muted">N° Cuenta</p>
              <h3>{info.numeroCuenta}</h3>
            </div>
          </div>

          <div style={{display:'flex',gap:20,marginTop:12}}>
            <div>
              <p className="small-muted">Tipo</p>
              <p>{info.tipo}</p>
            </div>
            <div>
              <p className="small-muted">Balance</p>
              <p style={{fontWeight:700}}>${Number(info.balance).toFixed(2)}</p>
            </div>
          </div>

          <div style={{display:'flex',gap:10,marginTop:16}}>
            <button className="btn-primary" onClick={()=>nav(`/valores?tipo=${tipo}&cuenta=${cuenta}`)}>Continuar</button>
            <button className="btn-secondary" onClick={()=>nav(-1)}>Cancelar</button>
          </div>
        </div>
      </div>
    </>
  );
}
