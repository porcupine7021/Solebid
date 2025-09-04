package com.sesac.solbid.domain;
import com.sesac.solbid.domain.enums.TransEnum;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "point_transaction")
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "trans_enum", nullable = false, length = 20)
    private TransEnum transEnum; // HOLD/RELEASE/CAPTURE/REFUND/...

    // 변화량(±). 예: HOLD -10000, RELEASE +10000
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    // 변화 후 잔액
    @Column(name = "balance_after", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(length = 225)
    private String description;

    // 결제/경매 연동키 (선택)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payments payment;

    @Column(name = "auction_event_id")
    private Long auctionEventId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    @Builder
    public PointTransaction(User user,
                            TransEnum transEnum,
                            BigDecimal amount,
                            BigDecimal balanceAfter,
                            String description,
                            Payments payment,
                            Long auctionEventId) {
        this.user = user;
        this.transEnum = transEnum;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.payment = payment;
        this.auctionEventId = auctionEventId;
    }
}