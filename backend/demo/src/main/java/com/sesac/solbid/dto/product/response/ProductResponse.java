package com.sesac.solbid.dto.product.response;

import com.sesac.solbid.domain.AuctionEvent;
import com.sesac.solbid.domain.Product;
import com.sesac.solbid.domain.ProductImage;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

public record ProductResponse(
        Long id,
        String brand,
        String name,
        String image,
        String category,
        BigDecimal currentBid,
        LocalDateTime timeLeft,
        int bidders
) {
    public static ProductResponse fromEntity(Product product) {
        String imageUrl = product
                .getProductImages()
                .stream()
                .findFirst()
                .map(ProductImage::getFilePath)
                .orElse(null);

        Optional<AuctionEvent> auction = product
                .getAuctionEvents()
                .stream()
                // .filter(e -> e.getStatus() == AuctionStatus.LIVE
                //        || e.getStatus() == AuctionStatus.READY)
                .max(Comparator.comparing(AuctionEvent::getAuctionEventId));

        BigDecimal highestBidAmount = auction
                .map(AuctionEvent::getHighestBidAmount)
                .orElse(null);

        LocalDateTime endAt = auction
                .map(AuctionEvent::getEndAt)
                .orElse(null);

        int viewCount = auction
                .map(AuctionEvent::getViewCount)
                .orElse(0);

        return new ProductResponse(
                product.getProductId(),
                product.getProductBrand().name(),
                product.getName(),
                imageUrl,
                product.getProductCategory().name(),
                highestBidAmount,
                endAt,
                viewCount
        );
    }
}
