package com.sesac.solbid.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 사용자 로그인 요청 DTO
 * <p>
 * 사용자의 로그인 요청 시 필요한 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param email 사용자 이메일 주소 (필수, 이메일 형식 검증)
 * @param password 사용자 비밀번호 (필수)
 */
public record LoginRequest(
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    String password
) {}

