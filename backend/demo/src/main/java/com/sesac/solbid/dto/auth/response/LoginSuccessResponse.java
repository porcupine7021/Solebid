package com.sesac.solbid.dto.auth.response;

/**
 * OAuth2 로그인 성공 응답 DTO (토큰 제외)
 * <p>
 * OAuth2 소셜 로그인 성공 시 반환되는 사용자 정보를 담는 DTO입니다.
 * JWT 토큰은 별도로 쿠키나 헤더를 통해 전달되며, 이 DTO는 사용자 기본 정보만 포함합니다.
 * </p>
 * 
 * @param userId 사용자 고유 ID
 * @param email 사용자 이메일 주소
 * @param nickname 사용자 닉네임
 * @param userType 사용자 타입 (USER, ADMIN 등)
 * @param provider 소셜 로그인 제공자명 (google, kakao 등)
 * @param requiresNickname 닉네임 설정 필요 여부 (소셜 로그인 시 닉네임이 없는 경우)
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
     * Object 타입 userType 매개변수를 문자열로 변환하는 생성자
     * 
     * @param userId 사용자 고유 ID
     * @param email 사용자 이메일 주소
     * @param nickname 사용자 닉네임
     * @param userType 사용자 타입 (Object 타입, toString()으로 변환됨)
     * @param provider 소셜 로그인 제공자명
     * @param requiresNickname 닉네임 설정 필요 여부
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

