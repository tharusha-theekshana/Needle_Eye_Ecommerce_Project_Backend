package com.needleeye.cart_service.Service;

import com.needleeye.cart_service.Dto.Request.CartProductItemDto;
import com.needleeye.cart_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<ApiResponse<?>> getCartDetails(String userId);
    ResponseEntity<ApiResponse<?>> addProductItemToCart(String userId, CartProductItemDto itemData);
}
