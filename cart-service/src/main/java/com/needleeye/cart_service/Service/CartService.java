package com.needleeye.cart_service.Service;

import com.needleeye.cart_service.Dto.Request.CartItemQuantityDto;
import com.needleeye.cart_service.Dto.Request.CartProductItemDto;
import com.needleeye.cart_service.Dto.Response.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface CartService {
    ResponseEntity<ApiResponse<?>> getCartDetails(String userId);
    ResponseEntity<ApiResponse<?>> addProductItemToCart(String userId, CartProductItemDto itemData);
    ResponseEntity<ApiResponse<?>> updateItemQuantity(String userId, Long itemId, CartItemQuantityDto quantity);
    ResponseEntity<ApiResponse<?>> removePrductItemFromCart(String userId, Long itemId);
}
