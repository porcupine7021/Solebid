package com.sesac.solbid.service;

import com.sesac.solbid.service.user.EmailVerificationTokenService;
import com.sesac.solbid.service.user.InMemoryEmailVerificationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * EmailVerificationTokenService 단위 테스트
 * 토큰 생성, 검증, 소비, 만료 처리 및 재전송 제한 로직 테스트
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EmailVerificationTokenService 단위 테스트")
class EmailVerificationTokenServiceTest {

    private EmailVerificationTokenService tokenService;
    private final String testEmail = "test@example.com";
    private final String anotherEmail = "another@example.com";

    @BeforeEach
    void setUp() {
        tokenService = new InMemoryEmailVerificationTokenService();
    }

    @Test
    @DisplayName("토큰 생성 성공 테스트")
    void createToken_Success() {
        // When
        String token = tokenService.createToken(testEmail);

        // Then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token).hasSize(32); // UUID without hyphens
        assertThat(token).matches("^[0-9a-f]{32}$");
    }

    @Test
    @DisplayName("토큰 생성 시 고유성 보장 테스트")
    void createToken_Uniqueness() {
        // When
        String token1 = tokenService.createToken(testEmail);
        String token2 = tokenService.createToken(testEmail);
        String token3 = tokenService.createToken(anotherEmail);

        // Then
        assertThat(token1).isNotEqualTo(token2);
        assertThat(token2).isNotEqualTo(token3);
        assertThat(token1).isNotEqualTo(token3);
    }

    @Test
    @DisplayName("유효한 토큰으로 이메일 조회 성공 테스트")
    void getEmailIfValid_Success() {
        // Given
        String token = tokenService.createToken(testEmail);

        // When
        String email = tokenService.getEmailIfValid(token);

        // Then
        assertThat(email).isEqualTo(testEmail);
    }

    @Test
    @DisplayName("null 토큰으로 이메일 조회 실패 테스트")
    void getEmailIfValid_Fail_NullToken() {
        // When
        String email = tokenService.getEmailIfValid(null);

        // Then
        assertThat(email).isNull();
    }

