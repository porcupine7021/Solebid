package com.sesac.solbid.service;

import com.sesac.solbid.dto.product.request.ProductCreateRequest;

public interface ProductService {
    Long create(Long sellerId, ProductCreateRequest req);}
