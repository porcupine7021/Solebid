# 멀티 스테이지 빌드를 사용하여 Spring Boot 애플리케이션을 컨테이너화합니다.
# 첫 번째 스테이지에서는 Gradle을 사용하여 JAR 파일을 빌드하고,
# 두 번째 스테이지에서는 최적화된 런타임 환경에서 애플리케이션을 실행합니다.

# ================================
# 빌드 스테이지 (Build Stage)
# ================================
FROM eclipse-temurin:17-jdk-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper와 설정 파일들을 먼저 복사 (의존성 캐싱 최적화)
COPY gradlew .
COPY gradle gradle/
COPY build.gradle .
COPY settings.gradle .

# Gradle Wrapper에 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 (소스 코드 변경 시 캐시 활용 가능)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src/

# 애플리케이션 빌드 (테스트 제외하여 빌드 시간 단축)
RUN ./gradlew bootJar --no-daemon -x test

# ================================
# 런타임 스테이지 (Runtime Stage)
# ================================
FROM eclipse-temurin:17-jre-alpine AS runtime

# 헬스체크를 위한 curl 설치 및 애플리케이션 실행을 위한 사용자 생성 (Alpine Linux 사용)
RUN apk add --no-cache curl && \
    addgroup -g 1000 spring && \
    adduser -D -s /bin/sh -u 1000 -G spring spring

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 파일 소유권을 spring 사용자로 변경
RUN chown spring:spring app.jar

# spring 사용자로 전환
USER spring

# 애플리케이션이 사용할 포트 노출
EXPOSE 8080

# JVM 최적화 옵션과 함께 애플리케이션 실행
# -Xms512m: 초기 힙 메모리 크기
# -Xmx1024m: 최대 힙 메모리 크기
# -XX:+UseG1GC: G1 가비지 컬렉터 사용 (컨테이너 환경에 최적화)
# -XX:MaxGCPauseMillis=200: GC 일시정지 시간 최대 200ms로 제한
# -Djava.security.egd=file:/dev/./urandom: 보안 난수 생성 최적화
ENTRYPOINT ["java", \
    "-Xms512m", \
    "-Xmx1024m", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "app.jar"]

# 헬스체크 설정 (Spring Boot Actuator 사용)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1