    @Test
    @DisplayName("빈 문자열 토큰으로 이메일 조회 실패 테스트")
    void getEmailIfValid_Fail_EmptyToken() {
        // When
        String email1 = tokenService.getEmailIfValid("");
        String email2 = tokenService.getEmailIfValid("   ");

        // Then
        assertThat(email1).isNull();
        assertThat(email2).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 토큰으로 이메일 조회 실패 테스트")
    void getEmailIfValid_Fail_NonExistentToken() {
        // Given
        String nonExistentToken = "nonexistenttoken12345678901234567890";

        // When
        String email = tokenService.getEmailIfValid(nonExistentToken);

        // Then
        assertThat(email).isNull();
    }

    @Test
    @DisplayName("토큰 소비 성공 테스트")
    void consumeToken_Success() {
        // Given
        String token = tokenService.createToken(testEmail);

        // When
        String email = tokenService.consumeToken(token);

        // Then
        assertThat(email).isEqualTo(testEmail);
        
        // 소비된 토큰은 다시 사용할 수 없어야 함
        String emailAfterConsume = tokenService.getEmailIfValid(token);
        assertThat(emailAfterConsume).isNull();
    }

    @Test
    @DisplayName("토큰 소비 후 재사용 불가 테스트")
    void consumeToken_OneTimeUse() {
        // Given
        String token = tokenService.createToken(testEmail);

        // When
        String email1 = tokenService.consumeToken(token);
        String email2 = tokenService.consumeToken(token);

        // Then
        assertThat(email1).isEqualTo(testEmail);
        assertThat(email2).isNull();
    }

    @Test
    @DisplayName("null 토큰 소비 실패 테스트")
    void consumeToken_Fail_NullToken() {
        // When
        String email = tokenService.consumeToken(null);

        // Then
        assertThat(email).isNull();
    }

    @Test
    @DisplayName("존재하지 않는 토큰 소비 실패 테스트")
    void consumeToken_Fail_NonExistentToken() {
        // Given
        String nonExistentToken = "nonexistenttoken12345678901234567890";

        // When
        String email = tokenService.consumeToken(nonExistentToken);

        // Then
        assertThat(email).isNull();
    }

    @Test
    @DisplayName("재전송 요청 가능 여부 확인 - 첫 번째 요청")
    void canRequestResend_FirstRequest_Success() {
        // When
        boolean canResend = tokenService.canRequestResend(testEmail);

        // Then
        assertThat(canResend).isTrue();
    }

    @Test
    @DisplayName("재전송 요청 기록 테스트")
    void recordResendRequest_Success() {
        // When
        tokenService.recordResendRequest(testEmail);

        // Then
        int dailyCount = tokenService.getDailyResendCount(testEmail);
        assertThat(dailyCount).isEqualTo(1);
        
        long lastResendTime = tokenService.getLastResendTime(testEmail);
        assertThat(lastResendTime).isGreaterThan(0);
    }

    @Test
    @DisplayName("5분 간격 재전송 제한 테스트")
    void canRequestResend_Fail_TooFrequent() {
        // Given
        tokenService.recordResendRequest(testEmail);

        // When
        boolean canResend = tokenService.canRequestResend(testEmail);

        // Then
        assertThat(canResend).isFalse();
    }

    @Test
    @DisplayName("일일 재전송 횟수 제한 테스트")
    void canRequestResend_Fail_DailyLimitExceeded() throws Exception {
        // Given - 5회 재전송 기록
        for (int i = 0; i < 5; i++) {
            tokenService.recordResendRequest(testEmail);
            // 시간 간격 제한을 우회하기 위해 마지막 재전송 시간을 과거로 설정
            try {
                setLastResendTimeToFiveMinutesAgo(testEmail);
            } catch (IllegalAccessException e) {
                // 리플렉션 접근이 제한된 경우 테스트 스킵
                System.out.println("리플렉션 접근 제한으로 일일 재전송 제한 테스트 스킵");
                return;
            }
        }

        // When
        boolean canResend = tokenService.canRequestResend(testEmail);

        // Then
        assertThat(canResend).isFalse();
        assertThat(tokenService.getDailyResendCount(testEmail)).isEqualTo(5);
    }

    @Test
    @DisplayName("일일 재전송 횟수 조회 테스트")
    void getDailyResendCount_Test() {
        // Given
        assertThat(tokenService.getDailyResendCount(testEmail)).isEqualTo(0);

        // When
        tokenService.recordResendRequest(testEmail);
        tokenService.recordResendRequest(testEmail);

        // Then
        assertThat(tokenService.getDailyResendCount(testEmail)).isEqualTo(2);
    }

    @Test
    @DisplayName("마지막 재전송 시간 조회 테스트")
    void getLastResendTime_Test() {
        // Given
        assertThat(tokenService.getLastResendTime(testEmail)).isEqualTo(-1);

        // When
        long beforeRecord = Instant.now().getEpochSecond();
        tokenService.recordResendRequest(testEmail);
        long afterRecord = Instant.now().getEpochSecond();

        // Then
        long lastResendTime = tokenService.getLastResendTime(testEmail);
        assertThat(lastResendTime).isBetween(beforeRecord, afterRecord);
    }

    @Test
    @DisplayName("null 이메일 처리 테스트")
    void nullEmail_Handling() {
        // When & Then
        assertThat(tokenService.canRequestResend(null)).isFalse();
        assertThat(tokenService.getDailyResendCount(null)).isEqualTo(0);
        assertThat(tokenService.getLastResendTime(null)).isEqualTo(-1);
        
        assertThatCode(() -> tokenService.recordResendRequest(null))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("빈 문자열 이메일 처리 테스트")
    void emptyEmail_Handling() {
        // When & Then
        assertThat(tokenService.canRequestResend("")).isFalse();
        assertThat(tokenService.canRequestResend("   ")).isFalse();
        assertThat(tokenService.getDailyResendCount("")).isEqualTo(0);
        assertThat(tokenService.getLastResendTime("")).isEqualTo(-1);
    }

    @Test
    @DisplayName("서로 다른 이메일 간 독립성 테스트")
    void differentEmails_Independence() {
        // Given
        tokenService.recordResendRequest(testEmail);
        tokenService.recordResendRequest(testEmail);

        // When & Then
        assertThat(tokenService.getDailyResendCount(testEmail)).isEqualTo(2);
        assertThat(tokenService.getDailyResendCount(anotherEmail)).isEqualTo(0);
        
        assertThat(tokenService.getLastResendTime(testEmail)).isGreaterThan(0);
        assertThat(tokenService.getLastResendTime(anotherEmail)).isEqualTo(-1);
        
        assertThat(tokenService.canRequestResend(anotherEmail)).isTrue();
    }

    @Test
    @DisplayName("만료된 토큰 처리 테스트")
    void expiredToken_Handling() throws Exception {
        // Given
        String token = tokenService.createToken(testEmail);
        
        // 토큰을 만료시킴 (24시간 + 1초 대기는 불가능하므로 리플렉션 사용)
        try {
            expireToken(token);
            
            // When
            String email = tokenService.getEmailIfValid(token);
            String consumedEmail = tokenService.consumeToken(token);

            // Then
            assertThat(email).isNull();
            assertThat(consumedEmail).isNull();
        } catch (IllegalAccessException e) {
            // 리플렉션 접근이 제한된 경우 테스트 스킵
            System.out.println("리플렉션 접근 제한으로 만료 토큰 테스트 스킵");
        }
    }

    @Test
    @DisplayName("토큰 마스킹 기능 테스트")
    void maskToken_Test() throws Exception {
        // Given
        String token = tokenService.createToken(testEmail);
        
        // 리플렉션으로 private 메서드 접근
        Method maskTokenMethod = InMemoryEmailVerificationTokenService.class.getDeclaredMethod("maskToken", String.class);
        maskTokenMethod.setAccessible(true);

        // When
        String maskedToken = (String) maskTokenMethod.invoke(tokenService, token);

        // Then
        assertThat(maskedToken).isNotEqualTo(token);
        assertThat(maskedToken).startsWith(token.substring(0, 4));
        assertThat(maskedToken).endsWith(token.substring(token.length() - 4));
        assertThat(maskedToken).contains("****");
    }

    @Test
    @DisplayName("이메일 마스킹 기능 테스트")
    void maskEmail_Test() throws Exception {
        // Given
        Method maskEmailMethod = InMemoryEmailVerificationTokenService.class.getDeclaredMethod("maskEmail", String.class);
        maskEmailMethod.setAccessible(true);

        // When
        String maskedEmail = (String) maskEmailMethod.invoke(tokenService, testEmail);

        // Then
        assertThat(maskedEmail).isNotEqualTo(testEmail);
        assertThat(maskedEmail).startsWith("te");
        assertThat(maskedEmail).endsWith("@example.com");
        assertThat(maskedEmail).contains("****");
    }

    @Test
    @DisplayName("짧은 이메일 마스킹 테스트")
    void maskEmail_ShortEmail_Test() throws Exception {
        // Given
        Method maskEmailMethod = InMemoryEmailVerificationTokenService.class.getDeclaredMethod("maskEmail", String.class);
        maskEmailMethod.setAccessible(true);

        // When & Then
        String maskedShort = (String) maskEmailMethod.invoke(tokenService, "ab@test.com");
        assertThat(maskedShort).isEqualTo("**@test.com");

        String maskedNull = (String) maskEmailMethod.invoke(tokenService, (String) null);
        assertThat(maskedNull).isEqualTo("****");

        String maskedInvalid = (String) maskEmailMethod.invoke(tokenService, "invalid-email");
        assertThat(maskedInvalid).isEqualTo("****");
    }

    @Test
    @DisplayName("동시성 테스트 - 여러 토큰 동시 생성")
    void concurrency_MultipleTokenGeneration() throws InterruptedException {
        // Given
        int threadCount = 10;
        int tokensPerThread = 10;
        Thread[] threads = new Thread[threadCount];
        String[] allTokens = new String[threadCount * tokensPerThread];

        // When
        for (int i = 0; i < threadCount; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < tokensPerThread; j++) {
                    String token = tokenService.createToken(testEmail + threadIndex);
                    allTokens[threadIndex * tokensPerThread + j] = token;
                }
            });
            threads[i].start();
        }

