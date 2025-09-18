package com.sesac.solbid.dto.user.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SignupResponse Record JSON 직렬화/역직렬화 테스트
 */
class SignupResponseJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testJsonSerialization() throws Exception {
        // Given
        SignupResponse response = new SignupResponse(
                1L,
                "test@example.com",
                "testuser",
                true,
                "회원가입이 완료되었습니다.",
                "로그인하여 서비스를 이용하세요."
        );

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertThat(json).contains("\"userId\":1");
        assertThat(json).contains("\"email\":\"test@example.com\"");
        assertThat(json).contains("\"nickname\":\"testuser\"");
        assertThat(json).contains("\"emailVerified\":true");
        assertThat(json).contains("\"message\":\"회원가입이 완료되었습니다.\"");
        assertThat(json).contains("\"nextStep\":\"로그인하여 서비스를 이용하세요.\"");
    }

    @Test
    void testJsonDeserialization() throws Exception {
        // Given
        String json = "{\"userId\":1,\"email\":\"test@example.com\",\"nickname\":\"testuser\"," +
                      "\"emailVerified\":true,\"message\":\"회원가입이 완료되었습니다.\"," +
                      "\"nextStep\":\"로그인하여 서비스를 이용하세요.\"}";

        // When
        SignupResponse response = objectMapper.readValue(json, SignupResponse.class);

        // Then
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.nickname()).isEqualTo("testuser");
        assertThat(response.emailVerified()).isTrue();
        assertThat(response.message()).isEqualTo("회원가입이 완료되었습니다.");
        assertThat(response.nextStep()).isEqualTo("로그인하여 서비스를 이용하세요.");
    }

    @Test
    void testJsonSerializationWithEmailNotVerified() throws Exception {
        // Given
        SignupResponse response = new SignupResponse(
                2L,
                "unverified@example.com",
                "unverifieduser",
                false,
                "회원가입이 완료되었습니다.",
                "이메일 인증을 완료한 후 로그인할 수 있습니다."
        );

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertThat(json).contains("\"emailVerified\":false");
        assertThat(json).contains("\"nextStep\":\"이메일 인증을 완료한 후 로그인할 수 있습니다.\"");
    }

    @Test
    void testJsonSerializationWithNullValues() throws Exception {
        // Given
        SignupResponse response = new SignupResponse(
                null,
                null,
                null,
                null,
                null,
                null
        );

        // When
        String json = objectMapper.writeValueAsString(response);

        // Then
        assertThat(json).contains("\"userId\":null");
        assertThat(json).contains("\"email\":null");
        assertThat(json).contains("\"emailVerified\":null");
        assertThat(json).contains("\"message\":null");
    }

    @Test
    void testRoundTripSerialization() throws Exception {
        // Given
        SignupResponse original = new SignupResponse(
                1L,
                "test@example.com",
                "testuser",
                true,
                "회원가입이 완료되었습니다.",
                "로그인하여 서비스를 이용하세요."
        );

        // When
        String json = objectMapper.writeValueAsString(original);
        SignupResponse deserialized = objectMapper.readValue(json, SignupResponse.class);

        // Then
        assertThat(deserialized).isEqualTo(original);
        assertThat(deserialized.userId()).isEqualTo(original.userId());
        assertThat(deserialized.emailVerified()).isEqualTo(original.emailVerified());
        assertThat(deserialized.message()).isEqualTo(original.message());
        assertThat(deserialized.nextStep()).isEqualTo(original.nextStep());
    }
}