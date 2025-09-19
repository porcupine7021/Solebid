package com.sesac.solbid.dto.user.response;

import com.sesac.solbid.domain.User;
import com.sesac.solbid.domain.enums.UserType;

public record LoginResponse(
    Long userId,
    String email,
    String nickname,
    UserType userType,
    String accessToken,
    String refreshToken
) {
    public static LoginResponse from(User user, String accessToken, String refreshToken) {
        return new LoginResponse(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getUserType(),
                accessToken,
                refreshToken
        );
    }
}

