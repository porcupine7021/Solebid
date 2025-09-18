package com.sesac.solbid.dto.user.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * NicknameUpdateRequest Record JSON 직렬화/역직렬화 테스트
 */
class NicknameUpdateRequestJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testJsonSerialization() throws Exception {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest("새닉네임");

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"nickname\":\"새닉네임\"");
    }

    @Test
    void testJsonDeserialization() throws Exception {
        // Given
        String json = "{\"nickname\":\"새닉네임\"}";

        // When
        NicknameUpdateRequest request = objectMapper.readValue(json, NicknameUpdateRequest.class);

        // Then
        assertThat(request.nickname()).isEqualTo("새닉네임");
    }

    @Test
    void testJsonSerializationWithEnglishNickname() throws Exception {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest("newuser123");

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"nickname\":\"newuser123\"");
    }

    @Test
    void testJsonSerializationWithSpecialCharacters() throws Exception {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest("닉네임_123");

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"nickname\":\"닉네임_123\"");
    }

    @Test
    void testJsonSerializationWithNullValue() throws Exception {
        // Given
        NicknameUpdateRequest request = new NicknameUpdateRequest(null);

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"nickname\":null");
    }

    @Test
    void testRoundTripSerialization() throws Exception {
        // Given
        NicknameUpdateRequest original = new NicknameUpdateRequest("테스트닉네임");

        // When
        String json = objectMapper.writeValueAsString(original);
        NicknameUpdateRequest deserialized = objectMapper.readValue(json, NicknameUpdateRequest.class);

        // Then
        assertThat(deserialized).isEqualTo(original);
        assertThat(deserialized.nickname()).isEqualTo(original.nickname());
    }

    @Test
    void testJsonStructureCompatibility() throws Exception {
        // Given - 기존 JSON 구조와 호환성 확인
        String legacyJson = "{\"nickname\":\"기존닉네임\"}";

        // When
        NicknameUpdateRequest request = objectMapper.readValue(legacyJson, NicknameUpdateRequest.class);

        // Then - 기존 JSON 구조를 정상적으로 읽을 수 있어야 함
        assertThat(request.nickname()).isEqualTo("기존닉네임");
    }

    @Test
    void testJsonSerializationWithMinMaxLength() throws Exception {
        // Given - 최소/최대 길이 테스트
        NicknameUpdateRequest minRequest = new NicknameUpdateRequest("ab");
        NicknameUpdateRequest maxRequest = new NicknameUpdateRequest("1234567890");

        // When
        String minJson = objectMapper.writeValueAsString(minRequest);
        String maxJson = objectMapper.writeValueAsString(maxRequest);

        // Then
        assertThat(minJson).contains("\"nickname\":\"ab\"");
        assertThat(maxJson).contains("\"nickname\":\"1234567890\"");
    }
}