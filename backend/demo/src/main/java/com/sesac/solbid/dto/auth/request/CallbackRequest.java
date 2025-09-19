package com.sesac.solbid.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

/**
 * OAuth2 콜백 요청 DTO
 * <p>
 * OAuth2 인증 제공자로부터 콜백 받은 인증 코드와 상태 정보를 담는 DTO입니다.
 * Google, Kakao 등의 소셜 로그인 콜백 처리에 사용됩니다.
 * </p>
 * 
 * @param code OAuth2 제공자로부터 받은 인증 코드 (필수)
 * @param state CSRF 방지를 위한 상태 파라미터 (필수)
 */
public record CallbackRequest(
    @NotBlank(message = "인증 코드는 필수입니다.")
    String code,
    
    @NotBlank(message = "State 파라미터는 필수입니다.")
    String state
) {}

