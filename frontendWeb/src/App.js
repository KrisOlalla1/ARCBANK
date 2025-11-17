import React from "react";
import { Routes, Route, useLocation } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import ProtectedRoute from "./components/ProtectedRoute";
import { useAuth } from "./context/AuthContext";

import Login from "./pages/Login";
import Home from "./pages/Home";
import Movimientos from "./pages/Movimientos";
import Transferir from "./pages/Transferir";
import Perfil from "./pages/Perfil";

export default function App() {
  const location = useLocation();
  const { loggedIn } = useAuth();

  const isLoginPage = location.pathname === "/login";

  return (
    <div className="app-shell">
      {/* SOLO mostrar el sidebar si NO estamos en login y el usuario está logueado */}
      {loggedIn && !isLoginPage && <Sidebar />}

      <main className="main" style={{ width: "100%" }}>
        <Routes>
          <Route path="/login" element={<Login />} />

          <Route
            path="/"
            element={
              <ProtectedRoute>
                <Home />
              </ProtectedRoute>
            }
          />

          <Route
            path="/movimientos"
            element={
              <ProtectedRoute>
                <Movimientos />
              </ProtectedRoute>
            }
          />

          <Route
            path="/transferir"
            element={
              <ProtectedRoute>
                <Transferir />
              </ProtectedRoute>
            }
          />

          <Route
            path="/perfil"
            element={
              <ProtectedRoute>
                <Perfil />
              </ProtectedRoute>
            }
          />
        </Routes>
      </main>
    </div>
  );
}
