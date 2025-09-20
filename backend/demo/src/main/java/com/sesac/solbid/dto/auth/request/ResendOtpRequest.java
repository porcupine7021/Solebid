package com.sesac.solbid.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * OTP 재발송 요청 DTO
 * <p>
 * 이메일 인증번호(OTP) 재발송 요청 시 필요한 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param email 인증번호를 재발송할 사용자의 이메일 주소 (필수, 이메일 형식 검증)
 */
public record ResendOtpRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email
) {}