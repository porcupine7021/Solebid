import { useCallback, useEffect, useMemo, useState } from 'react';
import type { AuthUser } from '../types/AuthTypes';
import { logout as apiLogout } from '../services/AuthService';

function readSessionUser(): AuthUser | null {
  try {
    const raw = sessionStorage.getItem('auth.user');
    if (!raw) return null;
    return JSON.parse(raw) as AuthUser;
  } catch (e) {
    console.debug('readSessionUser: failed to parse', e);
    return null;
  }
}

export function useAuth() {
  const [user, setUser] = useState<AuthUser | null>(() => readSessionUser());

  const isAuthenticated = useMemo(() => Boolean(user && (user.userId || user.email)), [user]);

  // 세션 변경 이벤트 구독
  useEffect(() => {
    const handler = (evt: Event) => {
      try {
        const u = (evt as CustomEvent).detail?.user as AuthUser | null | undefined;
        if (u) setUser(u);
        else setUser(null);
      } catch (e) {
        console.debug('auth event parse error', e);
      }
    };
    window.addEventListener('auth-changed', handler as EventListener);
    return () => window.removeEventListener('auth-changed', handler as EventListener);
  }, []);

  const refreshMe = useCallback(async (): Promise<AuthUser | null> => {
    try {
      const res = await fetch('/api/users/me', { credentials: 'include' });
      const payload = await res.json();
      if (res.ok && payload?.success && payload?.data) {
        try { sessionStorage.setItem('auth.user', JSON.stringify(payload.data)); } catch (e) { console.debug('failed to cache user', e); }
        const evt = new CustomEvent('auth-changed', { detail: { user: payload.data } });
        window.dispatchEvent(evt);
        setUser(payload.data as AuthUser);
        return payload.data as AuthUser;
      }
    } catch (e) {
      console.debug('refreshMe failed', e);
    }
    return null;
  }, []);

  const logout = useCallback(async () => {
    try { await apiLogout(); } catch (e) { console.debug('logout api failed', e); }
    try { sessionStorage.removeItem('auth.user'); } catch (e) { console.debug('remove session failed', e); }
    const evt = new CustomEvent('auth-changed', { detail: { user: null } });
    window.dispatchEvent(evt);
    setUser(null);
  }, []);

  return { user, isAuthenticated, refreshMe, logout } as const;
}
