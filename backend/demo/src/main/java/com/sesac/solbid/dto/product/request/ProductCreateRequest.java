package com.sesac.solbid.dto.product.request;

import com.sesac.solbid.domain.enums.ProductBrand;
import com.sesac.solbid.domain.enums.ProductCategory;
import com.sesac.solbid.domain.enums.ProductCondition;
import com.sesac.solbid.domain.enums.ProductStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record ProductCreateRequest(
        @NotNull ProductCategory category,
        @NotNull ProductStatus status,
        @NotNull ProductCondition condition,
        @NotNull ProductBrand brand,
        @Min(220) @Max(320) int size,                  // mm
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 1000) String description,
        @Size(max = 60) String modelCode,
        @Size(max = 120) String colorway,
        LocalDate releaseDate,
        @NotNull @Size(max = 5) List<ImageDto> images
) {
    public record ImageDto(
            @NotBlank @Size(max = 512) String filePath,
            @NotBlank @Size(max = 255) String fileName,
            @Min(0) int sortOrder,
            boolean isThumbnail
    ) {}
}
