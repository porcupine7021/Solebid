package com.sesac.solbid.dto.auth.response;

/**
 * 이메일 인증 응답 DTO
 */
public record EmailVerificationResponse(
    String email,
    String message
) {
    
    public static EmailVerificationResponse success(String email, String message) {
        return new EmailVerificationResponse(email, message);
    }
}