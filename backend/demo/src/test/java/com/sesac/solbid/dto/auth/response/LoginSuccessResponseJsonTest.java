package com.sesac.solbid.dto.auth.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.solbid.domain.enums.UserType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LoginSuccessResponse Record JSON 직렬화/역직렬화 테스트
 */
class LoginSuccessResponseJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testJsonSerialization() throws Exception {
        // Given
        LoginSuccessResponse response = new LoginSuccessResponse(
                1L, "test@example.com", "testuser", "USER", "google", false
        );

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertThat(json).contains("\"userId\":1");
        assertThat(json).contains("\"email\":\"test@example.com\"");
        assertThat(json).contains("\"nickname\":\"testuser\"");
        assertThat(json).contains("\"userType\":\"USER\"");
        assertThat(json).contains("\"provider\":\"google\"");
        assertThat(json).contains("\"requiresNickname\":false");
    }

    @Test
    void testJsonDeserialization() throws Exception {
        // Given
        String json = "{\"userId\":1,\"email\":\"test@example.com\",\"nickname\":\"testuser\"," +
                      "\"userType\":\"USER\",\"provider\":\"google\",\"requiresNickname\":false}";

        // When
        LoginSuccessResponse response = objectMapper.readValue(json, LoginSuccessResponse.class);

        // Then
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.nickname()).isEqualTo("testuser");
        assertThat(response.userType()).isEqualTo("USER");
        assertThat(response.provider()).isEqualTo("google");
        assertThat(response.requiresNickname()).isFalse();
    }

    @Test
    void testJsonSerializationWithObjectTypeConversion() throws Exception {
        // Given - Using the Object constructor that converts enum to string
        LoginSuccessResponse response = new LoginSuccessResponse(
                1L, "test@example.com", "testuser", UserType.ADMIN, "google", true
        );

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertThat(json).contains("\"userType\":\"ADMIN\""); // Should be converted to string
        assertThat(json).contains("\"requiresNickname\":true");
    }

    @Test
    void testJsonSerializationWithNullUserType() throws Exception {
        // Given
        LoginSuccessResponse response = new LoginSuccessResponse(
                1L, "test@example.com", "testuser", (Object) null, "google", false
        );

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertThat(json).contains("\"userType\":null");
    }

    @Test
    void testRoundTripSerialization() throws Exception {
        // Given
        LoginSuccessResponse original = new LoginSuccessResponse(
                1L, "test@example.com", "testuser", UserType.USER, "google", false
        );

        // When
        String json = objectMapper.writeValueAsString(original);
        LoginSuccessResponse deserialized = objectMapper.readValue(json, LoginSuccessResponse.class);

        // Then
        assertThat(deserialized).isEqualTo(original);
        assertThat(deserialized.userType()).isEqualTo("USER"); // Should be string after conversion
    }
}