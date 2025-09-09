package com.sesac.solbid.mapper;

import com.sesac.solbid.domain.Product;
import com.sesac.solbid.domain.ProductImage;
import com.sesac.solbid.dto.product.request.ProductCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(target = "product", source = "product")
    @Mapping(target = "filePath", source = "dto.filePath")
    @Mapping(target = "fileName", source = "dto.fileName")
    @Mapping(target = "sortOrder", source = "dto.sortOrder")
    @Mapping(target = "isThumbnail", source = "dto.isThumbnail") // 필드명 isThumbnail 명시
    ProductImage toEntity(ProductCreateRequest.ImageDto dto, Product product);
}
