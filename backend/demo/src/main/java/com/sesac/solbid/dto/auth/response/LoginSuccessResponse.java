package com.sesac.solbid.dto.auth.response;

/**
 * OAuth2 로그인 성공 응답 DTO (토큰 제외)
 */
public record LoginSuccessResponse(
    Long userId,
    String email,
    String nickname,
    String userType,
    String provider,
    boolean requiresNickname
) {
    /**
     * Object 타입 매개변수를 toString()으로 변환하는 생성자
     */
    public LoginSuccessResponse(Long userId, String email, String nickname,
                                Object userType, String provider, boolean requiresNickname) {
        this(
            userId,
            email,
            nickname,
            userType != null ? userType.toString() : null,
            provider,
            requiresNickname
        );
    }
}

