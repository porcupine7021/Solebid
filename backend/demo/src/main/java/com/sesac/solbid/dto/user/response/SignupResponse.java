package com.sesac.solbid.dto.user.response;

import com.sesac.solbid.domain.User;

public record SignupResponse(
    Long userId,
    String email,
    String nickname,
    Boolean emailVerified,
    String message,
    String nextStep
) {
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

