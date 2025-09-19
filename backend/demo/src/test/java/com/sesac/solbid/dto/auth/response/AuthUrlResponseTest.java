package com.sesac.solbid.dto.auth.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AuthUrlResponse Record 테스트
 */
class AuthUrlResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("정적 팩토리 메서드로 AuthUrlResponse 생성 테스트")
    void testStaticFactoryMethod() {
        // given
        String authUrl = "https://oauth.provider.com/auth";
        String state = "random-state-123";
        String provider = "google";

        // when
        AuthUrlResponse response = AuthUrlResponse.of(authUrl, state, provider);

        // then
        assertThat(response).isNotNull();
        assertThat(response.authUrl()).isEqualTo(authUrl);
        assertThat(response.state()).isEqualTo(state);
        assertThat(response.provider()).isEqualTo(provider);
    }

    @Test
    @DisplayName("Record 생성자로 AuthUrlResponse 생성 테스트")
    void testRecordConstructor() {
        // given
        String authUrl = "https://oauth.provider.com/auth";
        String state = "random-state-123";
        String provider = "google";

        // when
        AuthUrlResponse response = new AuthUrlResponse(authUrl, state, provider);

        // then
        assertThat(response).isNotNull();
        assertThat(response.authUrl()).isEqualTo(authUrl);
        assertThat(response.state()).isEqualTo(state);
        assertThat(response.provider()).isEqualTo(provider);
    }

    @Test
    @DisplayName("JSON 직렬화 테스트")
    void testJsonSerialization() throws Exception {
        // given
        AuthUrlResponse response = AuthUrlResponse.of(
                "https://oauth.provider.com/auth",
                "random-state-123",
                "google"
        );

        // when
        String json = objectMapper.writeValueAsString(response);

        // then
        assertThat(json).contains("\"authUrl\":\"https://oauth.provider.com/auth\"");
        assertThat(json).contains("\"state\":\"random-state-123\"");
        assertThat(json).contains("\"provider\":\"google\"");
    }

    @Test
    @DisplayName("JSON 역직렬화 테스트")
    void testJsonDeserialization() throws Exception {
        // given
        String json = "{\"authUrl\":\"https://oauth.provider.com/auth\",\"state\":\"random-state-123\",\"provider\":\"google\"}";

        // when
        AuthUrlResponse response = objectMapper.readValue(json, AuthUrlResponse.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.authUrl()).isEqualTo("https://oauth.provider.com/auth");
        assertThat(response.state()).isEqualTo("random-state-123");
        assertThat(response.provider()).isEqualTo("google");
    }

    @Test
    @DisplayName("Record의 equals와 hashCode 테스트")
    void testEqualsAndHashCode() {
        // given
        AuthUrlResponse response1 = AuthUrlResponse.of(
                "https://oauth.provider.com/auth",
                "random-state-123",
                "google"
        );
        AuthUrlResponse response2 = AuthUrlResponse.of(
                "https://oauth.provider.com/auth",
                "random-state-123",
                "google"
        );
        AuthUrlResponse response3 = AuthUrlResponse.of(
                "https://oauth.provider.com/auth",
                "different-state",
                "google"
        );

        // then
        assertThat(response1).isEqualTo(response2);
        assertThat(response1).isNotEqualTo(response3);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        assertThat(response1.hashCode()).isNotEqualTo(response3.hashCode());
    }

    @Test
    @DisplayName("Record의 toString 테스트")
    void testToString() {
        // given
        AuthUrlResponse response = AuthUrlResponse.of(
                "https://oauth.provider.com/auth",
                "random-state-123",
                "google"
        );

        // when
        String toString = response.toString();

        // then
        assertThat(toString).contains("AuthUrlResponse");
        assertThat(toString).contains("authUrl=https://oauth.provider.com/auth");
        assertThat(toString).contains("state=random-state-123");
        assertThat(toString).contains("provider=google");
    }
}