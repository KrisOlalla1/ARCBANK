import React from "react";
import { Routes, Route, useLocation } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import ProtectedRoute from "./components/ProtectedRoute";
import { useAuth } from "./context/AuthContext";

import Login from "./pages/Login";
import Home from "./pages/Home";
import Movimientos from "./pages/Movimientos";
import Transferir from "./pages/Transferir";
import TransaccionesInterbancarias from "./pages/TransaccionesInterbancarias";
import Perfil from "./pages/Perfil";

export default function App() {
  const location = useLocation();
  const { loggedIn } = useAuth();

  const isLoginPage = location.pathname === "/login";
  const isDevView = location.pathname === "/home-dev" || location.pathname === "/interbancarias-dev";

  return (
    <div className="app-shell">
      {}
      {(loggedIn || isDevView) && !isLoginPage && <Sidebar />}

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

          {/* Ruta temporal: Transferir sin login para desarrollo */}
          <Route
            path="/transferir-dev"
            element={
              <ProtectedRoute allowAnonymous={true}>
                <Transferir />
              </ProtectedRoute>
            }
          />

          <Route
            path="/interbancarias"
            element={
              <ProtectedRoute>
                <TransaccionesInterbancarias />
              </ProtectedRoute>
            }
          />

          {/* Ruta temporal: permitir acceso sin login para desarrollo */}
          <Route
            path="/interbancarias-dev"
            element={
              <ProtectedRoute allowAnonymous={true}>
                <TransaccionesInterbancarias />
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

          {/* Ruta temporal para desarrollo: Home sin login */}
          <Route
            path="/home-dev"
            element={
              <ProtectedRoute allowAnonymous={true}>
                <Home />
              </ProtectedRoute>
            }
          />
        </Routes>
      </main>
    </div>
  );
}
