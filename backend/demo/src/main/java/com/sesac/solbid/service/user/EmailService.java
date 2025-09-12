package com.sesac.solbid.service.user;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.sesac.solbid.exception.CustomException;
import com.sesac.solbid.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Slf4j
@Service
public class EmailService {

    private static final String APPLICATION_NAME = "SoleBid";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final int MAX_RETRY_ATTEMPTS = 3;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${google.mail.refresh-token}")
    private String googleRefreshToken;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    /**
     * 비밀번호 재설정 이메일 전송 (기존 기능 유지)
     */
    @Retryable(
        retryFor = {IOException.class, GeneralSecurityException.class, MessagingException.class},
        maxAttempts = MAX_RETRY_ATTEMPTS,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendPasswordResetMail(String to, String link) {
        try {
            Gmail service = createGmailService();
            
            String subject = "[SoleBid] 비밀번호 재설정 안내";
            String htmlContent = createPasswordResetEmailTemplate(link);
            
            sendEmail(service, to, subject, htmlContent);
            log.info("비밀번호 재설정 메일 발송 성공: {}", maskEmail(to));
            
        } catch (Exception e) {
            log.error("비밀번호 재설정 메일 발송 실패: {}", maskEmail(to), e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    /**
     * 이메일 인증 메일 전송
     */
    @Retryable(
        retryFor = {IOException.class, GeneralSecurityException.class, MessagingException.class},
        maxAttempts = MAX_RETRY_ATTEMPTS,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendVerificationEmail(String to, String token) {
        try {
            Gmail service = createGmailService();
            
            String subject = "[SoleBid] 이메일 인증 안내";
            String verificationLink = frontendBaseUrl + "/auth/verify-email?token=" + token;
            String htmlContent = createVerificationEmailTemplate(verificationLink);
            
            sendEmail(service, to, subject, htmlContent);
            log.info("이메일 인증 메일 발송 성공: {}", maskEmail(to));
            
        } catch (Exception e) {
            log.error("이메일 인증 메일 발송 실패: {}", maskEmail(to), e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    /**
     * 마지막 알림 이메일 전송 (계정 삭제 2시간 전)
     */
    @Retryable(
        retryFor = {IOException.class, GeneralSecurityException.class, MessagingException.class},
        maxAttempts = MAX_RETRY_ATTEMPTS,
        backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendFinalReminderEmail(String to, String token) {
        try {
            Gmail service = createGmailService();
            
            String subject = "[SoleBid] 계정 삭제 예정 - 마지막 알림";
            String verificationLink = frontendBaseUrl + "/auth/verify-email?token=" + token;
            String htmlContent = createFinalReminderEmailTemplate(verificationLink);
            
            sendEmail(service, to, subject, htmlContent);
            log.info("마지막 알림 이메일 발송 성공: {}", maskEmail(to));
            
        } catch (Exception e) {
            log.error("마지막 알림 이메일 발송 실패: {}", maskEmail(to), e);
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }

    /**
     * Gmail 서비스 클라이언트 생성
     */
    private Gmail createGmailService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(googleClientId, googleClientSecret)
                .build()
                .setRefreshToken(googleRefreshToken);

        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * 이메일 전송 공통 메서드
     */
    private void sendEmail(Gmail service, String to, String subject, String htmlContent) 
            throws MessagingException, IOException {
        MimeMessage mimeMessage = createEmail(to, fromEmail, subject, htmlContent);
        Message message = createMessageWithEmail(mimeMessage);
        service.users().messages().send("me", message).execute();
    }

    /**
     * 비밀번호 재설정 이메일 템플릿
     */
    private String createPasswordResetEmailTemplate(String link) {
        return """
            <div style="max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="background-color: #f8f9fa; padding: 20px; text-align: center; border-bottom: 3px solid #007bff;">
                    <h1 style="color: #007bff; margin: 0;">SoleBid</h1>
                </div>
                <div style="padding: 30px 20px;">
                    <h2 style="color: #333; margin-bottom: 20px;">비밀번호 재설정 안내</h2>
                    <p style="margin-bottom: 20px;">안녕하세요!</p>
                    <p style="margin-bottom: 20px;">비밀번호 재설정을 요청하셨습니다. 아래 버튼을 클릭하여 새로운 비밀번호를 설정해주세요.</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #007bff; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold;">비밀번호 재설정</a>
                    </div>
                    <p style="margin-bottom: 10px; color: #666; font-size: 14px;">⚠️ 이 링크는 15분 후에 만료됩니다.</p>
                    <p style="margin-bottom: 20px; color: #666; font-size: 14px;">만약 비밀번호 재설정을 요청하지 않으셨다면, 이 이메일을 무시해주세요.</p>
                </div>
                <div style="background-color: #f8f9fa; padding: 20px; text-align: center; border-top: 1px solid #dee2e6; font-size: 12px; color: #666;">
                    <p>© 2025 SoleBid. All rights reserved.</p>
                </div>
            </div>
            """.formatted(link);
    }

    /**
     * 이메일 인증 메일 템플릿
     */
    private String createVerificationEmailTemplate(String verificationLink) {
        return """
            <div style="max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="background-color: #f8f9fa; padding: 20px; text-align: center; border-bottom: 3px solid #28a745;">
                    <h1 style="color: #28a745; margin: 0;">SoleBid</h1>
                </div>
                <div style="padding: 30px 20px;">
                    <h2 style="color: #333; margin-bottom: 20px;">이메일 인증 안내</h2>
                    <p style="margin-bottom: 20px;">안녕하세요!</p>
                    <p style="margin-bottom: 20px;">SoleBid 회원가입을 완료하기 위해 이메일 인증이 필요합니다. 아래 버튼을 클릭하여 이메일 인증을 완료해주세요.</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #28a745; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold;">이메일 인증하기</a>
                    </div>
                    <p style="margin-bottom: 10px; color: #666; font-size: 14px;">⚠️ 이 링크는 24시간 후에 만료됩니다.</p>
                    <p style="margin-bottom: 10px; color: #666; font-size: 14px;">📧 이메일이 스팸 폴더에 있을 수 있으니 확인해주세요.</p>
                    <p style="margin-bottom: 20px; color: #666; font-size: 14px;">이메일 인증을 완료하지 않으면 24시간 후 계정이 자동으로 삭제됩니다.</p>
                </div>
                <div style="background-color: #f8f9fa; padding: 20px; text-align: center; border-top: 1px solid #dee2e6; font-size: 12px; color: #666;">
                    <p>© 2025 SoleBid. All rights reserved.</p>
                </div>
            </div>
            """.formatted(verificationLink);
    }

    /**
     * 마지막 알림 이메일 템플릿
     */
    private String createFinalReminderEmailTemplate(String verificationLink) {
        return """
            <div style="max-width: 600px; margin: 0 auto; font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="background-color: #f8f9fa; padding: 20px; text-align: center; border-bottom: 3px solid #dc3545;">
                    <h1 style="color: #dc3545; margin: 0;">SoleBid</h1>
                </div>
                <div style="padding: 30px 20px;">
                    <h2 style="color: #dc3545; margin-bottom: 20px;">⚠️ 계정 삭제 예정 - 마지막 알림</h2>
                    <p style="margin-bottom: 20px;">안녕하세요!</p>
                    <p style="margin-bottom: 20px; color: #dc3545; font-weight: bold;">귀하의 SoleBid 계정이 2시간 후에 자동으로 삭제될 예정입니다.</p>
                    <p style="margin-bottom: 20px;">이메일 인증을 완료하지 않아 계정 삭제가 예정되어 있습니다. 계정을 유지하려면 지금 즉시 이메일 인증을 완료해주세요.</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #dc3545; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; display: inline-block; font-weight: bold;">지금 인증하기</a>
                    </div>
                    <div style="background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 5px; padding: 15px; margin: 20px 0;">
                        <p style="margin: 0; color: #856404; font-size: 14px;">
                            <strong>중요:</strong> 이 링크를 클릭하면 계정이 즉시 활성화되어 삭제가 취소됩니다.
                        </p>
                    </div>
                    <p style="margin-bottom: 20px; color: #666; font-size: 14px;">만약 계정을 삭제하려고 의도하셨다면, 이 이메일을 무시해주세요.</p>
                </div>
                <div style="background-color: #f8f9fa; padding: 20px; text-align: center; border-top: 1px solid #dee2e6; font-size: 12px; color: #666;">
                    <p>© 2025 SoleBid. All rights reserved.</p>
                </div>
            </div>
            """.formatted(verificationLink);
    }

    /**
     * MimeMessage 객체 생성
     */
    private MimeMessage createEmail(String to, String from, String subject, String bodyText) 
            throws MessagingException, IOException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from, "SoleBid", "UTF-8"));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setContent(bodyText, "text/html; charset=utf-8");
        return email;
    }

    /**
     * MimeMessage를 Gmail API Message 형식으로 변환
     */
    private Message createMessageWithEmail(MimeMessage emailMessage) 
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailMessage.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    /**
     * 이메일 주소 마스킹
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return "****";
        String[] parts = email.split("@");
        if (parts[0].length() <= 2) return "**@" + parts[1];
        return parts[0].substring(0, 2) + "****@" + parts[1];
    }
}