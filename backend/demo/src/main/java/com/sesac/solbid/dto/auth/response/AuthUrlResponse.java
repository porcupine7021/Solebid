package com.sesac.solbid.dto.auth.response;

/**
 * OAuth2 인증 URL 응답 DTO
 */
public record AuthUrlResponse(
    String authUrl,
    String state,
    String provider
) {

    public static AuthUrlResponse of(String authUrl, String state, String provider) {
        return new AuthUrlResponse(authUrl, state, provider);
    }
}

