package com.sesac.solbid.dto.user.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Request DTO 유효성 검증 테스트")
class UserRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    // === LoginRequest 테스트 ===

    @Test
    @DisplayName("LoginRequest - 유효한 입력값은 검증을 통과해야 한다")
    void loginRequest_validInput_shouldPassValidation() {
        // Given
        LoginRequest request = new LoginRequest(
                "test@example.com",
                "password123"
        );

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("LoginRequest - 잘못된 이메일 형식은 검증에 실패해야 한다")
    void loginRequest_invalidEmail_shouldFailValidation() {
        // Given
        LoginRequest request = new LoginRequest(
                "invalid-email",
                "password123"
        );

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("LoginRequest - 빈 이메일은 검증에 실패해야 한다")
    void loginRequest_blankEmail_shouldFailValidation() {
        // Given
        LoginRequest request = new LoginRequest(
                "",
                "password123"
        );

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(2); // @NotBlank와 @Email 둘 다 실패
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsAnyOf("이메일은 필수 입력 값입니다.", "이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("LoginRequest - 빈 비밀번호는 검증에 실패해야 한다")
    void loginRequest_blankPassword_shouldFailValidation() {
        // Given
        LoginRequest request = new LoginRequest(
                "test@example.com",
                ""
        );

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("비밀번호는 필수 입력 값입니다.");
    }

    @Test
    @DisplayName("LoginRequest - null 값들은 검증에 실패해야 한다")
    void loginRequest_nullValues_shouldFailValidation() {
        // Given
        LoginRequest request = new LoginRequest(null, null);

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(3); // email: @NotBlank, @Email, password: @NotBlank
        assertThat(violations).extracting(ConstraintViolation::getPropertyPath)
                .extracting(Object::toString)
                .containsOnly("email", "email", "password");
    }

    // === NicknameUpdateRequest 테스트 ===

    @Test
    @DisplayName("NicknameUpdateRequest - 유효한 닉네임은 검증을 통과해야 한다")
    void nicknameUpdateRequest_validNickname_shouldPassValidation() {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest("테스트닉네임");

        // When
        Set<ConstraintViolation<NicknameUpdateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("NicknameUpdateRequest - 2자 닉네임은 검증을 통과해야 한다")
    void nicknameUpdateRequest_twoCharNickname_shouldPassValidation() {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest("닉네");

        // When
        Set<ConstraintViolation<NicknameUpdateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("NicknameUpdateRequest - 10자 닉네임은 검증을 통과해야 한다")
    void nicknameUpdateRequest_tenCharNickname_shouldPassValidation() {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest("1234567890");

        // When
        Set<ConstraintViolation<NicknameUpdateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("NicknameUpdateRequest - 1자 닉네임은 검증에 실패해야 한다")
    void nicknameUpdateRequest_oneCharNickname_shouldFailValidation() {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest("닉");

        // When
        Set<ConstraintViolation<NicknameUpdateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("닉네임은 2자 이상 10자 이하로 입력해주세요.");
    }

    @Test
    @DisplayName("NicknameUpdateRequest - 11자 닉네임은 검증에 실패해야 한다")
    void nicknameUpdateRequest_elevenCharNickname_shouldFailValidation() {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest("12345678901");

        // When
        Set<ConstraintViolation<NicknameUpdateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("닉네임은 2자 이상 10자 이하로 입력해주세요.");
    }

    @Test
    @DisplayName("NicknameUpdateRequest - 빈 닉네임은 검증에 실패해야 한다")
    void nicknameUpdateRequest_blankNickname_shouldFailValidation() {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest("");

        // When
        Set<ConstraintViolation<NicknameUpdateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(2); // @NotBlank와 @Size 둘 다 실패
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsAnyOf("닉네임은 필수 입력 값입니다.", "닉네임은 2자 이상 10자 이하로 입력해주세요.");
    }

    @Test
    @DisplayName("NicknameUpdateRequest - null 닉네임은 검증에 실패해야 한다")
    void nicknameUpdateRequest_nullNickname_shouldFailValidation() {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest(null);

        // When
        Set<ConstraintViolation<NicknameUpdateRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("닉네임은 필수 입력 값입니다.");
    }

    // === SignupRequest 테스트 ===

    @Test
    @DisplayName("SignupRequest - 유효한 입력값은 검증을 통과해야 한다")
    void signupRequest_validInput_shouldPassValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "01012345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("SignupRequest - 잘못된 이메일 형식은 검증에 실패해야 한다")
    void signupRequest_invalidEmail_shouldFailValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "invalid-email",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "01012345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("SignupRequest - 약한 비밀번호는 검증에 실패해야 한다")
    void signupRequest_weakPassword_shouldFailValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "password", // 숫자, 특수문자 없음
                "테스트닉네임",
                "홍길동",
                "01012345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("비밀번호는 8~20자 영문 대소문자, 숫자, 특수문자를 모두 포함하세요.");
    }

    @Test
    @DisplayName("SignupRequest - 짧은 비밀번호는 검증에 실패해야 한다")
    void signupRequest_shortPassword_shouldFailValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Pass1!", // 7자
                "테스트닉네임",
                "홍길동",
                "01012345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("비밀번호는 8~20자 영문 대소문자, 숫자, 특수문자를 모두 포함하세요.");
    }

    @Test
    @DisplayName("SignupRequest - 긴 비밀번호는 검증에 실패해야 한다")
    void signupRequest_longPassword_shouldFailValidation() {
        // Given
        String longPassword = "Password123!" + "a".repeat(10); // 21자
        SignupRequest request = new SignupRequest(
                "test@example.com",
                longPassword,
                "테스트닉네임",
                "홍길동",
                "01012345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("비밀번호는 8~20자 영문 대소문자, 숫자, 특수문자를 모두 포함하세요.");
    }

    @Test
    @DisplayName("SignupRequest - 공백이 포함된 비밀번호는 검증에 실패해야 한다")
    void signupRequest_passwordWithSpace_shouldFailValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Pass word123!", // 공백 포함
                "테스트닉네임",
                "홍길동",
                "01012345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("비밀번호는 8~20자 영문 대소문자, 숫자, 특수문자를 모두 포함하세요.");
    }

    @Test
    @DisplayName("SignupRequest - 잘못된 전화번호 형식은 검증에 실패해야 한다")
    void signupRequest_invalidPhoneNumber_shouldFailValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "010-1234-5678" // 하이픈 포함
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("전화번호 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("SignupRequest - 유효한 010 전화번호는 검증을 통과해야 한다")
    void signupRequest_valid010PhoneNumber_shouldPassValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "01012345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("SignupRequest - 유효한 011 전화번호는 검증을 통과해야 한다")
    void signupRequest_valid011PhoneNumber_shouldPassValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "01112345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("SignupRequest - 유효한 016 전화번호는 검증을 통과해야 한다")
    void signupRequest_valid016PhoneNumber_shouldPassValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "01612345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("SignupRequest - 유효한 017 전화번호는 검증을 통과해야 한다")
    void signupRequest_valid017PhoneNumber_shouldPassValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "01712345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("SignupRequest - 유효한 018 전화번호는 검증을 통과해야 한다")
    void signupRequest_valid018PhoneNumber_shouldPassValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "01812345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("SignupRequest - 유효한 019 전화번호는 검증을 통과해야 한다")
    void signupRequest_valid019PhoneNumber_shouldPassValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "01912345678"
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("SignupRequest - 잘못된 전화번호 시작번호는 검증에 실패해야 한다")
    void signupRequest_invalidPhonePrefix_shouldFailValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "01512345678" // 015는 유효하지 않음
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("전화번호 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("SignupRequest - 짧은 전화번호는 검증에 실패해야 한다")
    void signupRequest_shortPhoneNumber_shouldFailValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "0101234567" // 10자리
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("전화번호 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("SignupRequest - 긴 전화번호는 검증에 실패해야 한다")
    void signupRequest_longPhoneNumber_shouldFailValidation() {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!",
                "테스트닉네임",
                "홍길동",
                "010123456789" // 12자리
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("전화번호 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("SignupRequest - 빈 필수 필드들은 검증에 실패해야 한다")
    void signupRequest_blankRequiredFields_shouldFailValidation() {
        // Given
        SignupRequest request = new SignupRequest("", "", "", "", "");

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(8); // 각 필드마다 @NotBlank + 추가 검증
        assertThat(violations).extracting(ConstraintViolation::getPropertyPath)
                .extracting(Object::toString)
                .containsOnly("email", "email", "password", "password", "nickname", "nickname", "name", "phone", "phone");
    }

    @Test
    @DisplayName("SignupRequest - null 필드들은 검증에 실패해야 한다")
    void signupRequest_nullFields_shouldFailValidation() {
        // Given
        SignupRequest request = new SignupRequest(null, null, null, null, null);

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(6); // 각 필드마다 @NotBlank
        assertThat(violations).extracting(ConstraintViolation::getPropertyPath)
                .extracting(Object::toString)
                .containsOnly("email", "email", "password", "nickname", "name", "phone");
    }
}