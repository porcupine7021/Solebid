package com.sesac.solbid.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 이메일 인증 재전송 요청 DTO
 * <p>
 * 회원가입 후 이메일 인증 메일 재전송 요청 시 필요한 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param email 인증 메일을 재전송할 사용자의 이메일 주소 (필수, 이메일 형식 검증)
 */
public record ResendVerificationRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    String email
) {}