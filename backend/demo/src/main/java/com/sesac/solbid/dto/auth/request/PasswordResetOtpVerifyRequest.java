package com.sesac.solbid.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 비밀번호 재설정 OTP 인증 요청 DTO
 * <p>
 * 비밀번호 재설정 시 이메일로 발송된 OTP 인증번호 검증 요청에 필요한 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param email 사용자 이메일 주소 (필수, 이메일 형식 검증)
 * @param otp 이메일로 발송된 6자리 OTP 인증번호 (필수, 6자리 숫자)
 */
public record PasswordResetOtpVerifyRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,
    
    @NotBlank(message = "인증번호는 필수입니다.")
    @Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리 숫자여야 합니다.")
    String otp
) {}