package com.needleeye.product_service.Service;

import com.needleeye.product_service.Dto.Request.ProductDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<ApiResponse<?>> getAllProducts();
    ResponseEntity<ApiResponse<?>> addProduct(ProductDto productData);

}
