package com.sesac.solbid.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 비밀번호 재설정 요청 DTO
 * <p>
 * 사용자의 비밀번호 재설정 요청 시 필요한 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param email 비밀번호를 재설정할 사용자의 이메일 주소 (필수, 이메일 형식 검증)
 */
public record PasswordResetRequest(
    @NotBlank
    @Email
    String email
) {}

