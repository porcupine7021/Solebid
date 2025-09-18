package com.sesac.solbid.dto.auth.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EmailVerificationResponse Record JSON 호환성 테스트
 */
class EmailVerificationResponseJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Record가 기존과 동일한 JSON 구조를 생성하는지 테스트")
    void testJsonStructureCompatibility() throws Exception {
        // given
        EmailVerificationResponse response = EmailVerificationResponse.success(
                "te**@example.com", 
                "이메일 인증이 완료되었습니다."
        );

        // when
        String json = objectMapper.writeValueAsString(response);

        // then - 기존 Lombok 클래스와 동일한 JSON 구조 확인
        assertThat(json).isEqualTo("{\"email\":\"te**@example.com\",\"message\":\"이메일 인증이 완료되었습니다.\"}");
    }

    @Test
    @DisplayName("기존 JSON 형식을 Record로 역직렬화할 수 있는지 테스트")
    void testBackwardCompatibility() throws Exception {
        // given - 기존 Lombok 클래스가 생성했을 JSON 형식
        String existingJson = "{\"email\":\"te**@example.com\",\"message\":\"이메일 인증이 완료되었습니다.\"}";

        // when
        EmailVerificationResponse response = objectMapper.readValue(existingJson, EmailVerificationResponse.class);

        // then
        assertThat(response.email()).isEqualTo("te**@example.com");
        assertThat(response.message()).isEqualTo("이메일 인증이 완료되었습니다.");
    }

    @Test
    @DisplayName("정적 팩토리 메서드로 생성한 객체의 JSON 직렬화 테스트")
    void testStaticFactoryMethodJsonSerialization() throws Exception {
        // given
        EmailVerificationResponse response = EmailVerificationResponse.success(
                "user@test.com", 
                "인증 완료"
        );

        // when
        String json = objectMapper.writeValueAsString(response);
        EmailVerificationResponse deserializedResponse = objectMapper.readValue(json, EmailVerificationResponse.class);

        // then
        assertThat(deserializedResponse.email()).isEqualTo("user@test.com");
        assertThat(deserializedResponse.message()).isEqualTo("인증 완료");
        assertThat(deserializedResponse).isEqualTo(response);
    }

    @Test
    @DisplayName("null 값 처리 테스트")
    void testNullValueHandling() throws Exception {
        // given
        EmailVerificationResponse response = new EmailVerificationResponse(null, "메시지만 있음");

        // when
        String json = objectMapper.writeValueAsString(response);
        EmailVerificationResponse deserializedResponse = objectMapper.readValue(json, EmailVerificationResponse.class);

        // then
        assertThat(deserializedResponse.email()).isNull();
        assertThat(deserializedResponse.message()).isEqualTo("메시지만 있음");
    }
}