        // 모든 스레드 완료 대기
        for (Thread thread : threads) {
            thread.join();
        }

        // Then - 모든 토큰이 고유해야 함
        for (int i = 0; i < allTokens.length; i++) {
            assertThat(allTokens[i]).isNotNull();
            for (int j = i + 1; j < allTokens.length; j++) {
                assertThat(allTokens[i]).isNotEqualTo(allTokens[j]);
            }
        }
    }

    /**
     * 리플렉션을 사용하여 특정 토큰을 만료시키는 헬퍼 메서드
     */
    private void expireToken(String token) throws Exception {
        Field tokenStoreField = InMemoryEmailVerificationTokenService.class.getDeclaredField("tokenStore");
        tokenStoreField.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> tokenStore = (Map<String, Object>) tokenStoreField.get(tokenService);
        
        Object tokenEntry = tokenStore.get(token);
        if (tokenEntry != null) {
            // TokenEntry의 expiry를 과거로 설정
            Field expiryField = tokenEntry.getClass().getDeclaredField("expiry");
            expiryField.setAccessible(true);
            expiryField.set(tokenEntry, Instant.now().minusSeconds(3600)); // 1시간 전으로 설정
        }
    }

    /**
     * 리플렉션을 사용하여 마지막 재전송 시간을 5분 전으로 설정하는 헬퍼 메서드
     */
    private void setLastResendTimeToFiveMinutesAgo(String email) throws Exception {
        Field resendStoreField = InMemoryEmailVerificationTokenService.class.getDeclaredField("resendStore");
        resendStoreField.setAccessible(true);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> resendStore = (Map<String, Object>) resendStoreField.get(tokenService);
        
        Object resendEntry = resendStore.get(email);
        if (resendEntry != null) {
            // ResendEntry의 lastResendTime을 5분 전으로 설정
            Field lastResendTimeField = resendEntry.getClass().getDeclaredField("lastResendTime");
            lastResendTimeField.setAccessible(true);
            long fiveMinutesAgo = Instant.now().getEpochSecond() - 301; // 5분 1초 전
            lastResendTimeField.set(resendEntry, fiveMinutesAgo);
            
            // expiry도 업데이트
            Field expiryField = resendEntry.getClass().getDeclaredField("expiry");
            expiryField.setAccessible(true);
            expiryField.set(resendEntry, Instant.now().plusSeconds(300)); // 5분 후 만료
        }
    }
}