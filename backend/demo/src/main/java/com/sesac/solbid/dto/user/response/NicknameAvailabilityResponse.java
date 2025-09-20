package com.sesac.solbid.dto.user.response;

/**
 * 닉네임 사용 가능 여부 응답 DTO
 * <p>
 * 닉네임 중복 확인 요청에 대한 응답 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param available 닉네임 사용 가능 여부 (true: 사용 가능, false: 이미 사용 중)
 */
public record NicknameAvailabilityResponse(
    boolean available
) {
    /**
     * 닉네임 사용 가능 여부로부터 NicknameAvailabilityResponse를 생성합니다.
     * 
     * @param available 닉네임 사용 가능 여부
     * @return 생성된 NicknameAvailabilityResponse 객체
     */
    public static NicknameAvailabilityResponse of(boolean available) {
        return new NicknameAvailabilityResponse(available);
    }
}

