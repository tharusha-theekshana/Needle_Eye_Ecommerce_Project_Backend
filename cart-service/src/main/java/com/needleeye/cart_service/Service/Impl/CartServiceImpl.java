package com.needleeye.cart_service.Service.Impl;

import com.needleeye.cart_service.Configuration.OpenFeign.UserServiceClient;
import com.needleeye.cart_service.Dto.Request.CartProductItemDto;
import com.needleeye.cart_service.Dto.Response.ApiResponse;
import com.needleeye.cart_service.Entity.Cart;
import com.needleeye.cart_service.Entity.CartItem;
import com.needleeye.cart_service.Repository.CartItemRepo;
import com.needleeye.cart_service.Repository.CartRepo;
import com.needleeye.cart_service.Service.CartService;
import com.needleeye.cart_service.Utils.Constants.AppConstants;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private CartRepo cartRepo;
    private CartItemRepo cartItemRepo;
    private UserServiceClient userServiceClient;

    public CartServiceImpl(CartRepo cartRepo, CartItemRepo cartItemRepo, UserServiceClient userServiceClient) {
        this.cartRepo = cartRepo;
        this.cartItemRepo = cartItemRepo;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getCartDetails(String userId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> addProductItemToCart(String userId, CartProductItemDto itemData) {
        try {

            ResponseEntity<ApiResponse<?>> response;
            try {
                response = userServiceClient.getUserDataById(userId);

            } catch (FeignException.NotFound ex) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.USER_NOT_FOUND));
            }

            Cart cart = getOrCreateCart(userId);

            // Check already has cart item with request item
            Optional<CartItem> existingItem = cartItemRepo.findByCartIdAndProductIdAndSizeAndColor(
                    cart.getId(), itemData.getProductId(), itemData.getSize(), itemData.getColor());

            if (existingItem.isPresent()) {
                CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + itemData.getQuantity());
                item.setPrice(itemData.getPrice());
                item.setUpdatedAt(LocalDateTime.now());

                cartItemRepo.save(item);

            } else {
                CartItem newItem = mapDataToCartItemEntity(itemData, cart);

                // Get all cart items in cart and add new item as cart item
                List<CartItem> cartItems = cart.getItems();
                cartItems.add(newItem);

                // Add all items to cart as list
                cart.setItems(cartItems);
                cartItemRepo.save(newItem);
            }

            cart.setUpdatedAt(LocalDateTime.now());
            cartRepo.save(cart);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.PRODUCT_ITEM_ADDED));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Get existing cart or create new one for user
    private Cart getOrCreateCart(String userId) {
        Optional<Cart> optionalCart = cartRepo.findByUserId(userId);

        if (optionalCart.isPresent()) {
            return optionalCart.get();
        } else {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setCreatedAt(LocalDateTime.now());
            newCart.setUpdatedAt(LocalDateTime.now());

            return cartRepo.save(newCart);
        }
    }

    private CartItem mapDataToCartItemEntity(CartProductItemDto itemData, Cart cart) {
        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProductId(itemData.getProductId());
        item.setProductName(itemData.getProductName());
        item.setImageUrl(itemData.getImageUrl());
        item.setPrice(itemData.getPrice());
        item.setSize(itemData.getSize());
        item.setColor(itemData.getColor());
        item.setQuantity(itemData.getQuantity());
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        return item;
    }

}
