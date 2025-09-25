# Solebid Docker 통합 테스트 가이드

## 개요

이 문서는 Solebid 프로젝트의 Docker 환경에서 통합 테스트를 실행하는 방법을 설명합니다. 통합 테스트는 모든 서비스(백엔드, 프론트엔드, Redis)가 올바르게 작동하고 서로 통신할 수 있는지 검증합니다.

## 테스트 범위

### 1. 서비스 시작 확인
- 모든 Docker 컨테이너가 정상적으로 시작되는지 확인
- 컨테이너 상태 및 헬스체크 검증
- 서비스 간 의존성 관리 확인

### 2. API 엔드포인트 접근성 테스트
- 백엔드 API 엔드포인트 응답 확인
- Spring Boot Actuator 헬스체크 검증
- HTTP 상태 코드 및 응답 시간 측정

### 3. AWS RDS 연결 테스트
- 백엔드에서 AWS RDS MySQL 데이터베이스 연결 확인
- 데이터베이스 연결 상태 모니터링
- 연결 오류 및 타임아웃 처리 검증

### 4. Redis 연결 테스트
- Redis 컨테이너 연결 상태 확인
- 데이터 저장 및 조회 기능 테스트
- 백엔드-Redis 간 통신 검증

### 5. 서비스 간 통신 테스트
- 프론트엔드-백엔드 프록시 통신 확인
- 네트워크 설정 및 포트 매핑 검증
- CORS 설정 및 API 호출 테스트

## 사전 요구사항

### 필수 소프트웨어
- Docker Desktop 또는 Docker Engine
- Docker Compose (v2.0 이상 권장)
- curl (HTTP 요청 테스트용)

### 환경 설정
1. `.env` 파일이 프로젝트 루트에 존재해야 함
2. 필수 환경 변수가 올바르게 설정되어야 함:
   - `DB_URL`: AWS RDS 연결 URL
   - `DB_USERNAME`: 데이터베이스 사용자명
   - `DB_PASSWORD`: 데이터베이스 비밀번호
   - `JWT_SECRET`: JWT 토큰 서명 키

## 테스트 실행 방법

### 권장 방법 (크로스 플랫폼)

```bash
# Linux/macOS 환경
./scripts/run-integration-tests.sh

# Windows 환경
scripts\run-integration-tests.bat
```

### 사용 가능한 옵션

```bash
# 기본 통합 테스트 실행
./scripts/run-integration-tests.sh

# 빠른 검증 테스트만 실행
./scripts/run-integration-tests.sh --quick

# 테스트 후 서비스 자동 중지
./scripts/run-integration-tests.sh --stop-services

# 로그만 수집 (테스트 실행 안 함)
./scripts/run-integration-tests.sh --logs-only

# 도움말 보기
./scripts/run-integration-tests.sh --help
```

### 고급 테스트 스크립트 (상세 제어)

더 세밀한 제어가 필요한 경우 고급 테스트 스크립트를 사용할 수 있습니다:

```bash
# Linux/macOS 환경
./scripts/integration-test.sh

# Windows 환경
scripts\integration-test.bat
```

## 테스트 결과 해석

### 성공적인 테스트 출력 예시

```
==========================================
     Solebid Docker 통합 테스트 시작
==========================================

[INFO] Docker Compose 설치 확인 중...
[SUCCESS] Docker Compose가 설치되어 있습니다
[INFO] 환경 변수 파일 확인 중...
[SUCCESS] 환경 변수 파일이 올바르게 설정되어 있습니다
[INFO] Docker 서비스 시작 중...
[SUCCESS] Docker 서비스가 시작되었습니다

[INFO] 통합 테스트 실행 중...

[SUCCESS] ✓ 컨테이너 상태 확인
[SUCCESS] ✓ Redis 연결 테스트
[SUCCESS] ✓ Redis 데이터 저장/조회 테스트
[SUCCESS] ✓ 백엔드 헬스체크 엔드포인트
[SUCCESS] ✓ 프론트엔드 메인 페이지
[SUCCESS] ✓ 데이터베이스 연결 테스트
[SUCCESS] ✓ 프론트엔드-백엔드 프록시 통신
[SUCCESS] ✓ 백엔드-Redis 통신
[SUCCESS] ✓ 리소스 사용량 확인

==========================================
           테스트 결과 요약
==========================================

[SUCCESS] 통과한 테스트: 9
[SUCCESS] 모든 테스트가 통과했습니다! 🎉

총 테스트 수: 9
성공률: 100%

[SUCCESS] 모든 통합 테스트가 성공적으로 완료되었습니다! ✅
```

### 실패한 테스트 출력 예시

```
[ERROR] ✗ 백엔드 헬스체크 엔드포인트
[ERROR] ✗ 데이터베이스 연결 테스트

==========================================
           테스트 결과 요약
==========================================

[SUCCESS] 통과한 테스트: 7
[ERROR] 실패한 테스트: 2

[ERROR] 실패한 테스트 목록:
  - 백엔드 헬스체크 엔드포인트
  - 데이터베이스 연결 테스트

총 테스트 수: 9
성공률: 77%

[ERROR] 일부 테스트가 실패했습니다. 로그를 확인하세요. ❌
```

