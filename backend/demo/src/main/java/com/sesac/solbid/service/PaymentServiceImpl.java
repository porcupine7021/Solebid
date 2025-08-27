package com.sesac.solbid.service;

import com.sesac.solbid.domain.Payments;
import com.sesac.solbid.domain.User;
import com.sesac.solbid.domain.enums.PaymentStatus;
import com.sesac.solbid.dto.request.PaymentPrepareRequest;
import com.sesac.solbid.dto.response.PaymentPrepareResponse;
import com.sesac.solbid.dto.response.PortOnePaymentResponse;
import com.sesac.solbid.repository.PaymentsRepository;
import com.sesac.solbid.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PortOneService portOneService; // 결제 승인 요청에 사용
    private final WebClient portOneWebClient;    // 사용 계획 없으면 제거 가능
    private final UserRepository userRepository;
    private final PaymentsRepository paymentsRepository;

    @Value("${portone.base-url}")
    private String portoneBaseUrl;


    /** 결제 준비 */
    @Override
    @Transactional
    public PaymentPrepareResponse preparePayment(PaymentPrepareRequest request) {
        String orderId = UUID.randomUUID().toString();
        String redirectUrl = request.getRedirectUrl();

        // TODO: 실제 로그인 연동되면 인증 사용자 사용
        User dummyUser = userRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("테스트용 유저가 없습니다."));

        log.info("[결제 준비] orderId={}, amount={}, method={}, redirectUrl={}",
                orderId, request.getAmount(), request.getPaymentMethod(), redirectUrl);

        Payments payment = Payments.builder()
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .orderId(orderId)
                .paymentStatus(PaymentStatus.WAITING)
                .convertedPoint(0)
                .charged(false)
                .user(dummyUser)
                .build();

        paymentsRepository.save(payment);

        log.info("[결제 준비 완료] orderId={} → redirectUrl={}", orderId, redirectUrl);
        return new PaymentPrepareResponse(orderId, redirectUrl);
    }

    /** 결제 승인(성공) */
    @Override
    @Transactional
    public String handlePaymentSuccess(String impUid, String accessToken) {
        log.info("[결제 승인 요청] impUid={}", impUid);

        // PortOne 승인
        PortOnePaymentResponse.PaymentData paymentData =
                portOneService.approvePayment(impUid, accessToken);

        int amount = paymentData.getAmount();

        Payments payment = paymentsRepository.findByOrderId(paymentData.getOrderId())
                .orElseThrow(() -> new IllegalStateException("해당 주문이 없습니다. orderId=" + paymentData.getOrderId()));

        payment.approve(impUid, amount);

        User user = payment.getUser();

        log.info("[포인트 전환 완료] userId={}, amount={}, orderId={}",
                user.getUserId(), amount, paymentData.getOrderId());

        return "결제 승인 및 포인트 적립 완료";
    }
}
