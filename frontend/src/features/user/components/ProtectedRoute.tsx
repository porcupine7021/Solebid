import React, { useEffect, useState } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

interface Props {
  children: React.ReactElement;
}

const ProtectedRoute: React.FC<Props> = ({ children }) => {
  const { isAuthenticated, refreshMe } = useAuth();
  const location = useLocation();
  const [checking, setChecking] = useState(true);

  useEffect(() => {
    let alive = true;
    (async () => {
      if (!isAuthenticated) {
        await refreshMe();
      }
      if (alive) setChecking(false);
    })();
    return () => { alive = false; };
  }, [isAuthenticated, refreshMe]);

  if (checking) {
    return <div className="min-h-[200px] flex items-center justify-center text-gray-500 text-sm">확인 중…</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }
  return children;
};

export default ProtectedRoute;
