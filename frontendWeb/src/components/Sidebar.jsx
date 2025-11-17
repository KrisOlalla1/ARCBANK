import React from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { FiHome, FiList, FiLogOut } from "react-icons/fi"
import { TbArrowsExchange } from "react-icons/tb"   // ← ICONO DE TRANSFERENCIA REAL

export default function Sidebar(){
  const { state, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <aside className="sidebar" style={styles.sidebar}>
      <div className="brand" style={styles.brand}>ARCBANK</div>

      <div className="profile-mini" style={styles.profileMini}>
        <div style={{ display: "flex", alignItems: "center", gap: 10 }}>
          <div style={styles.circleIcon}>👤</div>

          <div style={{ display: "flex", flexDirection: "column", lineHeight: "18px" }}>
            <span style={{ fontWeight: 700 }}>{state.user.name}</span>
            <NavLink to="/perfil" className="small" style={styles.profileLink}>
              Mi perfil
            </NavLink>
          </div>
        </div>
      </div>

      <nav style={styles.menuContainer}>
        <ul className="nav-list" style={styles.navList}>

          <li>
            <NavLink to="/" end className={({isActive}) => isActive ? 'active' : ''} style={styles.navItem}>
              <FiHome size={18} />
              Inicio
            </NavLink>
          </li>

          <li>
            <NavLink to="/movimientos" className={({isActive}) => isActive ? 'active' : ''} style={styles.navItem}>
              <FiList size={18} />
              Movimientos
            </NavLink>
          </li>

          <li>
            <NavLink to="/transferir" className={({isActive}) => isActive ? 'active' : ''} style={styles.navItem}>
              <TbArrowsExchange size={20} />   {/* ICONO NUEVO */}
              Transferir
            </NavLink>
          </li>

        </ul>
      </nav>

      <div style={styles.logoutContainer}>
        <button onClick={handleLogout} className="btn ghost" style={styles.logoutButton}>
          <FiLogOut size={18} />
          Cerrar sesión
        </button>
      </div>
    </aside>
  )
}

const styles = {
  sidebar: {
    width: "260px",
    background: "#fff",
    borderRight: "1px solid #e5e5e5",
    display: "flex",
    flexDirection: "column",
    padding: "25px 20px",
    height: "100vh"
  },
  brand: {
    fontSize: 28,
    fontWeight: 800,
    color: "#b8860b",
    marginBottom: 25
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
    fontSize: 20
  },
  profileLink: {
    fontSize: 14,
    color: "#444",
    marginTop: 4,
    textDecoration: "none"
  },
  menuContainer: { flexGrow: 1 },
  navList: {
    listStyle: "none",
    padding: 0,
    display: "flex",
    flexDirection: "column",
    gap: 15
  },
  navItem: {
    display: "flex",
    alignItems: "center",
    gap: 10,
    textDecoration: "none",
    color: "#333",
    fontSize: 16,
    padding: "8px 0"
  },
  logoutContainer: {
    marginTop: "auto",
    paddingTop: 20,
    borderTop: "1px solid #eee"
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
    color: "#333"
  }
}
