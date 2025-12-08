import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function ProtectedRoute({ children, allowAnonymous = false }) {
  const { loggedIn } = useAuth();
  const location = useLocation();

  // Si el componente permite acceso sin autenticación explícitamente
  if (allowAnonymous) return children;

  // Permitir rutas que contengan "-dev" para pruebas sin login
  if (location?.pathname?.includes('-dev')) return children;

  // Si no está logueado, redirigir al login
  if (!loggedIn) return <Navigate to="/login" replace />;

  return children;
}
