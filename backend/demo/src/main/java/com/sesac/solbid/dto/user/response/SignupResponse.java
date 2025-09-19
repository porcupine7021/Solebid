package com.sesac.solbid.dto.user.response;

import com.sesac.solbid.domain.User;

/**
 * 사용자 회원가입 응답 DTO
 * <p>
 * 사용자 회원가입 완료 시 반환되는 정보를 담는 DTO입니다.
 * 가입된 사용자 정보와 다음 단계 안내 메시지를 포함합니다.
 * </p>
 * 
 * @param userId 생성된 사용자 고유 ID
 * @param email 사용자 이메일 주소
 * @param nickname 사용자 닉네임
 * @param emailVerified 이메일 인증 완료 여부
 * @param message 회원가입 완료 메시지
 * @param nextStep 다음 단계 안내 메시지
 */
public record SignupResponse(
    Long userId,
    String email,
    String nickname,
    Boolean emailVerified,
    String message,
    String nextStep
) {
    /**
     * User 엔티티로부터 SignupResponse를 생성합니다.
     * 이메일 인증 상태에 따라 적절한 다음 단계 메시지를 설정합니다.
     * 
     * @param user 가입된 사용자 엔티티
     */
    public SignupResponse(User user) {
        this(
            user.getUserId(),
            user.getEmail(),
            user.getNickname(),
            user.getEmailVerified(),
            "회원가입이 완료되었습니다.",
            user.getEmailVerified() ? 
                "로그인하여 서비스를 이용하세요." : 
                "이메일 인증을 완료한 후 로그인할 수 있습니다."
        );
    }
}

