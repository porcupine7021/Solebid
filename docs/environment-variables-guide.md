# 환경 변수 설정 가이드

## 개요

이 문서는 Solebid 프로젝트의 Docker 환경에서 사용되는 환경 변수 설정에 대한 가이드입니다. 보안을 위해 민감한 정보는 환경 변수로 분리하여 관리합니다.

## 환경 변수 파일 설정

### 1. .env 파일 생성

프로젝트 루트 디렉토리에서 `.env.example` 파일을 복사하여 `.env` 파일을 생성합니다:

```bash
cp .env.example .env
```

### 2. 환경 변수 값 설정

`.env` 파일을 열어 실제 값으로 수정합니다:

```bash
# 예시: 데이터베이스 설정
DB_URL=jdbc:mysql://your-actual-rds-endpoint:3306/solebid?useSSL=false&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true
DB_USERNAME=your-actual-username
DB_PASSWORD=your-actual-password
```

## 환경 변수 카테고리

### 필수 환경 변수

다음 환경 변수들은 반드시 설정해야 합니다:

#### 데이터베이스 설정
- `DB_URL`: AWS RDS MySQL 연결 URL
- `DB_USERNAME`: 데이터베이스 사용자명
- `DB_PASSWORD`: 데이터베이스 비밀번호

#### JWT 설정
- `JWT_SECRET`: JWT 토큰 서명용 비밀 키 (최소 64바이트)

#### AWS 설정
- `AWS_ACCESS_KEY`: AWS 액세스 키
- `AWS_SECRET_KEY`: AWS 시크릿 키
- `S3_BUCKET`: S3 버킷 이름

#### OAuth2 설정
- `GOOGLE_CLIENT_ID`: Google OAuth2 클라이언트 ID
- `GOOGLE_CLIENT_SECRET`: Google OAuth2 클라이언트 시크릿
- `KAKAO_CLIENT_ID`: Kakao OAuth2 클라이언트 ID
- `KAKAO_CLIENT_SECRET`: Kakao OAuth2 클라이언트 시크릿
- `KAKAO_ADMIN_KEY`: Kakao API 관리자 키

#### PortOne 설정
- `PORTONE_API_KEY`: PortOne API 키
- `PORTONE_API_SECRET`: PortOne API 시크릿

### 선택적 환경 변수

다음 환경 변수들은 기본값이 있어 선택적으로 설정할 수 있습니다:

#### Redis 설정
- `REDIS_HOST`: Redis 호스트 (기본값: redis)
- `REDIS_PORT`: Redis 포트 (기본값: 6379)
- `REDIS_PASSWORD`: Redis 비밀번호 (기본값: 없음)

#### 애플리케이션 설정
- `SERVER_PORT`: 백엔드 서버 포트 (기본값: 8080)
- `FRONTEND_BASE_URL`: 프론트엔드 URL (기본값: http://localhost:3000)
- `AWS_REGION`: AWS 리전 (기본값: ap-southeast-2)

#### 로깅 설정
- `SECURITY_LOG_LEVEL`: 보안 로그 레벨 (기본값: info)
- `APP_LOG_LEVEL`: 애플리케이션 로그 레벨 (기본값: info)
- `JPA_SHOW_SQL`: JPA SQL 출력 여부 (기본값: false)
- `JPA_FORMAT_SQL`: JPA SQL 포맷팅 여부 (기본값: false)

## 보안 고려사항

### 1. 파일 보안

- `.env` 파일은 `.gitignore`에 포함되어 버전 관리에서 제외됩니다
- `.env.example` 파일만 버전 관리에 포함되며, 실제 값은 포함하지 않습니다
- 프로덕션 환경에서는 더 안전한 시크릿 관리 도구 사용을 권장합니다

### 2. 키 관리

- JWT 시크릿은 최소 64바이트 이상의 강력한 키를 사용하세요
- AWS 키는 최소 권한 원칙에 따라 필요한 권한만 부여하세요
- OAuth2 키는 해당 플랫폼의 보안 가이드라인을 따르세요

### 3. 네트워크 보안

- 프로덕션 환경에서는 HTTPS를 사용하세요
- 데이터베이스 연결은 SSL을 활성화하는 것을 권장합니다
- Redis에 비밀번호를 설정하는 것을 권장합니다

## Docker Compose 사용법

### 개발 환경

```bash
# 개발 환경 실행
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up

# 백그라운드 실행
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
```

### 프로덕션 환경

```bash
# 프로덕션 환경 실행
docker-compose up

# 백그라운드 실행
docker-compose up -d
```

## 환경 변수 검증

애플리케이션 시작 시 다음과 같이 환경 변수가 올바르게 설정되었는지 확인할 수 있습니다:

### 1. 백엔드 헬스체크

```bash
curl http://localhost:8080/actuator/health
```

### 2. 로그 확인

```bash
# 백엔드 로그 확인
docker-compose logs backend

# 모든 서비스 로그 확인
docker-compose logs
```

### 3. 환경 변수 확인

```bash
# 컨테이너 내부 환경 변수 확인 (개발용)
docker-compose exec backend env | grep -E "(DB_|JWT_|AWS_|GOOGLE_|KAKAO_|PORTONE_)"
```

## 문제 해결

### 일반적인 문제들

1. **환경 변수가 인식되지 않는 경우**
   - `.env` 파일이 프로젝트 루트에 있는지 확인
   - 환경 변수 이름에 오타가 없는지 확인
   - Docker Compose를 재시작해보세요

2. **데이터베이스 연결 실패**
   - `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` 값 확인
   - AWS RDS 보안 그룹 설정 확인
   - 네트워크 연결 상태 확인

3. **OAuth2 인증 실패**
   - 클라이언트 ID와 시크릿 값 확인
   - 리다이렉트 URI 설정 확인
   - 각 플랫폼의 OAuth2 설정 확인

## 추가 리소스

- [Docker Compose 환경 변수 문서](https://docs.docker.com/compose/environment-variables/)
- [Spring Boot 외부 설정 문서](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [AWS 보안 모범 사례](https://aws.amazon.com/security/security-learning/)