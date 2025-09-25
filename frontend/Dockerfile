# Frontend Dockerfile - React 애플리케이션 컨테이너화
# 멀티 스테이지 빌드를 사용하여 개발환경과 프로덕션 환경을 분리

# ================================
# 개발 스테이지 (Development Stage)
# ================================
FROM node:20-alpine AS development

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json 복사 (의존성 캐싱 최적화)
COPY package*.json ./

# 의존성 설치
RUN npm ci

# 소스 코드 복사
COPY . .

# 개발 서버 포트 노출
EXPOSE 3000

# 개발 서버 실행
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0", "--port", "3000"]

# ================================
# 빌드 스테이지 (Build Stage)
# ================================
FROM node:20-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json 복사
COPY package*.json ./

# 의존성 설치
RUN npm ci

# 소스 코드 복사
COPY . .

# 프로덕션 빌드
RUN npm run build

# ================================
# 프로덕션 스테이지 (Production Stage)
# ================================
FROM nginx:alpine AS production

# 빌드된 파일을 Nginx 웹 루트로 복사
COPY --from=builder /app/dist /usr/share/nginx/html

# Nginx 설정 파일 복사 (SPA 라우팅 및 API 프록시 설정)
COPY nginx.conf /etc/nginx/nginx.conf

# 포트 80 노출 (Nginx 기본 포트)
EXPOSE 80

# 헬스체크 설정
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:80/ || exit 1

# Nginx 시작 명령어
CMD ["nginx", "-g", "daemon off;"]