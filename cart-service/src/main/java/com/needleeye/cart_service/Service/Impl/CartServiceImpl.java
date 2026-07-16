package com.needleeye.cart_service.Service.Impl;

import com.needleeye.cart_service.Configuration.OpenFeign.UserServiceClient;
import com.needleeye.cart_service.Dto.Request.CartItemQuantityDto;
import com.needleeye.cart_service.Dto.Request.CartProductItemDto;
import com.needleeye.cart_service.Dto.Response.ApiResponse;
import com.needleeye.cart_service.Dto.Response.CartProductItemResponseDto;
import com.needleeye.cart_service.Dto.Response.CartResponseDto;
import com.needleeye.cart_service.Entity.Cart;
import com.needleeye.cart_service.Entity.CartItem;
import com.needleeye.cart_service.Repository.CartItemRepo;
import com.needleeye.cart_service.Repository.CartRepo;
import com.needleeye.cart_service.Service.CartService;
import com.needleeye.cart_service.Utils.Constants.AppConstants;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // Get card details by user id
    @Override
    public ResponseEntity<ApiResponse<?>> getCartDetails(String userId) {
        try {
            userServiceClient.getUserDataById(userId);

            Optional<Cart> cart = cartRepo.findByUserId(userId);

            if (cart.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CART_NOT_FOUND));
            }

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.CART_FETCHED, buildCartResponse(cart.get())));

        } catch (FeignException.NotFound e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.USER_NOT_FOUND));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
        }
    }

    // Add product item to cart
    @Override
    public ResponseEntity<ApiResponse<?>> addProductItemToCart(String userId, CartProductItemDto itemData) {
        try {
            userServiceClient.getUserDataById(userId);

            Cart cart = getOrCreateCart(userId);

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
                cart.getItems().add(newItem);
                cartItemRepo.save(newItem);
            }

            cart.setUpdatedAt(LocalDateTime.now());
            cartRepo.save(cart);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.PRODUCT_ITEM_ADDED));

        } catch (FeignException.NotFound e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.USER_NOT_FOUND));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
        }
    }

    // Change quantity of a product item in cart
    @Override
    public ResponseEntity<ApiResponse<?>> updateItemQuantity(String userId, Long itemId, CartItemQuantityDto quantity) {
        try {
            Optional<Cart> optionalCart = cartRepo.findByUserId(userId);

            if (optionalCart.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CART_NOT_FOUND));
            }

            Cart cart = optionalCart.get();
            Optional<CartItem> item = cartItemRepo.findByIdAndCartId(itemId, cart.getId());

            if (!item.isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.ITEM_NOT_FOUND));
            }

            item.get().setQuantity(quantity.getQuantity());
            item.get().setUpdatedAt(LocalDateTime.now());
            cartItemRepo.save(item.get());

            cart.setUpdatedAt(LocalDateTime.now());
            cartRepo.save(cart);

            Cart refreshedCart = cartRepo.findByUserId(userId).get();

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.ITEM_UPDATED, buildCartResponse(refreshedCart)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Remove product item from cart
    @Override
    public ResponseEntity<ApiResponse<?>> removeProductItemFromCart(String userId, Long itemId) {
        try {
            Optional<Cart> optionalCart = cartRepo.findByUserId(userId);

            if (optionalCart.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CART_NOT_FOUND));
            }

            Optional<CartItem> optionalItem = cartItemRepo.findByIdAndCartId(itemId, optionalCart.get().getId());

            if (optionalItem.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.ITEM_NOT_FOUND));
            }

            Cart cart = optionalCart.get();
            CartItem item = optionalItem.get();

            // Get cart items and remove item
            List<CartItem> cartItems = cart.getItems();
            cartItems.remove(item);
            cartItemRepo.delete(item);

            cart.setUpdatedAt(LocalDateTime.now());
            cartRepo.save(cart);

            Optional<Cart> refreshedCart = cartRepo.findByUserId(userId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.ITEM_REMOVED, buildCartResponse(refreshedCart.get())));


        }catch (Exception e) {
            e.printStackTrace();;
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Clear cart
    @Override
    public ResponseEntity<ApiResponse<?>> clearCart(String userId) {
        try{
            Optional<Cart> optionalCart = cartRepo.findByUserId(userId);

            if (optionalCart.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CART_NOT_FOUND));
            }

            Cart cart = optionalCart.get();

            cart.getItems().clear();
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepo.save(cart);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.CART_CLEARED));

        }catch (Exception e){
            e.printStackTrace();;
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

    // Map cart item dto to entity
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

    // Create cart data response
    private CartResponseDto buildCartResponse(Cart cart) {
        List<CartProductItemResponseDto> itemData = cart.getItems().stream()
                .map(item -> new CartProductItemResponseDto(
                        item.getId(),
                        item.getProductId(),
                        item.getProductName(),
                        item.getImageUrl(),
                        item.getPrice(),
                        item.getSize(),
                        item.getColor(),
                        item.getQuantity(),
                        item.getPrice() * item.getQuantity()
                ))
                .collect(Collectors.toList());

        int totalItems = itemData.stream().mapToInt(CartProductItemResponseDto::getQuantity).sum();
        double totalAmount = itemData.stream().mapToDouble(CartProductItemResponseDto::getSubtotal).sum();

        return new CartResponseDto(cart.getId(), cart.getUserId(), itemData, totalItems, totalAmount);
    }
}
