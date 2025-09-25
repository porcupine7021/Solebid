# Solebid Docker 완전 가이드

## 📋 목차

- [개요](#개요)
- [빠른 시작](#빠른-시작)
- [개발환경 설정](#개발환경-설정)
- [프로덕션 배포](#프로덕션-배포)
- [Docker 구성 요소](#docker-구성-요소)
- [일반적인 명령어](#일반적인-명령어)
- [문제 해결](#문제-해결)

## 🎯 개요

Solebid 프로젝트는 Docker Compose를 사용하여 다음 서비스들을 컨테이너화합니다:

- **Backend**: Spring Boot 애플리케이션 (포트 8080)
- **Frontend**: React 애플리케이션 + Nginx (포트 3000/80)
- **Redis**: 캐시 서버 (포트 6379)
- **외부 서비스**: AWS RDS MySQL (기존 연결 유지)

### 아키텍처 다이어그램

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │    Backend      │    │     Redis       │
│   (React +      │───▶│  (Spring Boot)  │───▶│   (Cache)       │
│    Nginx)       │    │                 │    │                 │
│   Port: 3000    │    │   Port: 8080    │    │   Port: 6379    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │   AWS RDS       │
                       │   (MySQL)       │
                       │   External      │
                       └─────────────────┘
```

## 🚀 빠른 시작

### 1. 초기 설정

#### 자동 설정 (권장)

**Windows:**
```cmd
scripts\docker-setup.bat
```

**Linux/macOS:**
```bash
./scripts/docker-setup.sh
```

#### 스마트 개발환경 시작 (권장)
포트 충돌 검사와 자동 해결 기능이 포함된 스마트 시작 스크립트를 사용하세요:

**Windows:**
```cmd
scripts\dev-start-smart.bat
```

**Linux/macOS:**
```bash
./scripts/dev-start-smart.sh
```

#### 수동 설정

1. **환경 변수 파일 생성**
   ```bash
   cp .env.example .env
   ```

2. **필수 환경 변수 설정** (`.env` 파일 편집)
   ```env
   DB_URL=jdbc:mysql://your-rds-endpoint:3306/solebid
   DB_USERNAME=your-db-username
   DB_PASSWORD=your-db-password
   JWT_SECRET=your-super-secure-and-long-secret-key-minimum-64-bytes
   ```

### 2. 서비스 시작

#### 개발환경
```bash
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
```

#### 프로덕션
```bash
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

### 3. 접속 확인
- **프론트엔드**: http://localhost:3000
- **백엔드 API**: http://localhost:8080
- **백엔드 헬스체크**: http://localhost:8080/actuator/health

## 📜 사용 가능한 스크립트

프로젝트에는 Docker 환경 관리를 위한 다양한 스크립트가 제공됩니다:

### 핵심 스크립트
- `scripts/docker-setup.*` - 초기 Docker 환경 설정
- `scripts/dev-start-smart.*` - 스마트 개발환경 시작 (포트 충돌 검사 포함)
- `scripts/docker-cleanup.*` - Docker 환경 정리

### 검증 및 진단 스크립트

#### 🔍 `scripts/validate-setup.*` - Docker 환경 설정 통합 검증
**목적**: Docker Compose 설정 파일과 환경 변수의 유효성을 종합적으로 검증
**사용 시점**: Docker 환경 구성 후, 서비스 시작 전
**특징**:
- Docker Compose 파일 구문 검증
- 환경 변수 설정 확인
- 네트워크 및 볼륨 설정 검증
- 서비스 의존성 관계 확인
- 포트 매핑 및 중복 검사

```bash
# 전체 설정 검증
./scripts/validate-setup.sh

# 환경 변수만 검증
./scripts/validate-setup.sh --env-only

# 설정 파일만 검증
./scripts/validate-setup.sh --config-only
```

#### 🏥 `scripts/docker-health-check.*` - 빠른 로컬 진단
**목적**: 실행 중인 Docker 환경의 상태를 빠르게 진단
**사용 시점**: 개발 중 문제 발생 시, 일상적인 상태 확인
**특징**:
- 컨테이너 실행 상태 확인
- 서비스 헬스체크 검증
- 네트워크 연결성 테스트
- 리소스 사용량 모니터링
- 로그 오류 분석

```bash
# 빠른 헬스체크 실행
./scripts/docker-health-check.sh
```

#### 🧪 `scripts/docker-system-test.*` - CI/CD용 종합 테스트
**목적**: 전체 시스템의 완전한 통합 테스트 수행
**사용 시점**: CI/CD 파이프라인, 배포 전 최종 검증
**특징**:
- 백엔드-프론트엔드 간 API 통신 검증
- 사용자 시나리오 기반 통합 테스트
- 전체 애플리케이션 워크플로우 테스트
- 성능 및 안정성 검증
- 상세한 테스트 보고서 생성

```bash
# 전체 시스템 테스트 실행
./scripts/docker-system-test.sh

# 테스트 후 서비스 중지
./scripts/docker-system-test.sh --stop-services

# 테스트 보고서만 생성
./scripts/docker-system-test.sh --report-only
```

### 기타 유틸리티 스크립트
- `scripts/check-ports.*` - 포트 사용 상태 확인
- `scripts/validate-env.*` - 환경 변수 검증
- `scripts/test-oauth-docker.*` - OAuth Docker 설정 테스트

### 배포 관련
- `scripts/deploy-prod.*` - 프로덕션 배포

### 스크립트 사용 가이드라인

| 상황 | 권장 스크립트 | 설명 |
|------|---------------|------|
| 초기 환경 구성 후 | `validate-setup.*` | 설정이 올바른지 확인 |
| 개발 중 문제 발생 | `docker-health-check.*` | 빠른 문제 진단 |
| 배포 전 검증 | `docker-system-test.*` | 완전한 시스템 테스트 |
| CI/CD 파이프라인 | `docker-system-test.*` | 자동화된 통합 테스트 |

> **참고**: 모든 스크립트는 Windows(.bat)와 Linux/macOS(.sh) 버전을 제공합니다.

## 🛠️ 개발환경 설정

### 사전 요구사항
- Docker Desktop (Windows/Mac) 또는 Docker Engine (Linux)
- Docker Compose v3.8 이상
- Git

### 개발환경 특화 기능
- **실시간 코드 변경 반영**: 볼륨 마운트를 통한 Hot Reload
- **디버깅 포트**: 백엔드 디버깅을 위한 포트 5005 노출
- **개발 도구**: 개발 편의를 위한 추가 설정

### 개발환경 환경 변수
```env
# 개발환경 전용 설정
SPRING_PROFILES_ACTIVE=docker,dev
REACT_APP_API_URL=http://localhost:8080
```

### 개발 워크플로우
1. 코드 변경
2. 자동 빌드 및 재시작 (Hot Reload)
3. 브라우저에서 즉시 확인

## 🚀 프로덕션 배포

### 시스템 요구사항
- **CPU**: 4코어 이상
- **메모리**: 8GB 이상 (권장: 16GB)
- **디스크**: 50GB 이상 여유 공간
- **OS**: Ubuntu 20.04 LTS 이상 / CentOS 8 이상

### 프로덕션 환경 변수
```env
# 프로덕션 전용 설정
SPRING_PROFILES_ACTIVE=docker,prod
NODE_ENV=production
```

### 보안 설정
- 환경 변수를 통한 민감 정보 관리
- 컨테이너 네트워크 격리
- 불필요한 포트 노출 최소화
- SSL/TLS 설정 (Nginx)

### 모니터링 및 로깅
- Docker 로그 수집
- 헬스체크 모니터링
- 리소스 사용량 추적

## 🔧 Docker 구성 요소

### Docker Compose 파일
- `docker-compose.yml`: 기본 서비스 정의
- `docker-compose.dev.yml`: 개발환경 오버라이드
- `docker-compose.prod.yml`: 프로덕션 오버라이드

### Dockerfile
- `backend/demo/Dockerfile`: Spring Boot 애플리케이션
- `frontend/Dockerfile`: React + Nginx

### 설정 파일
- `frontend/nginx.conf`: Nginx 웹 서버 설정
- `redis/redis.conf`: Redis 서버 설정

## 📝 일반적인 명령어

### 서비스 관리
```bash
# 서비스 시작
docker-compose up -d

# 서비스 중지
docker-compose down

# 서비스 재시작
docker-compose restart

# 로그 확인
docker-compose logs -f [service-name]

# 서비스 상태 확인
docker-compose ps
```

### 개발 도구
```bash
# 컨테이너 내부 접속
docker exec -it solebid-backend bash
docker exec -it solebid-frontend sh
docker exec -it solebid-redis redis-cli

# 이미지 재빌드
docker-compose build --no-cache

# 볼륨 정리
docker-compose down -v
```

## 🔍 문제 해결

### 포트 충돌 해결 (가장 흔한 문제)

포트 충돌은 Docker 개발환경에서 가장 흔히 발생하는 문제입니다. 스마트 시작 스크립트를 사용하면 자동으로 해결됩니다:

```bash
# 스마트 시작 (포트 충돌 자동 해결)
./scripts/dev-start-smart.sh  # Linux/Mac
scripts\dev-start-smart.bat   # Windows
```

### 종합 문제 해결

모든 Docker 관련 문제 해결 방법은 **[Docker 트러블슈팅 가이드](docs/docker-troubleshooting-guide.md)**에서 확인할 수 있습니다:

- **포트 충돌 및 포트 관리** - 자동 진단 및 해결 방법
- **컨테이너 시작 실패** - 환경 변수, 의존성, 헬스체크 문제
- **네트워크 연결 문제** - 서비스 간 통신 문제
- **볼륨 및 데이터 문제** - 데이터 손실, 권한 오류
- **성능 및 리소스 문제** - 메모리, CPU 최적화
- **빌드 관련 문제** - 의존성, 캐시 문제

### 빠른 진단 도구

```bash
# 포트 상태 확인
./scripts/check-ports.sh

# 환경 변수 검증
./scripts/validate-env.sh

# 헬스체크 수행
./scripts/docker-health-check.sh

# 전체 시스템 테스트
./scripts/docker-system-test.sh
```

## 📚 추가 자료

### 핵심 가이드
- [Docker 트러블슈팅 가이드](docs/docker-troubleshooting-guide.md) - 포트 관리 및 문제 해결
- [환경 변수 가이드](docs/environment-variables-guide.md)
- [Docker 헬스체크 가이드](docs/docker-healthcheck-guide.md)

### 전문 가이드
- [OAuth Docker 설정 가이드](docs/oauth-docker-configuration-guide.md)
- [통합 테스트 가이드](docs/integration-testing-guide.md)
- [로컬 개발 설정 가이드](docs/local-development-setup.md)