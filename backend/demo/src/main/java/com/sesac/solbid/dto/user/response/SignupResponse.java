package com.sesac.solbid.dto.user.response;

import com.sesac.solbid.domain.User;
import lombok.Getter;

@Getter
public class SignupResponse {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final Boolean emailVerified;
    private final String message;
    private final String nextStep;

    public SignupResponse(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.emailVerified = user.getEmailVerified();
        this.message = "회원가입이 완료되었습니다.";
        this.nextStep = user.getEmailVerified() ? 
            "로그인하여 서비스를 이용하세요." : 
            "이메일 인증을 완료한 후 로그인할 수 있습니다.";
    }
}

