package com.sesac.solbid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesac.solbid.dto.auth.request.CallbackRequest;
import com.sesac.solbid.dto.auth.request.PasswordResetRequest;
import com.sesac.solbid.dto.user.request.LoginRequest;
import com.sesac.solbid.dto.user.request.NicknameUpdateRequest;
import com.sesac.solbid.dto.user.request.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Record DTO와 REST 엔드포인트의 호환성 테스트
 * 
 * 테스트 범위:
 * - REST API 요청/응답에서 Record DTO 직렬화/역직렬화 호환성
 * - 기존 API 계약 유지 확인
 * - JSON 형식 호환성 검증
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class RecordDtoRestEndpointCompatibilityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("OAuth2 인증 URL 생성 API가 AuthUrlResponse Record를 정상적으로 직렬화한다")
    void authUrlResponse_serializesCorrectlyInRestApi() throws Exception {
        // when & then
        mockMvc.perform(get("/api/auth/oauth2/google/url"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.authUrl").exists())
                .andExpect(jsonPath("$.data.state").exists())
                .andExpect(jsonPath("$.data.provider").value("google"));
    }

    @Test
    @DisplayName("이메일 중복 확인 API가 정상적으로 동작한다")
    void emailAvailabilityCheck_worksCorrectlyWithRecordDto() throws Exception {
        // when & then
        mockMvc.perform(get("/api/users/email/available")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.available").exists());
    }

    @Test
    @DisplayName("닉네임 가용성 확인 API가 NicknameAvailabilityResponse를 정상적으로 직렬화한다")
    void nicknameAvailabilityResponse_serializesCorrectlyInRestApi() throws Exception {
        // when & then
        mockMvc.perform(get("/api/users/nickname/available")
                        .param("nickname", "testuser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.available").exists());
    }

    @Test
    @DisplayName("Record DTO의 JSON 직렬화가 기존 형식과 호환된다")
    void recordDto_jsonSerialization_maintainsCompatibility() throws Exception {
        // given
        SignupRequest signupRequest = new SignupRequest(
                "test@example.com",
                "Password123!",
                "testuser",
                "Test User",
                "01012345678"
        );

        // when
        String json = objectMapper.writeValueAsString(signupRequest);

        // then - JSON 구조가 예상된 형식과 일치하는지 확인
        assertThat(json).contains("\"email\":\"test@example.com\"");
        assertThat(json).contains("\"password\":\"Password123!\"");
        assertThat(json).contains("\"nickname\":\"testuser\"");
        assertThat(json).contains("\"name\":\"Test User\"");
        assertThat(json).contains("\"phone\":\"01012345678\"");
    }

    @Test
    @DisplayName("Record DTO의 JSON 역직렬화가 기존 형식과 호환된다")
    void recordDto_jsonDeserialization_maintainsCompatibility() throws Exception {
        // given - 기존 JSON 형식
        String json = """
                {
                    "email": "test@example.com",
                    "password": "Password123!",
                    "nickname": "testuser",
                    "name": "Test User",
                    "phone": "01012345678"
                }
                """;

        // when
        SignupRequest signupRequest = objectMapper.readValue(json, SignupRequest.class);

        // then
        assertThat(signupRequest.email()).isEqualTo("test@example.com");
        assertThat(signupRequest.password()).isEqualTo("Password123!");
        assertThat(signupRequest.nickname()).isEqualTo("testuser");
        assertThat(signupRequest.name()).isEqualTo("Test User");
        assertThat(signupRequest.phone()).isEqualTo("01012345678");
    }

    @Test
    @DisplayName("CallbackRequest Record의 JSON 역직렬화가 정상 동작한다")
    void callbackRequest_jsonDeserialization_worksCorrectly() throws Exception {
        // given
        String json = """
                {
                    "code": "auth-code-123",
                    "state": "state-456"
                }
                """;

        // when
        CallbackRequest request = objectMapper.readValue(json, CallbackRequest.class);

        // then
        assertThat(request.code()).isEqualTo("auth-code-123");
        assertThat(request.state()).isEqualTo("state-456");
    }

    @Test
    @DisplayName("LoginRequest Record의 JSON 역직렬화가 정상 동작한다")
    void loginRequest_jsonDeserialization_worksCorrectly() throws Exception {
        // given
        String json = """
                {
                    "email": "test@example.com",
                    "password": "Password123!"
                }
                """;

        // when
        LoginRequest request = objectMapper.readValue(json, LoginRequest.class);

        // then
        assertThat(request.email()).isEqualTo("test@example.com");
        assertThat(request.password()).isEqualTo("Password123!");
    }

    @Test
    @DisplayName("NicknameUpdateRequest Record의 JSON 역직렬화가 정상 동작한다")
    void nicknameUpdateRequest_jsonDeserialization_worksCorrectly() throws Exception {
        // given
        String json = """
                {
                    "nickname": "newnickname"
                }
                """;

        // when
        NicknameUpdateRequest request = objectMapper.readValue(json, NicknameUpdateRequest.class);

        // then
        assertThat(request.nickname()).isEqualTo("newnickname");
    }

    @Test
    @DisplayName("PasswordResetRequest Record의 JSON 역직렬화가 정상 동작한다")
    void passwordResetRequest_jsonDeserialization_worksCorrectly() throws Exception {
        // given
        String json = """
                {
                    "email": "test@example.com"
                }
                """;

        // when
        PasswordResetRequest request = objectMapper.readValue(json, PasswordResetRequest.class);

        // then
        assertThat(request.email()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Record DTO의 불변성이 JSON 처리 과정에서 보장된다")
    void recordDto_immutability_maintainedDuringJsonProcessing() throws Exception {
        // given
        SignupRequest original = new SignupRequest(
                "test@example.com",
                "Password123!",
                "testuser",
                "Test User",
                "01012345678"
        );

        // when - JSON 직렬화/역직렬화
        String json = objectMapper.writeValueAsString(original);
        SignupRequest deserialized = objectMapper.readValue(json, SignupRequest.class);

        // then - 원본과 역직렬화된 객체가 동일한 값을 가짐
        assertThat(deserialized).isEqualTo(original);
        assertThat(deserialized.email()).isEqualTo(original.email());
        assertThat(deserialized.nickname()).isEqualTo(original.nickname());
    }
}