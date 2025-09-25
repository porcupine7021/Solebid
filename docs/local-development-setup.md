# 로컬 개발 환경 설정 가이드

## 🚀 빠른 시작

### 1. 환경 변수 설정

```bash
# .env 파일 생성
cp .env.example .env
```

### 2. .env 파일 수정

`.env` 파일을 열고 다음 값들을 실제 값으로 변경하세요:

```bash
# 데이터베이스 설정
DB_PASSWORD=your-actual-db-password

# AWS 자격 증명
AWS_ACCESS_KEY=your-actual-aws-access-key
AWS_SECRET_KEY=your-actual-aws-secret-key

# JWT 비밀키 (최소 64바이트)
JWT_SECRET=your-actual-super-secure-jwt-secret-key

# 이메일 설정
GOOGLE_MAIL_REFRESH_TOKEN=your-actual-refresh-token

# OAuth2 설정
GOOGLE_CLIENT_ID=your-actual-google-client-id
GOOGLE_CLIENT_SECRET=your-actual-google-client-secret
KAKAO_CLIENT_ID=your-actual-kakao-client-id
KAKAO_CLIENT_SECRET=your-actual-kakao-client-secret
KAKAO_ADMIN_KEY=your-actual-kakao-admin-key

# PortOne 설정
PORTONE_API_KEY=your-actual-portone-api-key
PORTONE_API_SECRET=your-actual-portone-api-secret
```

### 3. 애플리케이션 실행

```bash
# 백엔드 실행 (IDE에서 또는 Gradle로)
./gradlew bootRun

# 또는 Docker로 실행
docker-compose up -d
```

## 📋 Properties 파일 설명

### application.properties
- **용도**: 모든 환경에서 사용하는 기본 설정
- **특징**: 환경 변수 사용 (`${ENV_VAR:default_value}`)
- **Git 상태**: ✅ 포함 (민감한 정보 없음)

### application-docker.properties
- **용도**: Docker 환경 전용 설정
- **활성화**: `SPRING_PROFILES_ACTIVE=docker`

### application-prod.properties
- **용도**: 프로덕션 환경 전용 설정
- **활성화**: `SPRING_PROFILES_ACTIVE=prod`

### application-redis.properties
- **용도**: Redis 관련 설정
- **활성화**: `SPRING_PROFILES_ACTIVE=redis`

## 🔒 보안 주의사항

1. **절대 Git에 커밋하지 말 것**:
   - `.env` 파일 (실제 환경 변수 값)
   - 실제 API 키, 비밀번호, 토큰

2. **환경 변수 사용**:
   - 모든 민감한 정보는 환경 변수로 관리
   - 기본값은 개발용 더미 값 사용

3. **팀 협업**:
   - 새 환경 변수 추가 시 `.env.example` 업데이트
   - 팀원들에게 새 설정 변수 공지

## 🛠️ 문제 해결

### "환경 변수를 찾을 수 없음" 오류
```bash
# .env 파일이 프로젝트 루트에 있는지 확인
ls -la .env

# .env 파일 내용 확인
cat .env
```

### 데이터베이스 연결 오류
- `.env` 파일의 `DB_PASSWORD` 값 확인
- RDS 보안 그룹 설정 확인

### AWS 관련 오류
- `.env` 파일의 AWS 자격 증명 확인
- IAM 권한 설정 확인

---

**💡 팁**: 모든 설정이 환경 변수로 관리되므로, 한 번 `.env` 파일을 설정하면 모든 환경에서 동일하게 작동합니다!