package com.sesac.solbid.dto.auth.response;

import com.sesac.solbid.domain.enums.UserType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LoginSuccessResponse Record 변환 테스트
 */
class LoginSuccessResponseTest {

    @Test
    void testRecordConstructorWithBasicTypes() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";
        String nickname = "testuser";
        String userType = "USER";
        String provider = "google";
        boolean requiresNickname = false;

        // When
        LoginSuccessResponse response = new LoginSuccessResponse(
                userId, email, nickname, userType, provider, requiresNickname
        );

        // Then
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.nickname()).isEqualTo(nickname);
        assertThat(response.userType()).isEqualTo(userType);
        assertThat(response.provider()).isEqualTo(provider);
        assertThat(response.requiresNickname()).isEqualTo(requiresNickname);
    }

    @Test
    void testRecordConstructorWithObjectTypeConversion() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";
        String nickname = "testuser";
        UserType userTypeEnum = UserType.USER; // Object type that needs toString() conversion
        String provider = "google";
        boolean requiresNickname = false;

        // When
        LoginSuccessResponse response = new LoginSuccessResponse(
                userId, email, nickname, userTypeEnum, provider, requiresNickname
        );

        // Then
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.nickname()).isEqualTo(nickname);
        assertThat(response.userType()).isEqualTo("USER"); // Should be converted to String
        assertThat(response.provider()).isEqualTo(provider);
        assertThat(response.requiresNickname()).isEqualTo(requiresNickname);
    }

    @Test
    void testRecordConstructorWithNullObjectType() {
        // Given
        Long userId = 1L;
        String email = "test@example.com";
        String nickname = "testuser";
        Object userType = null; // null Object should remain null
        String provider = "google";
        boolean requiresNickname = false;

        // When
        LoginSuccessResponse response = new LoginSuccessResponse(
                userId, email, nickname, userType, provider, requiresNickname
        );

        // Then
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.nickname()).isEqualTo(nickname);
        assertThat(response.userType()).isNull(); // Should remain null
        assertThat(response.provider()).isEqualTo(provider);
        assertThat(response.requiresNickname()).isEqualTo(requiresNickname);
    }

    @Test
    void testRecordEqualsAndHashCode() {
        // Given
        LoginSuccessResponse response1 = new LoginSuccessResponse(
                1L, "test@example.com", "testuser", "USER", "google", false
        );
        LoginSuccessResponse response2 = new LoginSuccessResponse(
                1L, "test@example.com", "testuser", "USER", "google", false
        );
        LoginSuccessResponse response3 = new LoginSuccessResponse(
                2L, "test@example.com", "testuser", "USER", "google", false
        );

        // Then
        assertThat(response1).isEqualTo(response2);
        assertThat(response1).isNotEqualTo(response3);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        assertThat(response1.hashCode()).isNotEqualTo(response3.hashCode());
    }

    @Test
    void testRecordToString() {
        // Given
        LoginSuccessResponse response = new LoginSuccessResponse(
                1L, "test@example.com", "testuser", "USER", "google", false
        );

        // When
        String toString = response.toString();

        // Then
        assertThat(toString).contains("LoginSuccessResponse");
        assertThat(toString).contains("userId=1");
        assertThat(toString).contains("email=test@example.com");
        assertThat(toString).contains("nickname=testuser");
        assertThat(toString).contains("userType=USER");
        assertThat(toString).contains("provider=google");
        assertThat(toString).contains("requiresNickname=false");
    }
}