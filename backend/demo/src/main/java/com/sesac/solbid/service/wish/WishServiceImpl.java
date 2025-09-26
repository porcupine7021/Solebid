package com.sesac.solbid.service.wish;

import com.sesac.solbid.domain.AuctionEvent;
import com.sesac.solbid.domain.User;
import com.sesac.solbid.domain.Wish;
import com.sesac.solbid.dto.wish.response.WishAuctionResponse;
import com.sesac.solbid.exception.CustomException;
import com.sesac.solbid.exception.ErrorCode;
import com.sesac.solbid.repository.auction.AuctionEventRepository;
import com.sesac.solbid.repository.user.UserRepository;
import com.sesac.solbid.repository.wish.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final AuctionEventRepository auctionEventRepository;

    @Override
    public List<WishAuctionResponse> getWishes(Long userId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        return wishRepository
                .findByUser(user)
                .stream()
                .map(WishAuctionResponse::from)
                .toList();
    }

    @Override
    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    public void addWish(Long userId, Long auctionEventId) {
        // 멱등: 이미 존재하면 조용히 성공 처리
        if (wishRepository.existsByUser_UserIdAndAuctionEvent_AuctionEventId(userId, auctionEventId)) {
            return;
        }

        User user = userRepository.getReferenceById(userId);
        AuctionEvent event = auctionEventRepository.getReferenceById(auctionEventId);

        try {
            wishRepository.saveAndFlush(Wish.builder()
                    .user(user)
                    .auctionEvent(event)
                    .build());
        } catch (DataIntegrityViolationException e) {
            // 동시 클릭 등으로 UNIQUE 충돌 → 이미 존재로 간주하고 성공 처리
            return;
        }
    }

    @Override
    @Transactional
    public void removeWish(Long userId, Long auctionEventId) {
        if (userId == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        AuctionEvent auctionEvent = auctionEventRepository
                .findById(auctionEventId)
                .orElseThrow(() -> new CustomException(ErrorCode.AUCTION_NOT_FOUND));

        wishRepository
                .findByUserAndAuctionEvent(user, auctionEvent)
                .ifPresent(wishRepository::delete);
    }
}
