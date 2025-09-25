package com.sesac.solbid.dto.wish.response;

import com.sesac.solbid.domain.AuctionEvent;
import com.sesac.solbid.domain.Product;
import com.sesac.solbid.domain.ProductImage;
import com.sesac.solbid.domain.Wish;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

public record WishAuctionResponse(
        Long id,
        Long productId,
        String brand,
        String name,
        String image,
        String category,
        BigDecimal currentBid,
        LocalDateTime timeLeft,
        int bidders
) {

    private static final Comparator<ProductImage> IMAGE_PRIORITY = Comparator
            .comparing(ProductImage::isThumbnail, Comparator.reverseOrder())
            .thenComparing(ProductImage::getSortOrder, Comparator.nullsLast(Integer::compareTo));

    public static WishAuctionResponse from(Wish wish) {
        AuctionEvent event = wish.getAuctionEvent();
        Product product = wish.getProduct();

        Long auctionEventId = event != null ? event.getAuctionEventId() : null;
        Long productId = product != null ? product.getProductId() : null;
        String brand = product != null && product.getProductBrand() != null
                ? product.getProductBrand().name()
                : null;
        String category = product != null && product.getProductCategory() != null
                ? product.getProductCategory().name()
                : null;
        String name = product != null ? product.getName() : null;

        String image = null;
        if (product != null) {
            image = product.getProductImages()
                    .stream()
                    .sorted(IMAGE_PRIORITY)
                    .map(ProductImage::getFilePath)
                    .findFirst()
                    .orElse(null);
        }

        BigDecimal currentBid = null;
        LocalDateTime timeLeft = null;
        int bidders = 0;

        if (event != null) {
            currentBid = event.getHighestBidAmount() != null
                    ? event.getHighestBidAmount()
                    : event.getStartPrice();
            timeLeft = event.getEndAt();
            bidders = event.getViewCount();
        }

        return new WishAuctionResponse(
                auctionEventId,
                productId,
                brand,
                name,
                image,
                category,
                currentBid,
                timeLeft,
                bidders
        );
    }
}
