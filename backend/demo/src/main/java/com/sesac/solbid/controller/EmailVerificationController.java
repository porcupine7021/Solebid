package com.sesac.solbid.controller;

import com.sesac.solbid.dto.ApiResponse;
import com.sesac.solbid.dto.auth.request.ResendVerificationRequest;
import com.sesac.solbid.dto.auth.request.VerifyCodeRequest;
import com.sesac.solbid.dto.auth.response.EmailVerificationResponse;
import com.sesac.solbid.exception.CustomException;
import com.sesac.solbid.exception.EmailVerificationException;
import com.sesac.solbid.service.user.EmailVerificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * 이메일 인증 컨트롤러
 * 이메일 인증 및 재전송 요청을 처리합니다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    /**
     * 이메일 인증 처리
     * GET /api/auth/verify-email?token={token}
     * 
     * @param token 인증 토큰
     * @param request HTTP 요청 (로깅용)
     * @return 인증 결과
     */
    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponse<EmailVerificationResponse>> verifyEmail(
            @RequestParam String token,
            HttpServletRequest request) {
        
        String clientIp = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        
        log.info("이메일 인증 요청: clientIp={}, userAgent={}, token={}", 
                clientIp, maskUserAgent(userAgent), maskToken(token));
        
        try {
            String email = emailVerificationService.verifyEmail(token);
            
            EmailVerificationResponse response = EmailVerificationResponse.success(
                    maskEmail(email), 
                    "이메일 인증이 완료되었습니다."
            );
            
            log.info("이메일 인증 성공: clientIp={}, email={}", clientIp, maskEmail(email));
            
            return ResponseEntity.ok(
                ApiResponse.success(response, "이메일 인증이 완료되었습니다.")
            );
            
        } catch (EmailVerificationException e) {
            log.warn("이메일 인증 실패: clientIp={}, error={}, token={}", 
                    clientIp, e.getMessage(), maskToken(token));
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(
                ApiResponse.error(e.getErrorCode().name(), e.getMessage())
            );
        } catch (CustomException e) {
            log.warn("이메일 인증 실패(Custom): clientIp={}, error={}, token={}", 
                    clientIp, e.getMessage(), maskToken(token));
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(
                ApiResponse.error(e.getErrorCode().name(), e.getMessage())
            );
        } catch (Exception e) {
            log.error("이메일 인증 중 예외 발생: clientIp={}, token={}", 
                    clientIp, maskToken(token), e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.error("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.")
            );
        }
    }

    /**
     * 이메일 인증번호 검증 (새로운 방식)
     * POST /api/auth/verify-code
     * 
     * @param request 인증번호 검증 요청 (이메일과 인증번호 포함)
     * @param httpRequest HTTP 요청 (로깅용)
     * @return 인증 결과
     */
    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponse<EmailVerificationResponse>> verifyCode(
            @Valid @RequestBody VerifyCodeRequest request,
            HttpServletRequest httpRequest) {
        
        String clientIp = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        
        log.info("이메일 인증번호 검증 요청: clientIp={}, userAgent={}, email={}, code={}", 
                clientIp, maskUserAgent(userAgent), maskEmail(request.email()), maskCode(request.verificationCode()));
        
        try {
            String email = emailVerificationService.verifyEmailWithCode(request.email(), request.verificationCode());
            
            EmailVerificationResponse response = EmailVerificationResponse.success(
                    maskEmail(email), 
                    "이메일 인증이 완료되었습니다."
            );
            
            log.info("이메일 인증번호 검증 성공: clientIp={}, email={}", clientIp, maskEmail(email));
            
            return ResponseEntity.ok(
                ApiResponse.success(response, "이메일 인증이 완료되었습니다.")
            );
            
        } catch (EmailVerificationException e) {
            log.warn("이메일 인증번호 검증 실패: clientIp={}, email={}, error={}", 
                    clientIp, maskEmail(request.email()), e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(
                ApiResponse.error(e.getErrorCode().name(), e.getMessage())
            );
        } catch (CustomException e) {
            log.warn("이메일 인증번호 검증 실패(Custom): clientIp={}, email={}, error={}", 
                    clientIp, maskEmail(request.email()), e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(
                ApiResponse.error(e.getErrorCode().name(), e.getMessage())
            );
        } catch (Exception e) {
            log.error("이메일 인증번호 검증 중 예외 발생: clientIp={}, email={}", 
                    clientIp, maskEmail(request.email()), e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.error("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.")
            );
        }
    }

    /**
     * 회원가입 전 이메일 인증번호 검증
     * POST /api/auth/verify-signup-code
     * 
     * @param request 인증번호 검증 요청 (이메일과 인증번호 포함)
     * @param httpRequest HTTP 요청 (로깅용)
     * @return 인증 결과
     */
    @PostMapping("/verify-signup-code")
    public ResponseEntity<ApiResponse<EmailVerificationResponse>> verifySignupCode(
            @Valid @RequestBody VerifyCodeRequest request,
            HttpServletRequest httpRequest) {
        
        String clientIp = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        
        log.info("회원가입 전 이메일 인증번호 검증 요청: clientIp={}, userAgent={}, email={}, code={}", 
                clientIp, maskUserAgent(userAgent), maskEmail(request.email()), maskCode(request.verificationCode()));
        
        try {
            String email = emailVerificationService.verifyEmailForSignup(request.email(), request.verificationCode());
            
            EmailVerificationResponse response = EmailVerificationResponse.success(
                    maskEmail(email), 
                    "이메일 인증이 완료되었습니다."
            );
            
            log.info("회원가입 전 이메일 인증번호 검증 성공: clientIp={}, email={}", clientIp, maskEmail(email));
            
            return ResponseEntity.ok(
                ApiResponse.success(response, "이메일 인증이 완료되었습니다.")
            );
            
        } catch (EmailVerificationException e) {
            log.warn("회원가입 전 이메일 인증번호 검증 실패: clientIp={}, email={}, error={}", 
                    clientIp, maskEmail(request.email()), e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(
                ApiResponse.error(e.getErrorCode().name(), e.getMessage())
            );
        } catch (CustomException e) {
            log.warn("회원가입 전 이메일 인증번호 검증 실패(Custom): clientIp={}, email={}, error={}", 
                    clientIp, maskEmail(request.email()), e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(
                ApiResponse.error(e.getErrorCode().name(), e.getMessage())
            );
        } catch (Exception e) {
            log.error("회원가입 전 이메일 인증번호 검증 중 예외 발생: clientIp={}, email={}", 
                    clientIp, maskEmail(request.email()), e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.error("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.")
            );
        }
    }

    /**
     * 회원가입 전 이메일 인증번호 전송
     * POST /api/auth/send-verification
     * 
     * @param request 인증번호 전송 요청 (이메일 포함)
     * @param httpRequest HTTP 요청 (로깅용)
     * @return 전송 결과
     */
    @PostMapping("/send-verification")
    public ResponseEntity<ApiResponse<Object>> sendVerification(
            @Valid @RequestBody ResendVerificationRequest request,
            HttpServletRequest httpRequest) {
        
        String clientIp = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        
        log.info("회원가입 전 이메일 인증번호 전송 요청: clientIp={}, userAgent={}, email={}", 
                clientIp, maskUserAgent(userAgent), maskEmail(request.email()));
        
        try {
            emailVerificationService.sendVerificationForSignup(request.email());
            
            log.info("회원가입 전 이메일 인증번호 전송 성공: clientIp={}, email={}", 
                    clientIp, maskEmail(request.email()));
            
            return ResponseEntity.ok(
                ApiResponse.success(Collections.emptyMap(), "인증번호를 전송했습니다.")
            );
            
        } catch (EmailVerificationException e) {
            log.warn("회원가입 전 이메일 인증번호 전송 실패: clientIp={}, email={}, error={}", 
                    clientIp, maskEmail(request.email()), e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(
                ApiResponse.error(e.getErrorCode().name(), e.getMessage())
            );
        } catch (CustomException e) {
            log.warn("회원가입 전 이메일 인증번호 전송 실패(Custom): clientIp={}, email={}, error={}", 
                    clientIp, maskEmail(request.email()), e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(
                ApiResponse.error(e.getErrorCode().name(), e.getMessage())
            );
        } catch (Exception e) {
            log.error("회원가입 전 이메일 인증번호 전송 중 예외 발생: clientIp={}, email={}", 
                    clientIp, maskEmail(request.email()), e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.error("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.")
            );
        }
    }

    /**
     * 인증 이메일 재전송
     * POST /api/auth/resend-verification
     * 
     * @param request 재전송 요청 (이메일 포함)
     * @param httpRequest HTTP 요청 (로깅용)
     * @return 재전송 결과
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<Object>> resendVerification(
            @Valid @RequestBody ResendVerificationRequest request,
            HttpServletRequest httpRequest) {
        
        String clientIp = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        
        log.info("이메일 인증 재전송 요청: clientIp={}, userAgent={}, email={}", 
                clientIp, maskUserAgent(userAgent), maskEmail(request.email()));
        
        try {
            emailVerificationService.resendVerificationEmail(request.email());
            
            log.info("이메일 인증 재전송 성공: clientIp={}, email={}", 
                    clientIp, maskEmail(request.email()));
            
            return ResponseEntity.ok(
                ApiResponse.success(Collections.emptyMap(), "인증번호를 재전송했습니다.")
            );
            
        } catch (EmailVerificationException e) {
            log.warn("이메일 인증 재전송 실패: clientIp={}, email={}, error={}", 
                    clientIp, maskEmail(request.email()), e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(
                ApiResponse.error(e.getErrorCode().name(), e.getMessage())
            );
        } catch (CustomException e) {
            log.warn("이메일 인증 재전송 실패(Custom): clientIp={}, email={}, error={}", 
                    clientIp, maskEmail(request.email()), e.getMessage());
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(
                ApiResponse.error(e.getErrorCode().name(), e.getMessage())
            );
        } catch (Exception e) {
            log.error("이메일 인증 재전송 중 예외 발생: clientIp={}, email={}", 
                    clientIp, maskEmail(request.email()), e);
            return ResponseEntity.internalServerError().body(
                ApiResponse.error("INTERNAL_SERVER_ERROR", "서버 내부 오류가 발생했습니다.")
            );
        }
    }

    /**
     * 클라이언트 IP 주소 추출 (프록시 고려)
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    /**
     * User-Agent 마스킹 처리 (보안)
     */
    private String maskUserAgent(String userAgent) {
        if (userAgent == null || userAgent.length() < 20) {
            return "****";
        }
        return userAgent.substring(0, 10) + "****" + userAgent.substring(userAgent.length() - 6);
    }

    /**
     * 토큰 마스킹 처리 (보안)
     */
    private String maskToken(String token) {
        if (token == null || token.length() < 8) {
            return "****";
        }
        return token.substring(0, 4) + "****" + token.substring(token.length() - 4);
    }

    /**
     * 이메일 마스킹 처리 (보안)
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "****";
        }

        String[] parts = email.split("@");
        String localPart = parts[0];
        String domain = parts[1];

        if (localPart.length() <= 2) {
            return "**@" + domain;
        }

        return localPart.substring(0, 2) + "****@" + domain;
    }

    /**
     * 인증번호 마스킹 처리 (보안)
     */
    private String maskCode(String code) {
        if (code == null || code.length() != 6) {
            return "****";
        }
        return code.substring(0, 2) + "****";
    }
}