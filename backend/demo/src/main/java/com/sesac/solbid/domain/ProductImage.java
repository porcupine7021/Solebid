package com.sesac.solbid.domain;

import com.sesac.solbid.domain.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="product_image" ,
        uniqueConstraints = {
                // 정렬 충돌 방지 (선택): 같은 상품에서 같은 sort_order 금지
                @UniqueConstraint(name="uk_product_sort", columnNames = {"product_id", "sort_order"})
        }
)
public class ProductImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    //이 이미지가 등록된 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", nullable = false)
    private Product product;

    //이미지 주소 S3 Key :products/{productId}/{uuid}.jpg
    @Column(name = "file_path", nullable = false, length = 512)
    private String filePath;

    //원본 파일명
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    //이미지 정렬 순번
    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    //썸네일 유무
    @Column(name = "is_thumbnail", nullable = false)
    private boolean isThumbnail;

    @Builder
    public ProductImage(Product product, String filePath, String fileName, Integer sortOrder, boolean isThumbnail) {
        this.product = product;
        this.filePath = filePath;
        this.fileName = fileName;
        this.sortOrder = sortOrder;
        this.isThumbnail = isThumbnail;
    }

    // 런타임 URL 조합 (직접 S3 or CloudFront)
    @Transient
    public String getPublicUrl(String baseUrl) {
        // baseUrl 예: https://cdn.yourapp.com/  또는  https://{bucket}.s3.ap-northeast-2.amazonaws.com/
        if (baseUrl.endsWith("/")) return baseUrl + filePath;
        return baseUrl + "/" + filePath;
    }
}
