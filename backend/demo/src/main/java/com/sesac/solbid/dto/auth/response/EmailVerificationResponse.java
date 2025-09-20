package com.sesac.solbid.dto.auth.response;

/**
 * 이메일 인증 응답 DTO
 * <p>
 * 이메일 인증 관련 요청(인증번호 발송, 인증 완료 등)에 대한 응답 정보를 담는 DTO입니다.
 * </p>
 * 
 * @param email 인증 대상 이메일 주소
 * @param message 인증 처리 결과 메시지
 */
public record EmailVerificationResponse(
    String email,
    String message
) {
    
    /**
     * 성공적인 이메일 인증 응답을 생성합니다.
     * 
     * @param email 인증 대상 이메일 주소
     * @param message 성공 메시지
     * @return 생성된 EmailVerificationResponse 객체
     */
    public static EmailVerificationResponse success(String email, String message) {
        return new EmailVerificationResponse(email, message);
    }
}