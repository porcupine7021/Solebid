package com.sesac.solbid.dto.user.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LoginRequest Record JSON 직렬화/역직렬화 테스트
 */
class LoginRequestJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testJsonSerialization() throws Exception {
        // Given
        LoginRequest request = new LoginRequest(
                "test@example.com",
                "password123"
        );

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"email\":\"test@example.com\"");
        assertThat(json).contains("\"password\":\"password123\"");
    }

    @Test
    void testJsonDeserialization() throws Exception {
        // Given
        String json = "{\"email\":\"test@example.com\",\"password\":\"password123\"}";

        // When
        LoginRequest request = objectMapper.readValue(json, LoginRequest.class);

        // Then
        assertThat(request.email()).isEqualTo("test@example.com");
        assertThat(request.password()).isEqualTo("password123");
    }

    @Test
    void testJsonSerializationWithSpecialCharacters() throws Exception {
        // Given
        LoginRequest request = new LoginRequest(
                "user+test@example.com",
                "P@ssw0rd!@#$%"
        );

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"email\":\"user+test@example.com\"");
        assertThat(json).contains("\"password\":\"P@ssw0rd!@#$%\"");
    }

    @Test
    void testJsonSerializationWithNullValues() throws Exception {
        // Given
        LoginRequest request = new LoginRequest(null, null);

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"email\":null");
        assertThat(json).contains("\"password\":null");
    }

    @Test
    void testRoundTripSerialization() throws Exception {
        // Given
        LoginRequest original = new LoginRequest(
                "test@example.com",
                "password123"
        );

        // When
        String json = objectMapper.writeValueAsString(original);
        LoginRequest deserialized = objectMapper.readValue(json, LoginRequest.class);

        // Then
        assertThat(deserialized).isEqualTo(original);
        assertThat(deserialized.email()).isEqualTo(original.email());
        assertThat(deserialized.password()).isEqualTo(original.password());
    }

    @Test
    void testJsonStructureCompatibility() throws Exception {
        // Given - 기존 JSON 구조와 호환성 확인
        String legacyJson = "{\"email\":\"legacy@example.com\",\"password\":\"legacypass\"}";

        // When
        LoginRequest request = objectMapper.readValue(legacyJson, LoginRequest.class);

        // Then - 기존 JSON 구조를 정상적으로 읽을 수 있어야 함
        assertThat(request.email()).isEqualTo("legacy@example.com");
        assertThat(request.password()).isEqualTo("legacypass");
    }
}