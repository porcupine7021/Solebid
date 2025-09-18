package com.sesac.solbid.dto.auth.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Auth Request DTO 유효성 검증 테스트")
class AuthRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // === CallbackRequest 테스트 ===

    @Test
    @DisplayName("CallbackRequest - 유효한 입력값은 검증을 통과해야 한다")
    void callbackRequest_validInput_shouldPassValidation() {
        // Given
        CallbackRequest request = new CallbackRequest(
                "valid-auth-code-12345",
                "valid-state-67890"
        );

        // When
        Set<ConstraintViolation<CallbackRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("CallbackRequest - 빈 인증 코드는 검증에 실패해야 한다")
    void callbackRequest_blankCode_shouldFailValidation() {
        // Given
        CallbackRequest request = new CallbackRequest(
                "",
                "valid-state"
        );

        // When
        Set<ConstraintViolation<CallbackRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("인증 코드는 필수입니다.");
    }

    @Test
    @DisplayName("CallbackRequest - null 인증 코드는 검증에 실패해야 한다")
    void callbackRequest_nullCode_shouldFailValidation() {
        // Given
        CallbackRequest request = new CallbackRequest(
                null,
                "valid-state"
        );

        // When
        Set<ConstraintViolation<CallbackRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("인증 코드는 필수입니다.");
    }

    @Test
    @DisplayName("CallbackRequest - 빈 state는 검증에 실패해야 한다")
    void callbackRequest_blankState_shouldFailValidation() {
        // Given
        CallbackRequest request = new CallbackRequest(
                "valid-code",
                ""
        );

        // When
        Set<ConstraintViolation<CallbackRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("State 파라미터는 필수입니다.");
    }

    @Test
    @DisplayName("CallbackRequest - null state는 검증에 실패해야 한다")
    void callbackRequest_nullState_shouldFailValidation() {
        // Given
        CallbackRequest request = new CallbackRequest(
                "valid-code",
                null
        );

        // When
        Set<ConstraintViolation<CallbackRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("State 파라미터는 필수입니다.");
    }

    // === PasswordResetRequest 테스트 ===

    @Test
    @DisplayName("PasswordResetRequest - 유효한 이메일은 검증을 통과해야 한다")
    void passwordResetRequest_validEmail_shouldPassValidation() {
        // Given
        PasswordResetRequest request = new PasswordResetRequest("test@example.com");

        // When
        Set<ConstraintViolation<PasswordResetRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("PasswordResetRequest - 잘못된 이메일 형식은 검증에 실패해야 한다")
    void passwordResetRequest_invalidEmail_shouldFailValidation() {
        // Given
        PasswordResetRequest request = new PasswordResetRequest("invalid-email");

        // When
        Set<ConstraintViolation<PasswordResetRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<PasswordResetRequest> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
        // @Email 어노테이션의 기본 메시지 확인
        assertThat(violation.getMessage()).contains("이메일");
    }

    @Test
    @DisplayName("PasswordResetRequest - 빈 이메일은 검증에 실패해야 한다")
    void passwordResetRequest_blankEmail_shouldFailValidation() {
        // Given
        PasswordResetRequest request = new PasswordResetRequest("");

        // When
        Set<ConstraintViolation<PasswordResetRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(2); // @NotBlank와 @Email 둘 다 실패
        assertThat(violations).extracting(ConstraintViolation::getPropertyPath)
                .extracting(Object::toString)
                .containsOnly("email", "email");
    }

    // === PasswordResetConfirmRequest 테스트 ===

    @Test
    @DisplayName("PasswordResetConfirmRequest - 유효한 입력값은 검증을 통과해야 한다")
    void passwordResetConfirmRequest_validInput_shouldPassValidation() {
        // Given
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest(
                "valid-token-12345",
                "newPassword123"
        );

        // When
        Set<ConstraintViolation<PasswordResetConfirmRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("PasswordResetConfirmRequest - 빈 토큰은 검증에 실패해야 한다")
    void passwordResetConfirmRequest_blankToken_shouldFailValidation() {
        // Given
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest(
                "",
                "newPassword123"
        );

        // When
        Set<ConstraintViolation<PasswordResetConfirmRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("token");
    }

    @Test
    @DisplayName("PasswordResetConfirmRequest - 짧은 비밀번호는 검증에 실패해야 한다")
    void passwordResetConfirmRequest_shortPassword_shouldFailValidation() {
        // Given
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest(
                "valid-token",
                "short" // 8자 미만
        );

        // When
        Set<ConstraintViolation<PasswordResetConfirmRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("newPassword");
    }

    @Test
    @DisplayName("PasswordResetConfirmRequest - 긴 비밀번호는 검증에 실패해야 한다")
    void passwordResetConfirmRequest_longPassword_shouldFailValidation() {
        // Given
        String longPassword = "a".repeat(65); // 64자 초과
        PasswordResetConfirmRequest request = new PasswordResetConfirmRequest(
                "valid-token",
                longPassword
        );

        // When
        Set<ConstraintViolation<PasswordResetConfirmRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("newPassword");
    }

    // === PasswordResetOtpVerifyRequest 테스트 ===

    @Test
    @DisplayName("PasswordResetOtpVerifyRequest - 유효한 입력값은 검증을 통과해야 한다")
    void passwordResetOtpVerifyRequest_validInput_shouldPassValidation() {
        // Given
        PasswordResetOtpVerifyRequest request = new PasswordResetOtpVerifyRequest(
                "test@example.com",
                "123456"
        );

        // When
        Set<ConstraintViolation<PasswordResetOtpVerifyRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("PasswordResetOtpVerifyRequest - 잘못된 이메일 형식은 검증에 실패해야 한다")
    void passwordResetOtpVerifyRequest_invalidEmail_shouldFailValidation() {
        // Given
        PasswordResetOtpVerifyRequest request = new PasswordResetOtpVerifyRequest(
                "invalid-email",
                "123456"
        );

        // When
        Set<ConstraintViolation<PasswordResetOtpVerifyRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("PasswordResetOtpVerifyRequest - 잘못된 OTP 형식은 검증에 실패해야 한다")
    void passwordResetOtpVerifyRequest_invalidOtp_shouldFailValidation() {
        // Given
        PasswordResetOtpVerifyRequest request = new PasswordResetOtpVerifyRequest(
                "test@example.com",
                "12345" // 5자리만
        );

        // When
        Set<ConstraintViolation<PasswordResetOtpVerifyRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("인증번호는 6자리 숫자여야 합니다.");
    }

    @Test
    @DisplayName("PasswordResetOtpVerifyRequest - 문자가 포함된 OTP는 검증에 실패해야 한다")
    void passwordResetOtpVerifyRequest_otpWithLetters_shouldFailValidation() {
        // Given
        PasswordResetOtpVerifyRequest request = new PasswordResetOtpVerifyRequest(
                "test@example.com",
                "12345a" // 문자 포함
        );

        // When
        Set<ConstraintViolation<PasswordResetOtpVerifyRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("인증번호는 6자리 숫자여야 합니다.");
    }

    // === ResendOtpRequest 테스트 ===

    @Test
    @DisplayName("ResendOtpRequest - 유효한 이메일은 검증을 통과해야 한다")
    void resendOtpRequest_validEmail_shouldPassValidation() {
        // Given
        ResendOtpRequest request = new ResendOtpRequest("test@example.com");

        // When
        Set<ConstraintViolation<ResendOtpRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("ResendOtpRequest - 잘못된 이메일 형식은 검증에 실패해야 한다")
    void resendOtpRequest_invalidEmail_shouldFailValidation() {
        // Given
        ResendOtpRequest request = new ResendOtpRequest("invalid-email");

        // When
        Set<ConstraintViolation<ResendOtpRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("ResendOtpRequest - 빈 이메일은 검증에 실패해야 한다")
    void resendOtpRequest_blankEmail_shouldFailValidation() {
        // Given
        ResendOtpRequest request = new ResendOtpRequest("");

        // When
        Set<ConstraintViolation<ResendOtpRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(2); // @NotBlank와 @Email 둘 다 실패
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsAnyOf("이메일은 필수입니다.", "이메일 형식이 올바르지 않습니다.");
    }

    // === ResendVerificationRequest 테스트 ===

    @Test
    @DisplayName("ResendVerificationRequest - 유효한 이메일은 검증을 통과해야 한다")
    void resendVerificationRequest_validEmail_shouldPassValidation() {
        // Given
        ResendVerificationRequest request = new ResendVerificationRequest("test@example.com");

        // When
        Set<ConstraintViolation<ResendVerificationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("ResendVerificationRequest - 잘못된 이메일 형식은 검증에 실패해야 한다")
    void resendVerificationRequest_invalidEmail_shouldFailValidation() {
        // Given
        ResendVerificationRequest request = new ResendVerificationRequest("invalid-email");

        // When
        Set<ConstraintViolation<ResendVerificationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("올바른 이메일 형식이 아닙니다.");
    }

    @Test
    @DisplayName("ResendVerificationRequest - 빈 이메일은 검증에 실패해야 한다")
    void resendVerificationRequest_blankEmail_shouldFailValidation() {
        // Given
        ResendVerificationRequest request = new ResendVerificationRequest("");

        // When
        Set<ConstraintViolation<ResendVerificationRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(2); // @NotBlank와 @Email 둘 다 실패
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsAnyOf("이메일은 필수입니다.", "올바른 이메일 형식이 아닙니다.");
    }

    // === VerifyCodeRequest 테스트 ===

    @Test
    @DisplayName("VerifyCodeRequest - 유효한 입력값은 검증을 통과해야 한다")
    void verifyCodeRequest_validInput_shouldPassValidation() {
        // Given
        VerifyCodeRequest request = new VerifyCodeRequest(
                "test@example.com",
                "123456"
        );

        // When
        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("VerifyCodeRequest - 잘못된 이메일 형식은 검증에 실패해야 한다")
    void verifyCodeRequest_invalidEmail_shouldFailValidation() {
        // Given
        VerifyCodeRequest request = new VerifyCodeRequest(
                "invalid-email",
                "123456"
        );

        // When
        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("VerifyCodeRequest - 잘못된 인증번호 형식은 검증에 실패해야 한다")
    void verifyCodeRequest_invalidVerificationCode_shouldFailValidation() {
        // Given
        VerifyCodeRequest request = new VerifyCodeRequest(
                "test@example.com",
                "12345" // 5자리만
        );

        // When
        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("인증번호는 6자리 숫자여야 합니다.");
    }

    @Test
    @DisplayName("VerifyCodeRequest - 문자가 포함된 인증번호는 검증에 실패해야 한다")
    void verifyCodeRequest_verificationCodeWithLetters_shouldFailValidation() {
        // Given
        VerifyCodeRequest request = new VerifyCodeRequest(
                "test@example.com",
                "12345a" // 문자 포함
        );

        // When
        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("인증번호는 6자리 숫자여야 합니다.");
    }

    @Test
    @DisplayName("VerifyCodeRequest - 빈 인증번호는 검증에 실패해야 한다")
    void verifyCodeRequest_blankVerificationCode_shouldFailValidation() {
        // Given
        VerifyCodeRequest request = new VerifyCodeRequest(
                "test@example.com",
                ""
        );

        // When
        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(2); // @NotBlank와 @Pattern 둘 다 실패
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsAnyOf("인증번호는 필수 입력 값입니다.", "인증번호는 6자리 숫자여야 합니다.");
    }
}