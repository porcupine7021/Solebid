package com.sesac.solbid.repository.wish;

import com.sesac.solbid.domain.User;
import com.sesac.solbid.domain.Wish;
import com.sesac.solbid.domain.AuctionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {

    Optional<Wish> findByUserAndAuctionEvent(User user, AuctionEvent auctionEvent);

    List<Wish> findByUser(User user);

    // ID 기반 멱등/삭제용
    boolean existsByUser_UserIdAndAuctionEvent_AuctionEventId(Long userId, Long auctionEventId);
    void deleteByUser_UserIdAndAuctionEvent_AuctionEventId(Long userId, Long auctionEventId);

    void deleteByUserAndAuctionEvent(User user, AuctionEvent auctionEvent);
}
