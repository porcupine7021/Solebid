package com.sesac.solbid.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 사용자 닉네임 변경 요청 DTO
 * <p>
 * 사용자의 닉네임 변경 요청 시 필요한 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param nickname 새로운 닉네임 (필수, 2~10자)
 */
public record NicknameUpdateRequest(
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    String nickname
) {}

