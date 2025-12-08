import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth, apiFetch } from "../context/AuthContext";
import { FaUser, FaLock, FaEyeSlash } from "react-icons/fa";
import "./Login.css";

export default function Login() {
  const { login, persistIdentification } = useAuth();
  const navigate = useNavigate();
  const [user, setUser] = useState("");
  const [pass, setPass] = useState("");
  const [err, setErr] = useState("");
  const [showRegister, setShowRegister] = useState(false)
  const [regUser, setRegUser] = useState("")
  const [regPass, setRegPass] = useState("")
  const [regTipoId, setRegTipoId] = useState("CEDULA")
  const [regId, setRegId] = useState("")
  const [regSucursal, setRegSucursal] = useState(1)
  const [regMsg, setRegMsg] = useState("")

  const submit = async (e) => {
    e.preventDefault()
    setErr('')

    const res = await login(user, pass)
    if (!res.ok) return setErr(res.error)

    setTimeout(() => navigate('/'), 100)
  }

  const submitRegister = async (e) => {
    e.preventDefault()
    setRegMsg('')

    try {
      const body = {
        nombreUsuario: regUser,
        clave: regPass,
        tipoIdentificacion: regTipoId,
        identificacion: regId,
        idSucursal: Number(regSucursal)
      }

      const resp = await apiFetch('/api/usuarios/registro', { method: 'POST', body: JSON.stringify(body) })
      setRegMsg('Registro exitoso')
      setShowRegister(false)
      setUser(regUser)
      try { persistIdentification(regId, resp && resp.idUsuario) } catch (e) { /* no crítico */ }
    } catch (e) {
      setRegMsg(e.body || e.message || 'Error en registro')
    }
  }


  return (
    <div className="login-page" style={styles.page}>
      <div className="left" style={styles.left}>
        <h1 className="login-logo" style={styles.logo}>ARCBANK</h1>

        <div className="login-box" style={styles.loginBox}>
          <h2 style={styles.title}>Bienvenidos</h2>
          <h3 style={styles.subtitle}>Ingresa a tu Banca</h3>

          {err && <div style={styles.error}>{err}</div>}

          <form onSubmit={submit}>
              <div className="step-group">
                <div style={styles.inputGroup}>
                  <FaUser style={styles.icon} />
                  <input
                    style={styles.input}
                    placeholder="Usuario"
                    value={user}
                    onChange={(e) => setUser(e.target.value)}
                  />
                </div>
                <div style={styles.forgot}>¿Olvidaste tu usuario?</div>

                <div style={styles.inputGroup}>
                  <FaLock style={styles.icon} />
                  <input
                    type="password"
                    style={styles.input}
                    placeholder="Contraseña"
                    value={pass}
                    onChange={(e) => setPass(e.target.value)}
                  />
                  <FaEyeSlash style={styles.eyeIcon} />
                </div>
                <div style={styles.forgot}>¿Olvidaste tu contraseña?</div>
              </div>

            <button type="submit" style={styles.button}>
              Ingresar
            </button>
            <button
              type="button"
              style={{ ...styles.button, marginTop: 10, background: 'transparent', color: '#315eb3', border: '2px solid #315eb3' }}
              onClick={() => { setShowRegister(s => !s); setRegMsg('') }}
            >
              {showRegister ? 'Cancelar' : 'Registrar'}
            </button>
          </form>

          {showRegister && (
            <div style={{ marginTop: 20 }}>
              <h3 style={{ marginBottom: 8 }}>Registro de Usuario</h3>
              {regMsg && <div style={{ color: regMsg.includes('exitoso') ? 'green' : 'red', marginBottom: 8 }}>{regMsg}</div>}
              <form onSubmit={submitRegister}>
                <div style={styles.inputGroup}>
                  <input style={styles.input} placeholder="Usuario" value={regUser} onChange={e => setRegUser(e.target.value)} />
                </div>
                <div style={styles.inputGroup}>
                  <input type="password" style={styles.input} placeholder="Contraseña" value={regPass} onChange={e => setRegPass(e.target.value)} />
                </div>
                <div style={styles.inputGroup}>
                  <select style={styles.input} value={regTipoId} onChange={e => setRegTipoId(e.target.value)}>
                    <option value="CEDULA">CÉDULA</option>
                    <option value="PASAPORTE">PASAPORTE</option>
                  </select>
                </div>
                <div style={styles.inputGroup}>
                  <input style={styles.input} placeholder="Número de identificación" value={regId} onChange={e => setRegId(e.target.value)} />
                </div>
                <div style={styles.inputGroup}>
                  <input type="number" style={styles.input} placeholder="Sucursal (id)" value={regSucursal} onChange={e => setRegSucursal(e.target.value)} />
                </div>
                <button type="submit" style={{ ...styles.button, background: 'linear-gradient(to right, #2e8b57, #3cb371)' }}>Crear cuenta</button>
              </form>
            </div>
          )}
        </div>
      </div>

      <div className="right" style={styles.right}>
        <div className="right-card">
          <h2 className="reco-title">Recomendaciones</h2>
          <ol className="reco-list">
            <li>Cuida bien tu usuario y contraseña</li>
            <li>Verifica todo antes de ingresar</li>
          </ol>

          <div className="reco-avatar">
            <img
              src="https://cdn-icons-png.flaticon.com/512/706/706830.png"
              alt="persona"
              className="reco-icon"
            />
          </div>
        </div>
      </div>
    </div>
  );
}

const styles = {
  page: {
    display: "flex",
    height: "100vh",
    background: "linear-gradient(to right, #f5f5f5, #e8e8e8)",
  },
  left: {
    flex: 1,
    padding: "20px 20px",
    display: "flex",
    flexDirection: "column",
    alignItems: "flex-start",
    justifyContent: "flex-start",
  },
  right: {
    flex: 1,
    padding: "24px 24px",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
  },
  logo: {
    fontSize: 40,
    fontWeight: "bold",
    color: "#b8860b",
    marginBottom: 0,
  },
  loginBox: {
    background: "white",
    padding: 32,
    borderRadius: 20,
    width: 460,
    minHeight: 520,
    boxShadow: "0 6px 15px rgba(0,0,0,0.1)",
  },
  title: {
    fontSize: 38,
    fontWeight: 700,
    marginBottom: 5,
  },
  subtitle: {
    fontSize: 22,
    marginBottom: 25,
  },
  error: {
    color: "red",
    marginBottom: 15,
  },
  inputGroup: {
    position: "relative",
    marginBottom: 10,
  },
  input: {
    width: "100%",
    padding: "10px 40px",
    borderRadius: 4,
    border: "2px solid #315eb3",
    fontSize: 16,
    outline: "none",
  },
  icon: {
    position: "absolute",
    top: "50%",
    left: 10,
    transform: "translateY(-50%)",
    color: "#0a0a0a",
  },
  eyeIcon: {
    position: "absolute",
    top: "50%",
    right: 10,
    transform: "translateY(-50%)",
    cursor: "pointer",
  },
  forgot: {
    fontSize: 13,
    textAlign: "right",
    marginBottom: 10,
    color: "#3f3f3f",
  },
  button: {
    marginTop: 15,
    width: "100%",
    padding: 12,
    fontSize: 18,
    background: "linear-gradient(to right, orange, gold)",
    borderRadius: 20,
    border: "none",
    cursor: "pointer",
  },
  recoTitle: {
    fontSize: 32,
    fontWeight: 700,
    marginBottom: 20,
  },
  recoList: {
    fontSize: 20,
    lineHeight: 1.6,
  },
};
