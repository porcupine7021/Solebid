package com.sesac.solbid.repository;

import com.sesac.solbid.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long> {
    boolean existsByIdempotencyKey(String idempotencyKey);
}
