package com.sesac.solbid.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 이메일 인증 예외 처리 테스트
 */
class EmailVerificationExceptionTest {

    @Test
    void testEmailVerificationExceptionWithBasicConstructor() {
        // Given
        String email = "test@example.com";
        ErrorCode errorCode = ErrorCode.EMAIL_NOT_VERIFIED;

        // When
        EmailVerificationException exception = new EmailVerificationException(errorCode, email);

        // Then
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(email, exception.getEmail());
        assertNull(exception.getAdditionalInfo());
        assertEquals(errorCode.getMessage(), exception.getMessage());
    }

    @Test
    void testEmailVerificationExceptionWithAdditionalInfo() {
        // Given
        String email = "test@example.com";
        String additionalInfo = "5분 후 재시도 가능";
        ErrorCode errorCode = ErrorCode.EMAIL_VERIFICATION_RESEND_TOO_FREQUENT;

        // When
        EmailVerificationException exception = new EmailVerificationException(errorCode, email, additionalInfo);

        // Then
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(email, exception.getEmail());
        assertEquals(additionalInfo, exception.getAdditionalInfo());
    }

    @Test
    void testMaskEmailWithValidEmail() {
        // Given
        String email = "testuser@example.com";
        EmailVerificationException exception = new EmailVerificationException(ErrorCode.EMAIL_NOT_VERIFIED, email);

        // When
        String maskedEmail = exception.getMaskedEmail();

        // Then
        assertEquals("te****@example.com", maskedEmail);
    }

    @Test
    void testMaskEmailWithShortEmail() {
        // Given
        String email = "ab@example.com";
        EmailVerificationException exception = new EmailVerificationException(ErrorCode.EMAIL_NOT_VERIFIED, email);

        // When
        String maskedEmail = exception.getMaskedEmail();

        // Then
        assertEquals("**@example.com", maskedEmail);
    }

    @Test
    void testMaskEmailWithInvalidEmail() {
        // Given
        String email = "invalid-email";
        EmailVerificationException exception = new EmailVerificationException(ErrorCode.EMAIL_NOT_VERIFIED, email);

        // When
        String maskedEmail = exception.getMaskedEmail();

        // Then
        assertEquals("****", maskedEmail);
    }

    @Test
    void testMaskEmailWithNullEmail() {
        // Given
        EmailVerificationException exception = new EmailVerificationException(ErrorCode.EMAIL_NOT_VERIFIED, null);

        // When
        String maskedEmail = exception.getMaskedEmail();

        // Then
        assertEquals("****", maskedEmail);
    }
}