import "./Sidebar.css";
import logo from "../../assets/logo.png";
import { useNavigate } from "react-router-dom";

export default function Sidebar({ cajero }) {
  const nav = useNavigate();

  const nombreCompleto = cajero?.nombreCompleto || "Cajero";
  const primerNombre = nombreCompleto.split(" ")[0];

  const cerrarSesion = () => {
    localStorage.removeItem("cajero");
    nav("/");
  };

  return (
    <div className="sidebar">
      <img src={logo} alt="ARC BANK" className="sidebar-logo" />

      <div className="sidebar-user">
        <div className="sidebar-user-icon">
          <i className="fa-solid fa-user"></i>
        </div>

        <span className="sidebar-user-name">{primerNombre}</span>
      </div>

      <div className="sidebar-options">
        <button className="sidebar-item">
          <i className="fa-solid fa-house"></i>
          Inicio
        </button>
      </div>

      <button className="sidebar-logout" onClick={cerrarSesion}>
        <i className="fa-solid fa-right-from-bracket"></i>
        Cerrar sesión
      </button>
    </div>
  );
}
