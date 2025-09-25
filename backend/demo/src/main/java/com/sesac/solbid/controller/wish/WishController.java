package com.sesac.solbid.controller.wish;

import com.sesac.solbid.domain.User;
import com.sesac.solbid.dto.api.ApiResponse;
import com.sesac.solbid.dto.wish.response.WishAuctionResponse;
import com.sesac.solbid.exception.CustomException;
import com.sesac.solbid.exception.ErrorCode;
import com.sesac.solbid.service.wish.WishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WishAuctionResponse>>> getWishes(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        List<WishAuctionResponse> wishes = wishService.getWishes(user.getUserId());

        return ResponseEntity.ok(ApiResponse.success(wishes));
    }

    @PostMapping("/{auctionEventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addWish(@AuthenticationPrincipal User user, @PathVariable Long auctionEventId) {
        if (user == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        wishService.addWish(user.getUserId(), auctionEventId);
    }

    @DeleteMapping("/{auctionEventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeWish(@AuthenticationPrincipal User user,
                           @PathVariable Long auctionEventId) {
        if (user == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        wishService.removeWish(user.getUserId(), auctionEventId);
    }
}
