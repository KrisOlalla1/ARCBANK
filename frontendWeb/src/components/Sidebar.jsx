import React, { useState, useEffect } from "react";
import { NavLink, useNavigate, useLocation } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { FiHome, FiList, FiLogOut } from "react-icons/fi";
import { TbArrowsExchange } from "react-icons/tb";

export default function Sidebar({ isOpen = true, onRequestClose }) {
  const { state, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const isDevView =
    location.pathname && location.pathname.indexOf("-dev") !== -1;

  const interbancariasPath = isDevView
    ? "/interbancarias-dev"
    : "/interbancarias";
  const transferirPath = isDevView ? "/transferir-dev" : "/transferir";

  const [isMobile, setIsMobile] = useState(
    typeof window !== "undefined" ? window.innerWidth <= 900 : false
  );

  useEffect(() => {
    const onResize = () => setIsMobile(window.innerWidth <= 900);
    window.addEventListener("resize", onResize);
    return () => window.removeEventListener("resize", onResize);
  }, []);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  // Si sidebar está cerrado (en pantallas pequeñas), no se muestra
  const hiddenStyle = isOpen ? {} : { display: "none" };

  // En mobile y abierto, se muestra como overlay fijo
  const mobileOverlayStyle =
    isMobile && isOpen
      ? {
          position: "fixed",
          left: 0,
          top: 0,
          height: "100%",
          zIndex: 1500,
          boxShadow: "0 10px 30px rgba(0,0,0,0.15)",
          background: "#fff",
        }
      : {};

  const sidebarStyle = { ...styles.sidebar, ...mobileOverlayStyle, ...hiddenStyle };

  const handleNavClick = () => {
    // Cerrar sidebar en mobile después de navegar
    if (isMobile && typeof onRequestClose === "function") onRequestClose();
  };

  return (
    <aside className="sidebar" style={sidebarStyle}>
      <div className="brand" style={styles.brand}>
        ARCBANK
      </div>

      <div className="profile-mini" style={styles.profileMini}>
        <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
          <div style={styles.circleIcon}>👤</div>

          <div
            style={{
              display: "flex",
              flexDirection: "column",
              lineHeight: "18px",
            }}
          >
            <span style={{ fontWeight: 700 }}>
              {state?.user?.name || "Invitado"}
            </span>
            <NavLink
              to="/perfil"
              className="small"
              style={styles.profileLink}
              onClick={handleNavClick}
            >
              Mi perfil
            </NavLink>
          </div>
        </div>
      </div>

      <nav style={styles.menuContainer}>
        <ul className="nav-list" style={styles.navList}>
          <li>
            <NavLink
              to="/"
              end
              className={({ isActive }) => (isActive ? "active" : "")}
              style={styles.navItem}
              onClick={handleNavClick}
            >
              <FiHome size={18} />
              Inicio
            </NavLink>
          </li>

          <li>
            <NavLink
              to="/movimientos"
              className={({ isActive }) => (isActive ? "active" : "")}
              style={styles.navItem}
              onClick={handleNavClick}
            >
              <FiList size={18} />
              Movimientos
            </NavLink>
          </li>

          <li>
            <NavLink
              to={transferirPath}
              className={({ isActive }) => (isActive ? "active" : "")}
              style={styles.navItem}
              onClick={handleNavClick}
            >
              <TbArrowsExchange size={20} />
              Transferir
            </NavLink>
          </li>

          <li>
            <NavLink
              to={interbancariasPath}
              className={({ isActive }) => (isActive ? "active" : "")}
              style={styles.navItem}
              onClick={handleNavClick}
            >
              <TbArrowsExchange size={20} />
              Transferencias Interbancarias
            </NavLink>
          </li>
        </ul>
      </nav>

      <div style={styles.logoutContainer}>
        <button
          onClick={() => {
            handleLogout();
            if (isMobile && typeof onRequestClose === "function")
              onRequestClose();
          }}
          className="btn ghost"
          style={styles.logoutButton}
        >
          <FiLogOut size={18} />
          Cerrar sesión
        </button>
      </div>
    </aside>
  );
}

const styles = {
  sidebar: {
    width: "260px",
    background: "#fff",
    borderRight: "1px solid #e5e5e5",
    display: "flex",
    flexDirection: "column",
    padding: "25px 20px",
    height: "100vh",
  },
  brand: {
    fontSize: 28,
    fontWeight: 800,
    color: "#b8860b",
    marginBottom: 25,
  },
  profileMini: { marginBottom: 40 },
  circleIcon: {
    width: 38,
    height: 38,
    background: "#ffd54f",
    borderRadius: "50%",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    fontSize: 20,
  },
  profileLink: {
    fontSize: 14,
    color: "#444",
    marginTop: 4,
    textDecoration: "none",
  },
  menuContainer: { flexGrow: 1 },
  navList: {
    listStyle: "none",
    padding: 0,
    display: "flex",
    flexDirection: "column",
    gap: 15,
  },
  navItem: {
    display: "flex",
    alignItems: "center",
    gap: 10,
    textDecoration: "none",
    color: "#333",
    fontSize: 16,
    padding: "8px 0",
  },
  logoutContainer: {
    marginTop: "auto",
    paddingTop: 20,
    borderTop: '1px solid #eee',
  },
  logoutButton: {
    padding: "8px 12px",
    fontSize: 16,
    display: "flex",
    alignItems: "center",
    gap: 10,
    width: "100%",
    justifyContent: "flex-start",
    background: "transparent",
    border: "none",
    cursor: "pointer",
    color: "#333",
  },
};
