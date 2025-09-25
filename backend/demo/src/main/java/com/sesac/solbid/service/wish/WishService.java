package com.sesac.solbid.service.wish;

import java.util.List;

import com.sesac.solbid.dto.wish.response.WishAuctionResponse;

public interface WishService {

    void addWish(Long userId, Long auctionEventId);

    void removeWish(Long userId, Long auctionEventId);

    List<WishAuctionResponse> getWishes(Long userId);
}
