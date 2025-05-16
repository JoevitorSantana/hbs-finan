import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from 'contexts/AuthProvider';

/**
 * Só renderiza o children se o usuário possuir o papel indicado.
 * Ex.: <RequireRole role="ADMIN"><Usuarios /></RequireRole>
 */
export default function RequireRole({ role, children }) {
  const { user } = useAuth();
  const location = useLocation();

  if (!user) {
    // não logado → volta pro login
    return <Navigate to="/login" state={{ from: location }} replace />;
  }
  if (user.role !== role) {
    // logado mas sem permissão → 403
    return <Navigate to="/unauthorized" replace />;
  }
  return children;
}
