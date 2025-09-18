package com.sesac.solbid.dto.auth.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EmailVerificationResponse Record 테스트
 */
class EmailVerificationResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("정적 팩토리 메서드로 EmailVerificationResponse 생성 테스트")
    void testStaticFactoryMethod() {
        // given
        String email = "test@example.com";
        String message = "이메일 인증이 완료되었습니다.";

        // when
        EmailVerificationResponse response = EmailVerificationResponse.success(email, message);

        // then
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.message()).isEqualTo(message);
    }

    @Test
    @DisplayName("Record 생성자로 EmailVerificationResponse 생성 테스트")
    void testRecordConstructor() {
        // given
        String email = "test@example.com";
        String message = "이메일 인증이 완료되었습니다.";

        // when
        EmailVerificationResponse response = new EmailVerificationResponse(email, message);

        // then
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.message()).isEqualTo(message);
    }

    @Test
    @DisplayName("JSON 직렬화 테스트")
    void testJsonSerialization() throws Exception {
        // given
        EmailVerificationResponse response = EmailVerificationResponse.success(
                "test@example.com", 
                "이메일 인증이 완료되었습니다."
        );

        // when
        String json = objectMapper.writeValueAsString(response);

        // then
        assertThat(json).contains("\"email\":\"test@example.com\"");
        assertThat(json).contains("\"message\":\"이메일 인증이 완료되었습니다.\"");
    }

    @Test
    @DisplayName("JSON 역직렬화 테스트")
    void testJsonDeserialization() throws Exception {
        // given
        String json = "{\"email\":\"test@example.com\",\"message\":\"이메일 인증이 완료되었습니다.\"}";

        // when
        EmailVerificationResponse response = objectMapper.readValue(json, EmailVerificationResponse.class);

        // then
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo("test@example.com");
        assertThat(response.message()).isEqualTo("이메일 인증이 완료되었습니다.");
    }

    @Test
    @DisplayName("Record의 equals와 hashCode 테스트")
    void testEqualsAndHashCode() {
        // given
        EmailVerificationResponse response1 = EmailVerificationResponse.success(
                "test@example.com", 
                "이메일 인증이 완료되었습니다."
        );
        EmailVerificationResponse response2 = EmailVerificationResponse.success(
                "test@example.com", 
                "이메일 인증이 완료되었습니다."
        );
        EmailVerificationResponse response3 = EmailVerificationResponse.success(
                "different@example.com", 
                "이메일 인증이 완료되었습니다."
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
        EmailVerificationResponse response = EmailVerificationResponse.success(
                "test@example.com", 
                "이메일 인증이 완료되었습니다."
        );

        // when
        String toString = response.toString();

        // then
        assertThat(toString).contains("EmailVerificationResponse");
        assertThat(toString).contains("email=test@example.com");
        assertThat(toString).contains("message=이메일 인증이 완료되었습니다.");
    }
}