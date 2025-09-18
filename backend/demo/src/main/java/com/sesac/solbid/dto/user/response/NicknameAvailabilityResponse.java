package com.sesac.solbid.dto.user.response;

public record NicknameAvailabilityResponse(
    boolean available
) {
    public static NicknameAvailabilityResponse of(boolean available) {
        return new NicknameAvailabilityResponse(available);
    }
}

