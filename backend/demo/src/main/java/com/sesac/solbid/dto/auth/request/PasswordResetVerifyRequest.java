package com.sesac.solbid.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 비밀번호 재설정 인증 및 변경 요청 DTO
 * <p>
 * 비밀번호 재설정 시 OTP 인증과 새 비밀번호 설정을 동시에 처리하는 요청에 필요한 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param email 사용자 이메일 주소 (필수, 이메일 형식 검증)
 * @param otp 이메일로 발송된 6자리 OTP 인증번호 (필수, 6자리 숫자)
 * @param newPassword 새로운 비밀번호 (필수, 8~64자)
 */
public record PasswordResetVerifyRequest(
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,

    @NotBlank(message = "인증번호는 필수입니다.")
    @Pattern(regexp = "^[0-9]{6}$", message = "인증번호는 6자리 숫자여야 합니다.")
    String otp,

    @NotBlank(message = "새 비밀번호는 필수입니다.")
    @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하여야 합니다.")
    String newPassword
) {}