## 로그 수집 및 분석

### 자동 로그 수집
테스트 실행 시 모든 서비스의 로그가 자동으로 수집됩니다:

```
test-logs-20241225-143022/
├── solebid-backend.log      # 백엔드 서비스 로그
├── solebid-frontend.log     # 프론트엔드 서비스 로그
├── solebid-redis.log        # Redis 서비스 로그
└── docker-compose.log       # Docker Compose 전체 로그
```

### 수동 로그 확인

```bash
# 개별 컨테이너 로그 확인
docker logs solebid-backend
docker logs solebid-frontend
docker logs solebid-redis

# 실시간 로그 모니터링
docker logs -f solebid-backend

# Docker Compose 전체 로그
docker-compose logs
```

## 문제 해결

### 일반적인 문제들

#### 1. 환경 변수 파일 누락
**증상**: `.env 파일이 없습니다` 경고 메시지
**해결**: `.env.example`을 복사하여 `.env` 파일 생성 후 실제 값으로 수정

```bash
cp .env.example .env
# .env 파일을 편집하여 실제 값 입력
```

#### 2. 데이터베이스 연결 실패
**증상**: `데이터베이스 연결 테스트` 실패
**해결**: 
- AWS RDS 보안 그룹 설정 확인
- 데이터베이스 자격 증명 확인
- 네트워크 연결 상태 확인

#### 3. 포트 충돌
**증상**: 서비스 시작 실패, 포트 바인딩 오류
**해결**:
- 사용 중인 포트 확인: `netstat -tulpn | grep :8080`
- 충돌하는 프로세스 종료 또는 포트 변경

#### 4. 메모리 부족
**증상**: 컨테이너가 시작되지 않거나 OOM 오류
**해결**:
- Docker Desktop 메모리 할당량 증가
- 불필요한 컨테이너 정리: `docker system prune`

#### 5. Redis 연결 실패
**증상**: `Redis 연결 테스트` 실패
**해결**:
- Redis 컨테이너 상태 확인: `docker ps | grep redis`
- Redis 로그 확인: `docker logs solebid-redis`
- 네트워크 설정 확인

### 디버깅 명령어

```bash
# 컨테이너 상태 확인
docker ps -a

# 네트워크 상태 확인
docker network ls
docker network inspect solebid-network

# 볼륨 상태 확인
docker volume ls
docker volume inspect solebid-redis-data

# 이미지 상태 확인
docker images

# 시스템 리소스 사용량 확인
docker stats

# 컨테이너 내부 접속
docker exec -it solebid-backend bash
docker exec -it solebid-redis redis-cli
```

## 성능 모니터링

### 리소스 사용량 확인
테스트 중 각 서비스의 CPU 및 메모리 사용량이 자동으로 모니터링됩니다:

```
solebid-backend 리소스 사용량: 15.23% 512MiB / 2GiB
solebid-frontend 리소스 사용량: 0.12% 45MiB / 2GiB
solebid-redis 리소스 사용량: 0.45% 32MiB / 512MiB
```

### 응답 시간 측정
각 HTTP 엔드포인트의 응답 시간이 측정되어 성능 이슈를 조기에 발견할 수 있습니다.

## 지속적 통합 (CI/CD)

### GitHub Actions 예시

```yaml
name: Docker Integration Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  integration-test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Create .env file
      run: |
        cp .env.example .env
        # 환경 변수 설정 (GitHub Secrets 사용)
        
    - name: Run Integration Tests
      run: |
        chmod +x scripts/integration-test.sh
        ./scripts/integration-test.sh --stop-services
        
    - name: Upload test logs
      if: failure()
      uses: actions/upload-artifact@v3
      with:
        name: test-logs
        path: test-logs-*/
```

## 보안 고려사항

### 환경 변수 보안
- `.env` 파일을 절대 버전 관리에 포함하지 마세요
- 프로덕션 환경에서는 Docker Secrets 또는 외부 키 관리 서비스 사용
- 테스트 환경에서도 실제 프로덕션 자격 증명 사용 금지

### 네트워크 보안
- 불필요한 포트 노출 최소화
- 프로덕션 환경에서는 내부 네트워크만 사용
- 방화벽 및 보안 그룹 설정 검토

## 추가 리소스

- [Docker Compose 공식 문서](https://docs.docker.com/compose/)
- [Spring Boot Actuator 가이드](https://spring.io/guides/gs/actuator-service/)
- [Redis 공식 문서](https://redis.io/documentation)
- [AWS RDS 연결 가이드](https://docs.aws.amazon.com/rds/latest/userguide/CHAP_GettingStarted.html)

## 문의 및 지원

테스트 관련 문제가 발생하면 다음 정보와 함께 문의하세요:

1. 운영 체제 및 버전
2. Docker 및 Docker Compose 버전
3. 오류 메시지 및 로그 파일
4. 테스트 실행 명령어
5. 환경 변수 설정 (민감한 정보 제외)

---

**참고**: 이 가이드는 Solebid 프로젝트의 Docker 환경 통합 테스트를 위한 것입니다. 프로덕션 환경에서는 추가적인 보안 및 성능 고려사항이 필요할 수 있습니다.