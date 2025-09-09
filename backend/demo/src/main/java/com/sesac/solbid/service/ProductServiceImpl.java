package com.sesac.solbid.service;

import com.sesac.solbid.domain.Product;
import com.sesac.solbid.domain.ProductImage;
import com.sesac.solbid.domain.User;
import com.sesac.solbid.dto.product.request.ProductCreateRequest;
import com.sesac.solbid.exception.CustomException;
import com.sesac.solbid.exception.ErrorCode;
import com.sesac.solbid.mapper.ProductImageMapper;
import com.sesac.solbid.mapper.ProductMapper;
import com.sesac.solbid.repository.ProductRepository;
import com.sesac.solbid.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private static final String S3_KEY_PREFIX = "products/";

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;           // MapStruct
    private final ProductImageMapper productImageMapper; // MapStruct

    /**
     * 상품 등록
     * - 검증: 썸네일 ≤ 1, sortOrder 중복 금지, S3 key prefix
     * - 처리: DTO → 엔티티 매핑(MapStruct) 후 저장
     * @throws CustomException 인증/검증 실패 시
     */
    @Override
    @Transactional
    public Long create(Long sellerId, ProductCreateRequest req) {
        // 판매자 조회
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

        // 이미지 검증
        validateImages(req);

        // 매핑 (Product 엔티티 생성)
        Product product = productMapper.toEntity(req, seller);

        // 이미지 엔티티 연결
        for (var dto : req.images()) {
            ProductImage entity = productImageMapper.toEntity(dto, product);
            product.getProductImages().add(entity);
        }

        // 저장
        Long id = productRepository.save(product).getProductId();
        log.info("Product created: id={} sellerId={} name={}", id, sellerId, req.name());
        return id;
    }

    //디버깅 로그
    private void validateImages(ProductCreateRequest req) {
        if (req.images() == null || req.images().isEmpty()) {
            log.warn("images is null or empty");
            throw new CustomException(ErrorCode.INVALID_IMAGE_KEY);
        }

        log.debug("validateImages start size={} imgs={}", req.size(), req.images().size());

        long thumbs = req.images().stream()
                .filter(ProductCreateRequest.ImageDto::isThumbnail)
                .count();
        if (thumbs > 1) {
            log.warn("thumbnail duplicated: thumbs={}", thumbs);
            throw new CustomException(ErrorCode.THUMBNAIL_DUPLICATED);
        }

        Set<Integer> orders = new HashSet<>();
        for (var img : req.images()) {
            if (!orders.add(img.sortOrder())) {
                log.warn("sortOrder duplicated: {}", img.sortOrder());
                throw new CustomException(ErrorCode.SORT_ORDER_DUPLICATED);
            }
            if (img.filePath() == null || !img.filePath().startsWith(S3_KEY_PREFIX)) {
                log.warn("invalid image key: {}", img.filePath());
                throw new CustomException(ErrorCode.INVALID_IMAGE_KEY);
            }
        }
    }

}
