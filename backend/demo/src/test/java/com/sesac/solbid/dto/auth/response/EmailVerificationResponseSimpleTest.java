package com.sesac.solbid.dto.auth.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EmailVerificationResponse Record Simple Test
 */
class EmailVerificationResponseSimpleTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testStaticFactoryMethod() {
        // given
        String email = "test@example.com";
        String message = "Email verification completed.";

        // when
        EmailVerificationResponse response = EmailVerificationResponse.success(email, message);

        // then
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.message()).isEqualTo(message);
    }

    @Test
    void testJsonSerialization() throws Exception {
        // given
        EmailVerificationResponse response = EmailVerificationResponse.success(
                "test@example.com", 
                "Email verification completed."
        );

        // when
        String json = objectMapper.writeValueAsString(response);

        // then
        assertThat(json).contains("\"email\":\"test@example.com\"");
        assertThat(json).contains("\"message\":\"Email verification completed.\"");
    }

    @Test
    void testJsonDeserialization() throws Exception {
        // given
        String json = "{\"email\":\"test@example.com\",\"message\":\"Email verification completed.\"}";

        // when
        EmailVerificationResponse response = objectMapper.readValue(json, EmailVerificationResponse.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.message()).isEqualTo("Email verification completed.");
    }
}