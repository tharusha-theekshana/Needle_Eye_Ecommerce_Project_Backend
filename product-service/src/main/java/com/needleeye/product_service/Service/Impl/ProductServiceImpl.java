package com.needleeye.product_service.Service.Impl;

import com.needleeye.product_service.Dto.Request.ProductDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Dto.Response.ProductResponseDto;
import com.needleeye.product_service.Entity.Category;
import com.needleeye.product_service.Entity.Color;
import com.needleeye.product_service.Entity.Product;
import com.needleeye.product_service.Entity.Review;
import com.needleeye.product_service.Repository.CategoryRepo;
import com.needleeye.product_service.Repository.ColorRepo;
import com.needleeye.product_service.Repository.ProductRepo;
import com.needleeye.product_service.Repository.ReviewRepo;
import com.needleeye.product_service.Service.CloudinaryService;
import com.needleeye.product_service.Service.KafkaProducerService;
import com.needleeye.product_service.Service.ProductService;
import com.needleeye.product_service.Utils.Constants.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepo productRepo;
    private CategoryRepo categoryRepo;
    private ColorRepo colorRepo;
    private ReviewRepo reviewRepo;
    private CloudinaryService cloudinaryService;
    private KafkaProducerService kafkaProducerService;

    public ProductServiceImpl(ProductRepo productRepo, CategoryRepo categoryRepo, ColorRepo colorRepo, ReviewRepo reviewRepo, CloudinaryService cloudinaryService, KafkaProducerService kafkaProducerService) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.colorRepo = colorRepo;
        this.reviewRepo = reviewRepo;
        this.cloudinaryService = cloudinaryService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getAllProducts() {
        try {

            List<Product> productList = productRepo.findAll();

            List<ProductResponseDto> responseDtoList = productList
                    .stream()
                    .map(this::mapEntityToDto)
                    .collect(Collectors.toList());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.PRODUCTS_FETCHED,responseDtoList));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> addProduct(ProductDto productData) {
        try {

            // Check category
            Optional<Category> category = categoryRepo.findById(productData.getCategoryId());
            if(category.isEmpty()){
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CATEGORY_NOT_FOUND));
            }

            // Check colors
            List<Color> colors = colorRepo.findAllById(productData.getColorIds());
            if (colors.size() != productData.getColorIds().size()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.COLORS_NOT_FOUND));
            }

            // Set last price
            double lastPrice = productData.getPrice();
            if (productData.getDiscountPercentage() != null && productData.getDiscountPercentage() > 0) {
                lastPrice = productData.getPrice() * (1 - productData.getDiscountPercentage() / 100);
            }

            Product product = mapDtoToEntity(productData,category.get(),colors,lastPrice);
            productRepo.save(product);

            // Send request to inventory-service to create the initial inventory record
            kafkaProducerService.sendProductCreatedEvent(product.getProductId());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(HttpStatus.CREATED.value(), AppConstants.PRODUCT_ADDED));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    Product mapDtoToEntity(ProductDto productData, Category category,List<Color> colors, Double lastPrice){
        Product mappedProduct = new Product();

        mappedProduct.setProductId(generateUniqueProductId());
        mappedProduct.setName(productData.getName());
        mappedProduct.setDescription(productData.getDescription());
        mappedProduct.setPrice(productData.getPrice());
        mappedProduct.setDiscountPercentage(productData.getDiscountPercentage());
        mappedProduct.setLastPrice(lastPrice);
        mappedProduct.setAvailable(productData.getAvailable());
        mappedProduct.setImageUrl(productData.getImageUrl());
        mappedProduct.setCategory(category);
        mappedProduct.setColors(colors);
        mappedProduct.setSizes(productData.getSizes());
        mappedProduct.setCreatedAt(LocalDate.now());
        mappedProduct.setUpdatedAt(LocalDate.now());

        return mappedProduct;
    }

    // Map product Dto to entity
    ProductResponseDto mapEntityToDto(Product product){
        ProductResponseDto responseDto = new ProductResponseDto();

        responseDto.setId(product.getId());
        responseDto.setProductId(product.getProductId());
        responseDto.setImageUrl(product.getImageUrl());
        responseDto.setName(product.getName());
        responseDto.setDescription(product.getDescription());
        responseDto.setPrice(product.getPrice());
        responseDto.setDiscountPercentage(product.getDiscountPercentage());
        responseDto.setLastPrice(product.getLastPrice());
        responseDto.setAvailable(product.getAvailable());
        responseDto.setSizes(product.getSizes());
        responseDto.setCreatedAt(product.getCreatedAt());
        responseDto.setUpdatedAt(product.getUpdatedAt());

        // Category name
        responseDto.setCategoryName(product.getCategory().getCategory());

        // Set Colors
        if (product.getColors() != null) {
            List<String> colorCodes = product.getColors()
                    .stream()
                    .map(Color::getColorCode)
                    .collect(Collectors.toList());
            responseDto.setColorCodes(colorCodes);
        }

        List<Review> reviewList = new ArrayList<>();
        responseDto.setAverageRating(0.0);
        responseDto.setTotalReviews(reviewList.size());
        return responseDto;
    }

    // Generate product id
    private String generateUniqueProductId() {
        String productId;
        do {
            int number = new Random().nextInt(900000) + 100000;
            productId = "PROD" + number;
        } while (productRepo.existsByProductId(productId));
        return productId;
    }
}
