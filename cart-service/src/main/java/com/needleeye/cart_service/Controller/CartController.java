package com.needleeye.cart_service.Controller;

import com.needleeye.cart_service.Dto.Request.CartItemQuantityDto;
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

    // Change quantity of a product item in cart
    @PutMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponse<?>> updateItemQuantity(@PathVariable String userId, @PathVariable Long itemId, @Valid @RequestBody CartItemQuantityDto quantity) {
        try {
            return cartService.updateItemQuantity(userId, itemId, quantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));

    }

    // Remove product item form cart
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponse<?>> removeProductItemFromCart(@PathVariable String userId, @PathVariable Long itemId) {
        try {
            return cartService.removePrductItemFromCart(userId, itemId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }
}
