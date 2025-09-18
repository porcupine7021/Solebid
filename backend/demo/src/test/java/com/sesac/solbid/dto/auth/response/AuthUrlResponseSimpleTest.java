package com.sesac.solbid.dto.auth.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AuthUrlResponse Record Simple Test
 */
class AuthUrlResponseSimpleTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
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
}