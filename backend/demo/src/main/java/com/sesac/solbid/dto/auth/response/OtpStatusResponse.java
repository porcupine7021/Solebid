package com.sesac.solbid.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * OTP 상태 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpStatusResponse {
    
    /**
     * OTP 존재 여부
     */
    private boolean exists;
    
    /**
     * 남은 시간 (초)
     */
    private long remainingTimeSeconds;
    
    /**
     * 만료 여부
     */
    private boolean expired;
    
    /**
     * 남은 시도 횟수
     */
    private int remainingAttempts;
}