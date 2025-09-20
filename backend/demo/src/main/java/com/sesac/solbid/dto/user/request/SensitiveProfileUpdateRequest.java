package com.sesac.solbid.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * 민감한 프로필 정보 업데이트 요청 DTO
 * <p>
 * 이메일, 전화번호 등 민감한 정보 업데이트 시 사용되는 DTO입니다.
 * 스텝업 인증을 위해 현재 비밀번호 확인이 필요합니다.
 * </p>
 * 
 * @param currentPassword 현재 비밀번호 (스텝업 인증용)
 * @param email 새로운 이메일 주소 (선택적)
 * @param phone 새로운 전화번호 (선택적, 010-XXXX-XXXX 형식)
 */
public record SensitiveProfileUpdateRequest(
    @NotBlank(message = "현재 비밀번호는 필수입니다")
    String currentPassword,
    
    @Email(message = "올바른 이메일 형식이 아닙니다")
    String email,
    
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다")
    String phone
) {
    /**
     * 업데이트할 필드가 있는지 확인합니다.
     * 
     * @return 이메일 또는 전화번호 중 하나라도 업데이트할 값이 있으면 true
     */
    public boolean hasUpdates() {
        return (email != null && !email.isBlank()) || 
               (phone != null && !phone.isBlank());
    }
}