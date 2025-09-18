package com.sesac.solbid.validation;

import com.sesac.solbid.dto.auth.request.CallbackRequest;
import com.sesac.solbid.dto.auth.request.VerifyCodeRequest;
import com.sesac.solbid.dto.user.request.LoginRequest;
import com.sesac.solbid.dto.user.request.SignupRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Record DTO 유효성 검증 기본 테스트
 * 
 * 이 테스트는 Record로 변환된 DTO들이 Jakarta Validation과 정상적으로 작동하는지 확인합니다.
 * 
 * 테스트 범위:
 * 1. Jakarta validation 어노테이션이 Record에서 정상 동작
 * 2. 기존과 동일한 오류 메시지 생성
 * 3. 복잡한 유효성 검증 패턴 (정규식, 길이 제한 등) 동작
 */
@DisplayName("Record DTO 유효성 검증 기본 테스트")
class SimpleRecordValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("CallbackRequest - 유효한 데이터는 검증을 통과해야 한다")
    void callbackRequest_validData_shouldPassValidation() {
        // Given
        CallbackRequest request = new CallbackRequest("valid-code", "valid-state");

        // When
        Set<ConstraintViolation<CallbackRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("CallbackRequest - 빈 필드는 적절한 오류 메시지와 함께 검증에 실패해야 한다")
    void callbackRequest_blankFields_shouldFailWithCorrectMessages() {
        // Given
        CallbackRequest request = new CallbackRequest("", "");

        // When
        Set<ConstraintViolation<CallbackRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "인증 코드는 필수입니다.",
                        "State 파라미터는 필수입니다."
                );
    }

    @Test
    @DisplayName("VerifyCodeRequest - 유효한 데이터는 검증을 통과해야 한다")
    void verifyCodeRequest_validData_shouldPassValidation() {
        // Given
        VerifyCodeRequest request = new VerifyCodeRequest("test@example.com", "123456");

        // When
        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("VerifyCodeRequest - 잘못된 이메일과 OTP는 적절한 오류 메시지와 함께 검증에 실패해야 한다")
    void verifyCodeRequest_invalidData_shouldFailWithCorrectMessages() {
        // Given
        VerifyCodeRequest request = new VerifyCodeRequest("invalid-email", "12345");

        // When
        Set<ConstraintViolation<VerifyCodeRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(2);
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "이메일 형식이 올바르지 않습니다.",
                        "인증번호는 6자리 숫자여야 합니다."
                );
    }

    @Test
    @DisplayName("LoginRequest - 유효한 데이터는 검증을 통과해야 한다")
    void loginRequest_validData_shouldPassValidation() {
        // Given
        LoginRequest request = new LoginRequest("test@example.com", "password123");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("LoginRequest - 잘못된 이메일은 적절한 오류 메시지와 함께 검증에 실패해야 한다")
    void loginRequest_invalidEmail_shouldFailWithCorrectMessage() {
        // Given
        LoginRequest request = new LoginRequest("invalid-email", "password123");

        // When
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("이메일 형식이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("SignupRequest - 유효한 데이터는 검증을 통과해야 한다")
    void signupRequest_validData_shouldPassValidation() {
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
    @DisplayName("SignupRequest - 복잡한 비밀번호 패턴 검증이 정상 동작해야 한다")
    void signupRequest_passwordPattern_shouldWorkCorrectly() {
        // Given - 약한 비밀번호 (숫자, 특수문자 없음)
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "password",
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
    @DisplayName("SignupRequest - 전화번호 패턴 검증이 정상 동작해야 한다")
    void signupRequest_phonePattern_shouldWorkCorrectly() {
        // Given - 잘못된 전화번호 형식
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
    @DisplayName("SignupRequest - 여러 필드가 잘못된 경우 모든 오류가 반환되어야 한다")
    void signupRequest_multipleErrors_shouldReturnAllErrors() {
        // Given - 모든 필드가 잘못된 경우
        SignupRequest request = new SignupRequest(
                "invalid-email",    // 잘못된 이메일
                "weak",            // 약한 비밀번호
                "닉",              // 짧은 닉네임
                "",                // 빈 이름
                "010-1234-5678"    // 잘못된 전화번호
        );

        // When
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(5); // 각 필드마다 하나씩 오류
        assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder(
                        "이메일 형식이 올바르지 않습니다.",
                        "비밀번호는 8~20자 영문 대소문자, 숫자, 특수문자를 모두 포함하세요.",
                        "닉네임은 2자 이상 10자 이하로 입력해주세요.",
                        "이름은 필수 입력 값입니다.",
                        "전화번호 형식이 올바르지 않습니다."
                );
    }

    @Test
    @DisplayName("Record DTO의 불변성 - 필드 값이 생성 후 변경되지 않아야 한다")
    void recordDto_immutability_shouldBeImmutable() {
        // Given
        CallbackRequest request = new CallbackRequest("original-code", "original-state");

        // When & Then - Record는 불변이므로 setter 메서드가 없어야 함
        assertThat(request.code()).isEqualTo("original-code");
        assertThat(request.state()).isEqualTo("original-state");
        
        // Record의 필드는 final이므로 변경할 수 없음을 확인
        // (컴파일 타임에 setter 메서드가 존재하지 않음)
    }

    @Test
    @DisplayName("Record DTO의 equals/hashCode - 동일한 값을 가진 Record는 같아야 한다")
    void recordDto_equalsHashCode_shouldWorkCorrectly() {
        // Given
        CallbackRequest request1 = new CallbackRequest("code", "state");
        CallbackRequest request2 = new CallbackRequest("code", "state");
        CallbackRequest request3 = new CallbackRequest("different-code", "state");

        // When & Then
        assertThat(request1).isEqualTo(request2);
        assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        assertThat(request1).isNotEqualTo(request3);
    }

    @Test
    @DisplayName("Record DTO의 toString - 모든 필드 값이 포함되어야 한다")
    void recordDto_toString_shouldIncludeAllFields() {
        // Given
        CallbackRequest request = new CallbackRequest("test-code", "test-state");

        // When
        String toString = request.toString();

        // Then
        assertThat(toString).contains("test-code");
        assertThat(toString).contains("test-state");
        assertThat(toString).contains("CallbackRequest");
    }
}