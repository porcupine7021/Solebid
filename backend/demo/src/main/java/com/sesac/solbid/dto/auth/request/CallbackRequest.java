package com.sesac.solbid.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

/**
 * OAuth2 콜백 요청 DTO
 */
public record CallbackRequest(
    @NotBlank(message = "인증 코드는 필수입니다.")
    String code,
    
    @NotBlank(message = "State 파라미터는 필수입니다.")
    String state
) {}

