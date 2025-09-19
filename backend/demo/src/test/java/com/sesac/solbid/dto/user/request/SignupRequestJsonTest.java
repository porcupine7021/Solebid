package com.sesac.solbid.dto.user.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SignupRequest Record JSON 직렬화/역직렬화 테스트
 */
class SignupRequestJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testJsonSerialization() throws Exception {
        // Given
        SignupRequest request = new SignupRequest(
                "test@example.com",
                "Password123!@#",
                "테스트유저",
                "홍길동",
                "01012345678"
        );

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"email\":\"test@example.com\"");
        assertThat(json).contains("\"password\":\"Password123!@#\"");
        assertThat(json).contains("\"nickname\":\"테스트유저\"");
        assertThat(json).contains("\"name\":\"홍길동\"");
        assertThat(json).contains("\"phone\":\"01012345678\"");
    }

    @Test
    void testJsonDeserialization() throws Exception {
        // Given
        String json = "{\"email\":\"test@example.com\",\"password\":\"Password123!@#\"," +
                      "\"nickname\":\"테스트유저\",\"name\":\"홍길동\",\"phone\":\"01012345678\"}";

        // When
        SignupRequest request = objectMapper.readValue(json, SignupRequest.class);

        // Then
        assertThat(request.email()).isEqualTo("test@example.com");
        assertThat(request.password()).isEqualTo("Password123!@#");
        assertThat(request.nickname()).isEqualTo("테스트유저");
        assertThat(request.name()).isEqualTo("홍길동");
        assertThat(request.phone()).isEqualTo("01012345678");
    }

    @Test
    void testJsonSerializationWithComplexPassword() throws Exception {
        // Given - 복잡한 비밀번호 패턴 테스트
        SignupRequest request = new SignupRequest(
                "complex@example.com",
                "C0mpl3x!P@ssw0rd#$%",
                "복잡한유저",
                "김복잡",
                "01087654321"
        );

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"password\":\"C0mpl3x!P@ssw0rd#$%\"");
        assertThat(json).contains("\"email\":\"complex@example.com\"");
    }

    @Test
    void testJsonSerializationWithDifferentPhoneFormats() throws Exception {
        // Given - 다양한 전화번호 형식 테스트
        SignupRequest request1 = new SignupRequest(
                "user1@example.com", "Password1!", "유저1", "사용자1", "01012345678"
        );
        SignupRequest request2 = new SignupRequest(
                "user2@example.com", "Password2!", "유저2", "사용자2", "01187654321"
        );

        // When
        String json1 = objectMapper.writeValueAsString(request1);
        String json2 = objectMapper.writeValueAsString(request2);

        // Then
        assertThat(json1).contains("\"phone\":\"01012345678\"");
        assertThat(json2).contains("\"phone\":\"01187654321\"");
    }

    @Test
    void testJsonSerializationWithNullValues() throws Exception {
        // Given
        SignupRequest request = new SignupRequest(null, null, null, null, null);

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"email\":null");
        assertThat(json).contains("\"password\":null");
        assertThat(json).contains("\"nickname\":null");
        assertThat(json).contains("\"name\":null");
        assertThat(json).contains("\"phone\":null");
    }

    @Test
    void testRoundTripSerialization() throws Exception {
        // Given
        SignupRequest original = new SignupRequest(
                "test@example.com",
                "Password123!@#",
                "테스트유저",
                "홍길동",
                "01012345678"
        );

        // When
        String json = objectMapper.writeValueAsString(original);
        SignupRequest deserialized = objectMapper.readValue(json, SignupRequest.class);

        // Then
        assertThat(deserialized).isEqualTo(original);
        assertThat(deserialized.email()).isEqualTo(original.email());
        assertThat(deserialized.password()).isEqualTo(original.password());
        assertThat(deserialized.nickname()).isEqualTo(original.nickname());
        assertThat(deserialized.name()).isEqualTo(original.name());
        assertThat(deserialized.phone()).isEqualTo(original.phone());
    }

    @Test
    void testJsonStructureCompatibility() throws Exception {
        // Given - 기존 JSON 구조와 호환성 확인
        String legacyJson = "{\"email\":\"legacy@example.com\",\"password\":\"LegacyPass1!\"," +
                           "\"nickname\":\"기존유저\",\"name\":\"기존사용자\",\"phone\":\"01098765432\"}";

        // When
        SignupRequest request = objectMapper.readValue(legacyJson, SignupRequest.class);

        // Then - 기존 JSON 구조를 정상적으로 읽을 수 있어야 함
        assertThat(request.email()).isEqualTo("legacy@example.com");
        assertThat(request.password()).isEqualTo("LegacyPass1!");
        assertThat(request.nickname()).isEqualTo("기존유저");
        assertThat(request.name()).isEqualTo("기존사용자");
        assertThat(request.phone()).isEqualTo("01098765432");
    }

    @Test
    void testJsonSerializationWithUnicodeCharacters() throws Exception {
        // Given - 유니코드 문자 테스트
        SignupRequest request = new SignupRequest(
                "unicode@example.com",
                "Unicode123!@#",
                "유니코드닉네임",
                "김유니코드",
                "01012345678"
        );

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertThat(json).contains("\"nickname\":\"유니코드닉네임\"");
        assertThat(json).contains("\"name\":\"김유니코드\"");
    }
}