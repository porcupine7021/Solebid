package com.sesac.solbid.domain;

import com.sesac.solbid.domain.baseentity.BaseEntity;
import com.sesac.solbid.domain.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    //판매자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    //이미지
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    List<WishList> wishLists = new ArrayList<>();

    // 상품 메타
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory productCategory;

    //상품 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus productStatus = ProductStatus.AVAILABLE;

    //상품 컨디션
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCondition productCondition;

    //상품 브랜드
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductBrand productBrand;

    //사이즈
    @Column(nullable = false)
    @Min(220)
    @Max(320)
    private int size; //mm 기준

    // 등록명
    @Column(nullable = false, length = 100)
    private String name;

    //상품 상세설명
    @Column(nullable = false, length = 1000)
    private String description;

    // 크롤링 메타
    @Column(length = 60)
    private String modelCode;

    @Column(length = 120)
    private String colorway;

    private LocalDate releaseDate;

    @Builder
    public Product(User seller,
                   ProductCategory productCategory,
                   ProductStatus productStatus,
                   ProductCondition productCondition,
                   ProductBrand productBrand,
                   int size,
                   String name,
                   String description,
                   String modelCode,
                   String colorway,
                   LocalDate releaseDate) {
        this.seller = seller;
        this.productCategory = productCategory;
        this.productStatus = (productStatus == null ? ProductStatus.AVAILABLE : productStatus);
        this.productCondition = productCondition;
        this.productBrand = productBrand;
        this.size = size;
        this.name = name;
        this.description = description;
        this.modelCode = modelCode;
        this.colorway = colorway;
        this.releaseDate = releaseDate;
    }
}