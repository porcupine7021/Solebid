# 인증 사용 가이드

이 문서는 화면/서비스 코드에서 인증 상태와 사용자 정보를 간단히 쓰는 방법만 정리합니다.

- 토큰은 HttpOnly 쿠키로 관리됩니다. Authorization 헤더는 필요 없습니다.
- 모든 API 요청은 credentials: 'include'가 자동으로 포함되어야 합니다(apiFetch 권장).

## 1) 현재 로그인 사용자 접근: useAuth
- 경로: `src/features/user/hooks/useAuth.ts`
- 제공: `{ user, isAuthenticated, refreshMe, logout }`

사용 예시
```tsx
import { useAuth } from '@/features/user/hooks/useAuth';

export default function MySection() {
  const { user, isAuthenticated, refreshMe, logout } = useAuth();

  if (!isAuthenticated) return <a href="/login">로그인</a>;

  return (
    <div>
      <p>안녕하세요, {user?.nickname ?? user?.email}</p>
      <button onClick={() => void refreshMe()}>내 정보 새로고침</button>
      <button onClick={() => void logout()}>로그아웃</button>
    </div>
  );
}
```
팁
- 첫 진입에 세션 캐시가 없으면 `refreshMe()`가 `/api/users/me`로 동기화합니다.

## 2) 인증 필요한 페이지 보호: ProtectedRoute
- 경로: `src/features/user/components/ProtectedRoute.tsx`
- 미로그인 시 `/login`으로 이동. 쿠키만 있는 케이스를 위해 최초 진입에 자동 동기화 수행.

라우터 적용 예시 (일부 이미 적용됨)
```tsx
<Route path="/profile" element={<ProtectedRoute><ProfilePage /></ProtectedRoute>} />
<Route path="/wish" element={<ProtectedRoute><WishPage /></ProtectedRoute>} />
```

## 3) API 호출: apiFetch 공통 래퍼
- 경로: `src/utils/apiFetch.ts`
- 기본으로 `credentials: 'include'` 포함, JSON 전송은 `json` 옵션 사용

예시
```ts
import { apiFetch } from '@/utils/apiFetch';

// GET
const me = await apiFetch('/api/users/me');

// POST(JSON 자동 직렬화)
const res = await apiFetch('/api/users/login', {
  method: 'POST',
  json: { email, password },
});
```

## 4) 로그인/닉네임 완료 등 사용자 정보 갱신 알림
- 경로: `src/features/user/services/AuthService.ts`
- 함수: `cacheUserAndEmit(user)`
- 효과: 세션(`sessionStorage['auth.user']`) 저장 + 전역 이벤트(`auth-changed`) 브로드캐스트

사용 예시
```ts
import { cacheUserAndEmit } from '@/features/user/services/AuthService';

// 로그인 성공 시 응답 사용자 객체를 전달
cacheUserAndEmit(loginResponse.data);
```

## 5) 실제 연동 예: 포인트 조회
- API: `GET /api/users/{userId}/points` (백엔드에서 DTO로 응답)
- 프런트: `src/features/profile/api/point.ts`

예시
```ts
import { apiFetch } from '@/utils/apiFetch';

export async function fetchUserPoint(userId: number) {
  return apiFetch(`/api/users/${userId}/points`);
}
```

## 문제 해결 체크리스트
- 401/403 발생: 쿠키 전송 여부 확인(apiFetch 사용 또는 `credentials: 'include'`).
- 첫 렌더에서 비로그인처럼 보임: ProtectedRoute 또는 `refreshMe()`로 동기화.
- 사용자 표시 정보가 갱신되지 않음: 갱신 시점에 `cacheUserAndEmit(updatedUser)` 호출 필요.

