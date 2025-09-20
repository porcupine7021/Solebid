package com.sesac.solbid.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 이메일 인증번호 검증 요청 DTO
 * <p>
 * 회원가입 후 이메일로 발송된 인증번호 검증 요청 시 필요한 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param email 인증할 사용자의 이메일 주소 (필수, 이메일 형식 검증)
 * @param verificationCode 이메일로 발송된 6자리 인증번호 (필수, 6자리 숫자)
 */
public record VerifyCodeRequest(
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,

    @NotBlank(message = "인증번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{6}$", message = "인증번호는 6자리 숫자여야 합니다.")
    String verificationCode
) {}