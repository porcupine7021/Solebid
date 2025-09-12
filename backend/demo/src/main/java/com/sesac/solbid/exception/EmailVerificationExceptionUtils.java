package com.sesac.solbid.exception;

/**
 * 이메일 인증 예외 처리 유틸리티 클래스
 * 이메일 인증 관련 예외를 생성하고 관리하는 유틸리티 메서드들을 제공합니다.
 */
public class EmailVerificationExceptionUtils {

    /**
     * 이메일 인증 토큰이 유효하지 않을 때 예외를 생성합니다.
     */
    public static EmailVerificationException invalidToken(String email) {
        return new EmailVerificationException(ErrorCode.EMAIL_VERIFICATION_TOKEN_INVALID, email);
    }

    /**
     * 이메일 인증 토큰이 만료되었을 때 예외를 생성합니다.
     */
    public static EmailVerificationException expiredToken(String email) {
        return new EmailVerificationException(ErrorCode.EMAIL_VERIFICATION_TOKEN_EXPIRED, email);
    }

    /**
     * 이미 인증된 이메일일 때 예외를 생성합니다.
     */
    public static EmailVerificationException alreadyVerified(String email) {
        return new EmailVerificationException(ErrorCode.EMAIL_ALREADY_VERIFIED, email);
    }

    /**
     * 재전송 횟수 제한을 초과했을 때 예외를 생성합니다.
     */
    public static EmailVerificationException resendLimitExceeded(String email, int currentCount, int maxCount) {
        String additionalInfo = String.format("현재 %d회, 최대 %d회", currentCount, maxCount);
        return new EmailVerificationException(ErrorCode.EMAIL_VERIFICATION_RESEND_LIMIT_EXCEEDED, email, additionalInfo);
    }

    /**
     * 재전송 간격 제한에 걸렸을 때 예외를 생성합니다.
     */
    public static EmailVerificationException resendTooFrequent(String email, long remainingSeconds) {
        String additionalInfo = String.format("%d초 후 재시도 가능", remainingSeconds);
        return new EmailVerificationException(ErrorCode.EMAIL_VERIFICATION_RESEND_TOO_FREQUENT, email, additionalInfo);
    }

    /**
     * 미인증 계정이 만료되었을 때 예외를 생성합니다.
     */
    public static EmailVerificationException accountExpired(String email) {
        return new EmailVerificationException(ErrorCode.UNVERIFIED_ACCOUNT_EXPIRED, email);
    }

    /**
     * 이메일 인증이 필요할 때 예외를 생성합니다.
     */
    public static EmailVerificationException verificationRequired(String email) {
        return new EmailVerificationException(ErrorCode.EMAIL_NOT_VERIFIED, email);
    }
}