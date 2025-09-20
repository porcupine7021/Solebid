package com.sesac.solbid.dto.auth.response;

/**
 * OAuth2 인증 URL 응답 DTO
 * <p>
 * OAuth2 소셜 로그인 인증 URL 생성 요청에 대한 응답 정보를 담는 DTO입니다.
 * 클라이언트는 이 URL로 사용자를 리다이렉트하여 소셜 로그인을 진행합니다.
 * </p>
 * 
 * @param authUrl OAuth2 제공자의 인증 URL
 * @param state CSRF 방지를 위한 상태 파라미터
 * @param provider 소셜 로그인 제공자명 (google, kakao 등)
 */
public record AuthUrlResponse(
    String authUrl,
    String state,
    String provider
) {

    /**
     * 인증 URL 정보로부터 AuthUrlResponse를 생성합니다.
     * 
     * @param authUrl OAuth2 제공자의 인증 URL
     * @param state CSRF 방지를 위한 상태 파라미터
     * @param provider 소셜 로그인 제공자명
     * @return 생성된 AuthUrlResponse 객체
     */
    public static AuthUrlResponse of(String authUrl, String state, String provider) {
        return new AuthUrlResponse(authUrl, state, provider);
    }
}

