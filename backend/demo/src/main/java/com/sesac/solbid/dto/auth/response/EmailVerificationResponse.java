package com.sesac.solbid.dto.auth.response;

import lombok.Builder;
import lombok.Getter;

/**
 * 이메일 인증 응답 DTO
 */
@Getter
@Builder
public class EmailVerificationResponse {
    
    private final String email;
    private final String message;
    
    public static EmailVerificationResponse success(String email, String message) {
        return EmailVerificationResponse.builder()
                .email(email)
                .message(message)
                .build();
    }
}