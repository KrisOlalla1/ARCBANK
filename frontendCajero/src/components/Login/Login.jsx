import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { auth } from "../../services/api";
import "./Login.css";

import logo from "../../assets/logo.png";
import sideImg from "../../assets/login-side.png";

export default function Login() {
  const [usuario, setUsuario] = useState("");
  const [clave, setClave] = useState("");
  const [error, setError] = useState("");
  const nav = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!usuario || !clave) {
      setError("Usuario y contraseña son requeridos");
      return;
    }
    if (!/^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$/.test(usuario)) {
      setError("El usuario solo puede contener letras y espacios");
      return;
    }

    try {
      const res = await auth.login(usuario, clave);
      localStorage.setItem("cajero", JSON.stringify(res.cajero));
      nav("/seleccionar");
    } catch (err) {
      setError("Credenciales inválidas o error de servidor");
    }
  };

  return (
    <div className="login-container">

      <div className="left-section">

        <img src={logo} alt="ARC BANK" className="logo" />

        <div className="login-box">

          <h1>Bienvenido</h1>
          <p className="subtitle">
            Ingresa a tu usuario y contraseña, para ingresar al sistema.
          </p>

          <form onSubmit={handleSubmit}>

            <div className="input-group">
              <input
                  type="text"
                  placeholder="Usuario"
                  value={usuario}
                  onChange={(e) => {
                    const value = e.target.value;

                    if (/^[A-Za-zÁÉÍÓÚáéíóúÑñ ]*$/.test(value)) {
                      setUsuario(value);
                    }
                  }}
                />
            </div>
            <a className="forgot">¿Olvidaste tu usuario?</a>

            <div className="input-group">
              <i className="fa-solid fa-lock icon"></i>
              <input
                type="password"
                placeholder="Contraseña"
                value={clave}
                onChange={(e) => setClave(e.target.value)}
              />
              <i className="fa-solid fa-eye-slash icon-right"></i>
            </div>
            <a className="forgot">¿Olvidaste tu contraseña?</a>

            {error && <p className="error">{error}</p>}

            <button className="btn-login">Ingresar</button>
          </form>

        </div>
      </div>

      <div className="right-section">
        <p className="security-text">
          Recuerda cuidar bien tu usuario y contraseña, no lo compartas con tus
          compañeros, recuerda que son datos sensibles.
        </p>

        <img src={sideImg} alt="Imagen informativa" className="side-img" />
      </div>
    </div>
  );
}
