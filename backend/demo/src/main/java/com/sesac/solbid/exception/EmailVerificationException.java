package com.sesac.solbid.exception;

/**
 * 이메일 인증 관련 예외 클래스
 * 이메일 인증 과정에서 발생하는 특수한 상황들을 처리하기 위한 커스텀 예외입니다.
 */
public class EmailVerificationException extends CustomException {
    
    private final String email;
    private final String additionalInfo;

    public EmailVerificationException(ErrorCode errorCode, String email) {
        this(errorCode, email, null);
    }

    public EmailVerificationException(ErrorCode errorCode, String email, String additionalInfo) {
        super(errorCode);
        this.email = email;
        this.additionalInfo = additionalInfo;
    }

    public String getEmail() {
        return email;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * 이메일 주소를 마스킹하여 반환합니다.
     * @return 마스킹된 이메일 주소
     */
    public String getMaskedEmail() {
        if (email == null || !email.contains("@")) {
            return "****";
        }
        String[] parts = email.split("@");
        if (parts[0].length() <= 2) {
            return "**@" + parts[1];
        }
        return parts[0].substring(0, 2) + "****@" + parts[1];
    }
}