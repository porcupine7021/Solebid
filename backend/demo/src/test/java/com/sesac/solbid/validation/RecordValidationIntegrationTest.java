package com.sesac.solbid.validation;

import com.sesac.solbid.dto.auth.request.*;
import com.sesac.solbid.dto.user.request.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Record DTO 유효성 검증 통합 테스트")
class RecordValidationIntegrationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // === 성공 케이스 테스트 ===

    @Test
    @DisplayName("모든 Record DTO - 유효한 데이터는 검증을 통과해야 한다")
    void allRecordDtos_validData_shouldPassValidation() {
        // Auth Request DTOs
        CallbackRequest callbackRequest = new CallbackRequest("valid-code", "valid-state");
        assertThat(validator.validate(callbackRequest)).isEmpty();

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest("test@example.com");
        assertThat(validator.validate(passwordResetRequest)).isEmpty();

        PasswordResetConfirmRequest passwordResetConfirmRequest = new PasswordResetConfirmRequest("valid-token", "password123");
        assertThat(validator.validate(passwordResetConfirmRequest)).isEmpty();

        PasswordResetOtpVerifyRequest passwordResetOtpVerifyRequest = new PasswordResetOtpVerifyRequest("test@example.com", "123456");
        assertThat(validator.validate(passwordResetOtpVerifyRequest)).isEmpty();

        PasswordResetVerifyRequest passwordResetVerifyRequest = new PasswordResetVerifyRequest("test@example.com", "123456", "newPassword123");
        assertThat(validator.validate(passwordResetVerifyRequest)).isEmpty();

        ResendOtpRequest resendOtpRequest = new ResendOtpRequest("test@example.com");
        assertThat(validator.validate(resendOtpRequest)).isEmpty();

        ResendVerificationRequest resendVerificationRequest = new ResendVerificationRequest("test@example.com");
        assertThat(validator.validate(resendVerificationRequest)).isEmpty();

        VerifyCodeRequest verifyCodeRequest = new VerifyCodeRequest("test@example.com", "123456");
        assertThat(validator.validate(verifyCodeRequest)).isEmpty();

        // User Request DTOs
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
        assertThat(validator.validate(loginRequest)).isEmpty();

        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("테스트닉네임");
        assertThat(validator.validate(nicknameUpdateRequest)).isEmpty();

        SignupRequest signupRequest = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "01012345678"
        );
        assertThat(validator.validate(signupRequest)).isEmpty();
    }

    // === 실패 케이스 테스트 ===

    @Test
    @DisplayName("이메일 검증 - 잘못된 이메일 형식은 모든 DTO에서 실패해야 한다")
    void emailValidation_invalidEmail_shouldFailInAllDtos() {
        String invalidEmail = "invalid-email";

        // Auth DTOs with email validation
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest(invalidEmail);
        Set<ConstraintViolation<PasswordResetRequest>> violations1 = validator.validate(passwordResetRequest);
        assertThat(violations1).isNotEmpty();

        PasswordResetOtpVerifyRequest passwordResetOtpVerifyRequest = new PasswordResetOtpVerifyRequest(invalidEmail, "123456");
        Set<ConstraintViolation<PasswordResetOtpVerifyRequest>> violations2 = validator.validate(passwordResetOtpVerifyRequest);
        assertThat(violations2).hasSize(1);
        assertThat(violations2.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");

        PasswordResetVerifyRequest passwordResetVerifyRequest = new PasswordResetVerifyRequest(invalidEmail, "123456", "newPassword123");
        Set<ConstraintViolation<PasswordResetVerifyRequest>> violations3 = validator.validate(passwordResetVerifyRequest);
        assertThat(violations3).hasSize(1);
        assertThat(violations3.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");

        ResendOtpRequest resendOtpRequest = new ResendOtpRequest(invalidEmail);
        Set<ConstraintViolation<ResendOtpRequest>> violations4 = validator.validate(resendOtpRequest);
        assertThat(violations4).hasSize(1);
        assertThat(violations4.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");

        ResendVerificationRequest resendVerificationRequest = new ResendVerificationRequest(invalidEmail);
        Set<ConstraintViolation<ResendVerificationRequest>> violations5 = validator.validate(resendVerificationRequest);
        assertThat(violations5).hasSize(1);
        assertThat(violations5.iterator().next().getMessage()).isEqualTo("올바른 이메일 형식이 아닙니다.");

        VerifyCodeRequest verifyCodeRequest = new VerifyCodeRequest(invalidEmail, "123456");
        Set<ConstraintViolation<VerifyCodeRequest>> violations6 = validator.validate(verifyCodeRequest);
        assertThat(violations6).hasSize(1);
        assertThat(violations6.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");

        // User DTOs with email validation
        LoginRequest loginRequest = new LoginRequest(invalidEmail, "password123");
        Set<ConstraintViolation<LoginRequest>> violations7 = validator.validate(loginRequest);
        assertThat(violations7).hasSize(1);
        assertThat(violations7.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");

        SignupRequest signupRequest = new SignupRequest(invalidEmail, "Password123!", "테스트닉네임", "홍길동", "01012345678");
        Set<ConstraintViolation<SignupRequest>> violations8 = validator.validate(signupRequest);
        assertThat(violations8).hasSize(1);
        assertThat(violations8.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("OTP 검증 - 잘못된 OTP 형식은 관련 DTO에서 실패해야 한다")
    void otpValidation_invalidOtp_shouldFailInRelatedDtos() {
        String invalidOtp = "12345"; // 5자리만

        PasswordResetOtpVerifyRequest passwordResetOtpVerifyRequest = new PasswordResetOtpVerifyRequest("test@example.com", invalidOtp);
        Set<ConstraintViolation<PasswordResetOtpVerifyRequest>> violations1 = validator.validate(passwordResetOtpVerifyRequest);
        assertThat(violations1).hasSize(1);
        assertThat(violations1.iterator().next().getMessage()).isEqualTo("인증번호는 6자리 숫자여야 합니다.");

        PasswordResetVerifyRequest passwordResetVerifyRequest = new PasswordResetVerifyRequest("test@example.com", invalidOtp, "newPassword123");
        Set<ConstraintViolation<PasswordResetVerifyRequest>> violations2 = validator.validate(passwordResetVerifyRequest);
        assertThat(violations2).hasSize(1);
        assertThat(violations2.iterator().next().getMessage()).isEqualTo("인증번호는 6자리 숫자여야 합니다.");

        VerifyCodeRequest verifyCodeRequest = new VerifyCodeRequest("test@example.com", invalidOtp);
        Set<ConstraintViolation<VerifyCodeRequest>> violations3 = validator.validate(verifyCodeRequest);
        assertThat(violations3).hasSize(1);
        assertThat(violations3.iterator().next().getMessage()).isEqualTo("인증번호는 6자리 숫자여야 합니다.");
    }

    @Test
    @DisplayName("비밀번호 검증 - SignupRequest의 복잡한 비밀번호 패턴이 정상 동작해야 한다")
    void passwordValidation_complexPattern_shouldWorkCorrectly() {
        // 유효한 비밀번호들
        String[] validPasswords = {
                "Password123!",
                "Abc123@def",
                "Test1234#",
                "MyPass99$"
        };

        for (String validPassword : validPasswords) {
            SignupRequest signupRequest = new SignupRequest("test@example.com", validPassword, "테스트닉네임", "홍길동", "01012345678");
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
            assertThat(violations).isEmpty();
        }

        // 잘못된 비밀번호들
        String[] invalidPasswords = {
                "password",        // 숫자, 특수문자 없음
                "PASSWORD123!",    // 소문자 없음
                "password123!",    // 대문자 없음
                "Password!",       // 숫자 없음
                "Password123",     // 특수문자 없음
                "Pass1!",          // 너무 짧음 (7자)
                "Pass word123!",   // 공백 포함
                "Password123!" + "a".repeat(10) // 너무 김 (21자)
        };

        for (String invalidPassword : invalidPasswords) {
            SignupRequest signupRequest = new SignupRequest("test@example.com", invalidPassword, "테스트닉네임", "홍길동", "01012345678");
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage())
                    .isEqualTo("비밀번호는 8~20자 영문 대소문자, 숫자, 특수문자를 모두 포함하세요.");
        }
    }

    @Test
    @DisplayName("전화번호 검증 - SignupRequest의 전화번호 패턴이 정상 동작해야 한다")
    void phoneValidation_koreanPhonePattern_shouldWorkCorrectly() {
        // 유효한 전화번호들
        String[] validPhones = {
                "01012345678",   // 010
                "01112345678",   // 011
                "01612345678",   // 016
                "01712345678",   // 017
                "01812345678",   // 018
                "01912345678"    // 019
        };

        for (String validPhone : validPhones) {
            SignupRequest signupRequest = new SignupRequest("test@example.com", "Password123!", "테스트닉네임", "홍길동", validPhone);
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
            assertThat(violations).isEmpty();
        }

        // 잘못된 전화번호들
        String[] invalidPhones = {
                "010-1234-5678", // 하이픈 포함
                "01512345678",   // 015는 유효하지 않음
                "0101234567",    // 10자리 (너무 짧음)
                "010123456789",  // 12자리 (너무 김)
                "02012345678",   // 020으로 시작
                "1012345678"     // 0으로 시작하지 않음
        };

        for (String invalidPhone : invalidPhones) {
            SignupRequest signupRequest = new SignupRequest("test@example.com", "Password123!", "테스트닉네임", "홍길동", invalidPhone);
            Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("전화번호 형식이 올바르지 않습니다.");
        }
    }

    @Test
    @DisplayName("닉네임 검증 - 길이 제한이 정상 동작해야 한다")
    void nicknameValidation_lengthConstraints_shouldWorkCorrectly() {
        // 유효한 닉네임들 (2-10자)
        String[] validNicknames = {
                "닉네",           // 2자
                "테스트닉네임",      // 5자
                "1234567890"      // 10자
        };

        for (String validNickname : validNicknames) {
            NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest(validNickname);
            Set<ConstraintViolation<NicknameUpdateRequest>> violations = validator.validate(nicknameUpdateRequest);
            assertThat(violations).isEmpty();

            SignupRequest signupRequest = new SignupRequest("test@example.com", "Password123!", validNickname, "홍길동", "01012345678");
            Set<ConstraintViolation<SignupRequest>> signupViolations = validator.validate(signupRequest);
            assertThat(signupViolations).isEmpty();
        }

        // 잘못된 닉네임들
        String[] invalidNicknames = {
                "닉",             // 1자 (너무 짧음)
                "12345678901"     // 11자 (너무 김)
        };

        for (String invalidNickname : invalidNicknames) {
            NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest(invalidNickname);
            Set<ConstraintViolation<NicknameUpdateRequest>> violations = validator.validate(nicknameUpdateRequest);
            assertThat(violations).hasSize(1);
            assertThat(violations.iterator().next().getMessage()).isEqualTo("닉네임은 2자 이상 10자 이하로 입력해주세요.");

            SignupRequest signupRequest = new SignupRequest("test@example.com", "Password123!", invalidNickname, "홍길동", "01012345678");
            Set<ConstraintViolation<SignupRequest>> signupViolations = validator.validate(signupRequest);
            assertThat(signupViolations).hasSize(1);
            assertThat(signupViolations.iterator().next().getMessage()).isEqualTo("닉네임은 2자 이상 10자 이하로 입력해주세요.");
        }
    }

    @Test
    @DisplayName("필수 필드 검증 - null과 빈 문자열이 적절히 처리되어야 한다")
    void requiredFieldValidation_nullAndBlank_shouldBeHandledCorrectly() {
        // CallbackRequest - 둘 다 필수
        CallbackRequest callbackRequest1 = new CallbackRequest("", "valid-state");
        Set<ConstraintViolation<CallbackRequest>> violations1 = validator.validate(callbackRequest1);
        assertThat(violations1).hasSize(1);
        assertThat(violations1.iterator().next().getMessage()).isEqualTo("인증 코드는 필수입니다.");

        CallbackRequest callbackRequest2 = new CallbackRequest("valid-code", "");
        Set<ConstraintViolation<CallbackRequest>> violations2 = validator.validate(callbackRequest2);
        assertThat(violations2).hasSize(1);
        assertThat(violations2.iterator().next().getMessage()).isEqualTo("State 파라미터는 필수입니다.");

        // LoginRequest - 둘 다 필수
        LoginRequest loginRequest1 = new LoginRequest("", "password123");
        Set<ConstraintViolation<LoginRequest>> violations3 = validator.validate(loginRequest1);
        assertThat(violations3).hasSize(2); // @NotBlank와 @Email 둘 다 실패

        LoginRequest loginRequest2 = new LoginRequest("test@example.com", "");
        Set<ConstraintViolation<LoginRequest>> violations4 = validator.validate(loginRequest2);
        assertThat(violations4).hasSize(1);
        assertThat(violations4.iterator().next().getMessage()).isEqualTo("비밀번호는 필수 입력 값입니다.");

        // NicknameUpdateRequest - 닉네임 필수
        NicknameUpdateRequest nicknameUpdateRequest = new NicknameUpdateRequest("");
        Set<ConstraintViolation<NicknameUpdateRequest>> violations5 = validator.validate(nicknameUpdateRequest);
        assertThat(violations5).hasSize(2); // @NotBlank와 @Size 둘 다 실패
    }

    @Test
    @DisplayName("복합 검증 오류 - 여러 필드가 잘못된 경우 모든 오류가 반환되어야 한다")
    void multipleValidationErrors_shouldReturnAllErrors() {
        // SignupRequest with all invalid fields
        SignupRequest signupRequest = new SignupRequest(
                "invalid-email",    // 잘못된 이메일
                "weak",            // 약한 비밀번호
                "닉",              // 짧은 닉네임
                "",                // 빈 이름
                "010-1234-5678"    // 잘못된 전화번호 형식
        );

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(signupRequest);
        assertThat(violations).hasSize(5); // 각 필드마다 하나씩 오류

        // 각 필드의 오류 메시지 확인
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsAnyOf(
                        "이메일 형식이 올바르지 않습니다.",
                        "비밀번호는 8~20자 영문 대소문자, 숫자, 특수문자를 모두 포함하세요.",
                        "닉네임은 2자 이상 10자 이하로 입력해주세요.",
                        "이름은 필수 입력 값입니다.",
                        "전화번호 형식이 올바르지 않습니다."
                );
    }
}