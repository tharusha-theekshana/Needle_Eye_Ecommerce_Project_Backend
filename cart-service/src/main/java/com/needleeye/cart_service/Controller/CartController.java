package com.needleeye.cart_service.Controller;

import com.needleeye.cart_service.Dto.Request.CartProductItemDto;
import com.needleeye.cart_service.Dto.Response.ApiResponse;
import com.needleeye.cart_service.Service.CartService;
import com.needleeye.cart_service.Utils.Constants.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Get cart details by user id
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> getCartDetails(@PathVariable String userId) {
        try {
            return cartService.getCartDetails(userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Add product item to cart
    @PostMapping("/{userId}/items")
    public ResponseEntity<ApiResponse<?>> addProductItemToCart(@PathVariable String userId, @Valid @RequestBody CartProductItemDto itemData) {
        try {
            return cartService.addProductItemToCart(userId, itemData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }


}
