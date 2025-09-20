package com.sesac.solbid.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 비밀번호 재설정 확인 요청 DTO
 * <p>
 * 비밀번호 재설정 토큰을 통한 새 비밀번호 설정 요청 시 필요한 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param token 비밀번호 재설정 토큰 (필수)
 * @param newPassword 새로운 비밀번호 (필수, 8~64자)
 */
public record PasswordResetConfirmRequest(
    @NotBlank
    String token,

    @NotBlank
    @Size(min = 8, max = 64)
    String newPassword
) {}

