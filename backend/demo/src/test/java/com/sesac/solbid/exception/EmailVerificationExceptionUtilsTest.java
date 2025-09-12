package com.sesac.solbid.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 이메일 인증 예외 유틸리티 테스트
 */
class EmailVerificationExceptionUtilsTest {

    private static final String TEST_EMAIL = "test@example.com";

    @Test
    void testInvalidToken() {
        // When
        EmailVerificationException exception = EmailVerificationExceptionUtils.invalidToken(TEST_EMAIL);

        // Then
        assertEquals(ErrorCode.EMAIL_VERIFICATION_TOKEN_INVALID, exception.getErrorCode());
        assertEquals(TEST_EMAIL, exception.getEmail());
        assertNull(exception.getAdditionalInfo());
    }

    @Test
    void testExpiredToken() {
        // When
        EmailVerificationException exception = EmailVerificationExceptionUtils.expiredToken(TEST_EMAIL);

        // Then
        assertEquals(ErrorCode.EMAIL_VERIFICATION_TOKEN_EXPIRED, exception.getErrorCode());
        assertEquals(TEST_EMAIL, exception.getEmail());
        assertNull(exception.getAdditionalInfo());
    }

    @Test
    void testAlreadyVerified() {
        // When
        EmailVerificationException exception = EmailVerificationExceptionUtils.alreadyVerified(TEST_EMAIL);

        // Then
        assertEquals(ErrorCode.EMAIL_ALREADY_VERIFIED, exception.getErrorCode());
        assertEquals(TEST_EMAIL, exception.getEmail());
        assertNull(exception.getAdditionalInfo());
    }

    @Test
    void testResendLimitExceeded() {
        // Given
        int currentCount = 5;
        int maxCount = 5;

        // When
        EmailVerificationException exception = EmailVerificationExceptionUtils.resendLimitExceeded(TEST_EMAIL, currentCount, maxCount);

        // Then
        assertEquals(ErrorCode.EMAIL_VERIFICATION_RESEND_LIMIT_EXCEEDED, exception.getErrorCode());
        assertEquals(TEST_EMAIL, exception.getEmail());
        assertEquals("현재 5회, 최대 5회", exception.getAdditionalInfo());
    }

    @Test
    void testResendTooFrequent() {
        // Given
        long remainingSeconds = 180; // 3분

        // When
        EmailVerificationException exception = EmailVerificationExceptionUtils.resendTooFrequent(TEST_EMAIL, remainingSeconds);

        // Then
        assertEquals(ErrorCode.EMAIL_VERIFICATION_RESEND_TOO_FREQUENT, exception.getErrorCode());
        assertEquals(TEST_EMAIL, exception.getEmail());
        assertEquals("180초 후 재시도 가능", exception.getAdditionalInfo());
    }

    @Test
    void testAccountExpired() {
        // When
        EmailVerificationException exception = EmailVerificationExceptionUtils.accountExpired(TEST_EMAIL);

        // Then
        assertEquals(ErrorCode.UNVERIFIED_ACCOUNT_EXPIRED, exception.getErrorCode());
        assertEquals(TEST_EMAIL, exception.getEmail());
        assertNull(exception.getAdditionalInfo());
    }

    @Test
    void testVerificationRequired() {
        // When
        EmailVerificationException exception = EmailVerificationExceptionUtils.verificationRequired(TEST_EMAIL);

        // Then
        assertEquals(ErrorCode.EMAIL_NOT_VERIFIED, exception.getErrorCode());
        assertEquals(TEST_EMAIL, exception.getEmail());
        assertNull(exception.getAdditionalInfo());
    }
}