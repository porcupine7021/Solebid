package com.sesac.solbid.domain;

import com.sesac.solbid.domain.enums.ProviderType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 소셜 로그인 정보 엔티티 클래스
 * <p>
 * 사용자의 소셜 로그인 연동 정보를 관리하는 엔티티입니다.
 * Google, Kakao 등의 OAuth2 제공자와의 연동 정보를 저장합니다.
 * </p>
 */
@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name="social_login")
public class SocialLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long socialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ProviderType provider;

    @Column(length = 100, nullable = false)
    private String providerId;

    // 제공자 토큰 저장: Google revoke 자동화를 위해 사용
    @Column(length = 2048)
    private String providerAccessToken;

    @Column(length = 2048)
    private String providerRefreshToken;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    /**
     * 소셜 로그인 엔티티 생성자
     * 
     * @param user 연동할 사용자 엔티티
     * @param provider 소셜 로그인 제공자 (GOOGLE, KAKAO 등)
     * @param providerId 제공자에서 발급한 사용자 고유 ID
     */
    @Builder
    public SocialLogin(User user, ProviderType provider, String providerId) {
        this.user = user;
        this.provider = provider;
        this.providerId = providerId;
    }

    /**
     * 소셜 로그인 제공자의 토큰 정보를 업데이트합니다.
     * 
     * @param accessToken 제공자 액세스 토큰
     * @param refreshToken 제공자 리프레시 토큰
     */
    public void updateProviderTokens(String accessToken, String refreshToken) {
        this.providerAccessToken = accessToken;
        this.providerRefreshToken = refreshToken;
    }
}
