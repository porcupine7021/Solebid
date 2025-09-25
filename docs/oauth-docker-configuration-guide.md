# OAuth 소셜 로그인 Docker 환경 설정 가이드

이 문서는 Solebid 프로젝트에서 Docker 환경에서 OAuth 소셜 로그인(Google, Kakao)을 설정하고 문제를 해결하는 방법을 설명합니다.

## 목차

1. [개요](#개요)
2. [Docker 환경별 포트 구성](#docker-환경별-포트-구성)
3. [OAuth 제공자 설정](#oauth-제공자-설정)
4. [백엔드 설정](#백엔드-설정)
5. [프론트엔드 설정](#프론트엔드-설정)
6. [환경 변수 설정](#환경-변수-설정)
7. [CORS 설정](#cors-설정)
8. [문제 해결](#문제-해결)
9. [테스트 방법](#테스트-방법)

## 개요

Docker 환경에서 OAuth 소셜 로그인을 구현할 때 주의해야 할 주요 사항들:

- **포트 매핑**: Docker 컨테이너 내부 포트와 호스트 포트 간의 매핑
- **리다이렉트 URI**: OAuth 제공자에 등록된 리다이렉트 URI와 실제 환경의 일치
- **CORS 설정**: 프론트엔드와 백엔드 간의 크로스 오리진 요청 허용
- **네트워크 통신**: Docker 네트워크 내부 서비스 간 통신

## Docker 환경별 포트 구성

### 프로덕션 환경 (docker-compose.yml)
```
프론트엔드: localhost:3000 → 컨테이너:80 (Nginx)
백엔드: localhost:8080 → 컨테이너:8080 (Spring Boot)
Redis: localhost:6379 → 컨테이너:6379
```

### 개발 환경 (docker-compose.dev.yml)
```
프론트엔드: localhost:3000 → 컨테이너:3000 (Vite Dev Server)
백엔드: localhost:8080 → 컨테이너:8080 (Spring Boot)
백엔드 디버깅: localhost:5005 → 컨테이너:5005 (Java Debug)
Redis: localhost:6379 → 컨테이너:6379
```

### 로컬 개발 환경 (Docker 없이)
```
프론트엔드: localhost:5173 (Vite Dev Server)
백엔드: localhost:8080 (Spring Boot)
Redis: localhost:6379 또는 외부 Redis
```

## OAuth 제공자 설정

### Google OAuth2 설정

Google Cloud Console에서 다음 리다이렉트 URI들을 등록해야 합니다:

**승인된 리디렉션 URI:**
```
# Docker 환경
http://localhost:3000/auth/callback/google

# 로컬 개발 환경 (Vite)
http://localhost:5173/auth/callback/google

# 프로덕션 환경 (실제 도메인으로 변경)
https://your-domain.com/auth/callback/google
```

### Kakao OAuth2 설정

Kakao Developers에서 다음 리다이렉트 URI들을 등록해야 합니다:

**Redirect URI:**
```
# Docker 환경
http://localhost:3000/auth/callback/kakao

# 로컬 개발 환경 (Vite)
http://localhost:5173/auth/callback/kakao

# 프로덕션 환경 (실제 도메인으로 변경)
https://your-domain.com/auth/callback/kakao
```

## 백엔드 설정

### application-docker.properties

Docker 환경에서 사용되는 OAuth 설정:

```properties
# Google OAuth2 설정 - Docker 환경에서는 프론트엔드가 포트 3000에서 실행됨
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}
spring.security.oauth2.client.registration.google.redirect-uri=${GOOGLE_REDIRECT_URI:http://localhost:3000/auth/callback/google}
spring.security.oauth2.client.registration.google.scope=profile,email
spring.security.oauth2.client.registration.google.authorization-grant-type=authorization_code

# Kakao OAuth2 설정 - Docker 환경에서는 프론트엔드가 포트 3000에서 실행됨
spring.security.oauth2.client.registration.kakao.client-id=${KAKAO_CLIENT_ID}
spring.security.oauth2.client.registration.kakao.client-secret=${KAKAO_CLIENT_SECRET}
spring.security.oauth2.client.registration.kakao.redirect-uri=${KAKAO_REDIRECT_URI:http://localhost:3000/auth/callback/kakao}
spring.security.oauth2.client.registration.kakao.client-authentication-method=client_secret_post
spring.security.oauth2.client.registration.kakao.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.kakao.scope=profile_nickname,account_email
```

### CORS 설정 (SecurityConfig.java)

Docker 환경을 지원하는 CORS 설정:

```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    // 허용할 오리진 설정 (Docker 환경 포함)
    configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*", 
            "http://127.0.0.1:*",
            "http://frontend:*",  // Docker 네트워크 내부 통신
            "http://solebid-frontend:*"  // Docker 컨테이너명 기반 통신
    ));
    
    // 허용할 HTTP 메서드 설정
    configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
    ));
    
    // 허용할 헤더
    configuration.setAllowedHeaders(Arrays.asList("*"));
    
    // 인증 정보 포함 허용
    configuration.setAllowCredentials(true);
    
    // 프리플라이트 요청 캐시 시간 (초)
    configuration.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    
    return source;
}
```

## 프론트엔드 설정

### Nginx 설정 (frontend/nginx.conf)

프로덕션 환경에서 API 프록시 설정:

```nginx
server {
    listen 80;
    server_name localhost;
    
    # React 앱 정적 파일 서빙
    location / {
        root /usr/share/nginx/html;
        index index.html index.htm;
        try_files $uri $uri/ /index.html;
    }
    
    # 백엔드 API 프록시 설정
    location /api/ {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        
        # CORS 헤더 설정
        add_header Access-Control-Allow-Origin $http_origin always;
        add_header Access-Control-Allow-Credentials true always;
        add_header Access-Control-Allow-Methods "GET, POST, PUT, DELETE, PATCH, OPTIONS" always;
        add_header Access-Control-Allow-Headers "DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,Authorization" always;
        
        # OPTIONS 요청 처리
        if ($request_method = 'OPTIONS') {
            add_header Access-Control-Allow-Origin $http_origin;
            add_header Access-Control-Allow-Credentials true;
            add_header Access-Control-Allow-Methods "GET, POST, PUT, DELETE, PATCH, OPTIONS";
            add_header Access-Control-Allow-Headers "DNT,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Range,Authorization";
            add_header Access-Control-Max-Age 1728000;
            add_header Content-Type 'text/plain; charset=utf-8';
            add_header Content-Length 0;
            return 204;
        }
    }
}
```

### Vite 설정 (개발 환경)

개발 환경에서 API 프록시 설정 (vite.config.ts):

```typescript
export default defineConfig({
  // ... 기타 설정
  server: {
    host: '0.0.0.0',
    port: 3000, // Docker 환경에서는 3000 포트 사용
    proxy: {
      '/api': {
        target: 'http://backend:8080', // Docker 네트워크에서 백엔드 서비스명
        changeOrigin: true,
        secure: false,
      }
    }
  }
});
```

## 환경 변수 설정

### .env 파일 (Docker 환경용)

```bash
# OAuth2 Configuration (Docker Environment)
# Google OAuth2 설정 - Docker 환경에서는 포트 3000 사용
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
GOOGLE_REDIRECT_URI=http://localhost:3000/auth/callback/google

# Kakao OAuth2 설정 - Docker 환경에서는 포트 3000 사용
KAKAO_CLIENT_ID=your-kakao-client-id
KAKAO_CLIENT_SECRET=your-kakao-client-secret
KAKAO_REDIRECT_URI=http://localhost:3000/auth/callback/kakao
KAKAO_API_HOST=https://kapi.kakao.com
KAKAO_ADMIN_KEY=your-kakao-admin-key

# 프론트엔드 URL 설정
FRONTEND_BASE_URL=http://localhost:3000
```

### 환경별 설정 비교

| 환경 | 프론트엔드 포트 | OAuth 리다이렉트 URI |
|------|----------------|---------------------|
| 로컬 개발 (Vite) | 5173 | `http://localhost:5173/auth/callback/{provider}` |
| Docker 개발 | 3000 | `http://localhost:3000/auth/callback/{provider}` |
| Docker 프로덕션 | 3000 | `http://localhost:3000/auth/callback/{provider}` |
| 실제 프로덕션 | 80/443 | `https://your-domain.com/auth/callback/{provider}` |

## CORS 설정

### 백엔드 CORS 설정 요점

1. **Origin 패턴**: `http://localhost:*` 패턴으로 모든 로컬호스트 포트 허용
2. **Docker 네트워크**: `http://frontend:*`, `http://solebid-frontend:*` 패턴 추가
3. **Credentials**: `setAllowCredentials(true)` 설정으로 쿠키/인증 정보 허용
4. **Methods**: OAuth 플로우에 필요한 모든 HTTP 메서드 허용

### 프론트엔드 프록시 설정

1. **개발 환경**: Vite 프록시로 `/api/*` 요청을 백엔드로 전달
2. **프로덕션 환경**: Nginx 프록시로 `/api/*` 요청을 백엔드로 전달
3. **Docker 네트워크**: 서비스명(`backend`)을 사용한 내부 통신

## 문제 해결

### 일반적인 OAuth 문제들

#### 1. "redirect_uri_mismatch" 오류

**원인**: OAuth 제공자에 등록된 리다이렉트 URI와 실제 요청 URI가 다름

**해결방법**:
1. OAuth 제공자 콘솔에서 정확한 리다이렉트 URI 등록 확인
2. 환경 변수 `GOOGLE_REDIRECT_URI`, `KAKAO_REDIRECT_URI` 값 확인
3. 포트 번호가 Docker 환경에 맞는지 확인 (3000 vs 5173)

#### 2. CORS 오류

**원인**: 프론트엔드와 백엔드 간 크로스 오리진 요청이 차단됨

**해결방법**:
1. 백엔드 CORS 설정에서 프론트엔드 오리진 허용 확인
2. `Access-Control-Allow-Credentials: true` 설정 확인
3. 프리플라이트 OPTIONS 요청 처리 확인

#### 3. 네트워크 연결 오류

**원인**: Docker 네트워크 내에서 서비스 간 통신 실패

**해결방법**:
1. 모든 서비스가 같은 Docker 네트워크에 있는지 확인
2. 서비스명으로 통신하는지 확인 (`backend`, `frontend`, `redis`)
3. 포트 매핑이 올바른지 확인

#### 4. 환경 변수 로드 실패

**원인**: `.env` 파일이 없거나 환경 변수가 제대로 로드되지 않음

**해결방법**:
1. `.env` 파일이 프로젝트 루트에 있는지 확인
2. Docker Compose에서 환경 변수가 올바르게 전달되는지 확인
3. 컨테이너 내부에서 환경 변수 값 확인: `docker exec -it solebid-backend env | grep GOOGLE`

### 디버깅 명령어

#### 환경 변수 확인
```bash
# 백엔드 컨테이너의 환경 변수 확인
docker exec -it solebid-backend env | grep -E "(GOOGLE|KAKAO)"

# 프론트엔드 컨테이너의 환경 변수 확인
docker exec -it solebid-frontend env
```

#### 네트워크 연결 확인
```bash
# 백엔드에서 프론트엔드로 연결 테스트
docker exec -it solebid-backend curl -I http://frontend:80

# 프론트엔드에서 백엔드로 연결 테스트
docker exec -it solebid-frontend curl -I http://backend:8080/actuator/health
```

#### 로그 확인
```bash
# 백엔드 로그 실시간 확인
docker logs -f solebid-backend

# 프론트엔드 로그 실시간 확인
docker logs -f solebid-frontend

# 모든 서비스 로그 확인
docker-compose logs -f
```

## 테스트 방법

### 1. OAuth URL 생성 테스트

```bash
# Google OAuth URL 생성 테스트
curl -X GET "http://localhost:8080/api/auth/oauth2/google/url" \
  -H "Content-Type: application/json" \
  -c cookies.txt

# Kakao OAuth URL 생성 테스트
curl -X GET "http://localhost:8080/api/auth/oauth2/kakao/url" \
  -H "Content-Type: application/json" \
  -c cookies.txt
```

### 2. 프론트엔드 OAuth 플로우 테스트

1. 브라우저에서 `http://localhost:3000` 접속
2. 회원가입 또는 로그인 페이지로 이동
3. "구글로 시작하기" 또는 "카카오로 시작하기" 버튼 클릭
4. OAuth 제공자 페이지로 리다이렉트 확인
5. 인증 완료 후 콜백 URL로 리다이렉트 확인
6. 로그인 성공 확인

### 3. 전체 시스템 통합 테스트

```bash
# 모든 서비스 시작
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d

# 서비스 상태 확인
docker-compose ps

# 헬스체크 확인
curl http://localhost:8080/actuator/health
curl http://localhost:3000

# OAuth 플로우 테스트 (브라우저에서)
# 1. http://localhost:3000 접속
# 2. 소셜 로그인 테스트
# 3. 로그인 성공 후 기능 테스트
```

### 4. 자동화된 테스트 스크립트

```bash
#!/bin/bash
# oauth-test.sh

echo "=== OAuth Docker 환경 테스트 시작 ==="

# 1. 서비스 상태 확인
echo "1. 서비스 상태 확인..."
docker-compose ps

# 2. 백엔드 헬스체크
echo "2. 백엔드 헬스체크..."
curl -f http://localhost:8080/actuator/health || exit 1

# 3. 프론트엔드 접근 확인
echo "3. 프론트엔드 접근 확인..."
curl -f http://localhost:3000 || exit 1

# 4. OAuth URL 생성 테스트
echo "4. Google OAuth URL 생성 테스트..."
GOOGLE_RESPONSE=$(curl -s http://localhost:8080/api/auth/oauth2/google/url)
echo "Google OAuth Response: $GOOGLE_RESPONSE"

echo "5. Kakao OAuth URL 생성 테스트..."
KAKAO_RESPONSE=$(curl -s http://localhost:8080/api/auth/oauth2/kakao/url)
echo "Kakao OAuth Response: $KAKAO_RESPONSE"

# 6. 환경 변수 확인
echo "6. OAuth 환경 변수 확인..."
docker exec solebid-backend env | grep -E "(GOOGLE|KAKAO)_CLIENT_ID"

echo "=== OAuth Docker 환경 테스트 완료 ==="
```

## 환경별 체크리스트

### Docker 개발 환경 설정 체크리스트

- [ ] `.env` 파일에 올바른 OAuth 클라이언트 ID/Secret 설정
- [ ] OAuth 제공자에 `http://localhost:3000/auth/callback/{provider}` URI 등록
- [ ] `docker-compose.dev.yml` 파일의 환경 변수 확인
- [ ] CORS 설정에 `http://localhost:3000` 포함 확인
- [ ] 모든 서비스가 `solebid-network`에 연결되어 있는지 확인

### Docker 프로덕션 환경 설정 체크리스트

- [ ] `.env.prod` 파일에 프로덕션용 OAuth 설정
- [ ] OAuth 제공자에 실제 도메인 URI 등록
- [ ] SSL/TLS 인증서 설정 (HTTPS)
- [ ] 보안 강화된 환경 변수 설정
- [ ] 프로덕션 도메인에 맞는 CORS 설정

### 로컬 개발 환경 설정 체크리스트

- [ ] OAuth 제공자에 `http://localhost:5173/auth/callback/{provider}` URI 등록
- [ ] `application.properties`에 올바른 리다이렉트 URI 설정
- [ ] Vite 개발 서버 프록시 설정 확인
- [ ] 로컬 Redis 또는 외부 Redis 연결 확인

이 가이드를 따라 설정하면 Docker 환경에서 OAuth 소셜 로그인이 정상적으로 작동해야 합니다. 문제가 발생하면 로그를 확인하고 위의 문제 해결 섹션을 참조하세요.