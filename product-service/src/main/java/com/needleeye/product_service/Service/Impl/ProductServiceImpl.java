package com.needleeye.product_service.Service.Impl;

import com.needleeye.product_service.Configuration.OpenFeign.InventoryServiceClient;
import com.needleeye.product_service.Dto.Request.ProductDto;
import com.needleeye.product_service.Dto.Response.ApiResponse;
import com.needleeye.product_service.Dto.Response.InventoryResponseDto;
import com.needleeye.product_service.Dto.Response.ProductResponseDto;
import com.needleeye.product_service.Entity.*;
import com.needleeye.product_service.Repository.*;
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

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;
    private final SubCategoryRepo subCategoryRepo;
    private final ReviewRepo reviewRepo;
    private final KafkaProducerService kafkaProducerService;
    private final InventoryServiceClient inventoryServiceClient;

    public ProductServiceImpl(ProductRepo productRepo, CategoryRepo categoryRepo, SubCategoryRepo subCategoryRepo, ReviewRepo reviewRepo, KafkaProducerService kafkaProducerService, InventoryServiceClient inventoryServiceClient) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.subCategoryRepo = subCategoryRepo;
        this.reviewRepo = reviewRepo;
        this.kafkaProducerService = kafkaProducerService;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    // Get all products
    @Override
    public ResponseEntity<ApiResponse<?>> getAllProducts() {
        try {

            List<Product> productList = productRepo.findAll();

            // Fetch all inventories as map
            Map<String, InventoryResponseDto> inventoryMap = fetchAllInventoriesMap();

            List<ProductResponseDto> responseDtoList = productList
                    .stream()
                    .map(product -> {
                        ProductResponseDto dto = mapEntityToDto(product);
                        dto.setInventory(returnInventoryHashMap(inventoryMap.get(product.getProductId())));
                        return dto;
                    })
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

    // Get product details by product id
    @Override
    public ResponseEntity<ApiResponse<?>> getProductById(String productId) {
        try {

            Optional<Product> optionalProduct = productRepo.findByProductId(productId);

            if (optionalProduct.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.PRODUCT_NOT_FOUND));
            }

            // Fetch inventory details
            ResponseEntity<ApiResponse<InventoryResponseDto>> response = inventoryServiceClient.getInventoryByProductId(productId);
            InventoryResponseDto inventoryData = response.getBody().getData();
            HashMap<String, Integer> inventoryHashMap = returnInventoryHashMap(inventoryData);

            ProductResponseDto productResponseDto = mapEntityToDto(optionalProduct.get());
            productResponseDto.setInventory(inventoryHashMap);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.PRODUCT_FETCHED, productResponseDto));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> getNewArrivals() {
        try {

            LocalDate fromDate = LocalDate.now().minusDays(AppConstants.NEW_ARRIVAL_WINDOW_DAYS);
            List<Product> productList = productRepo.findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(fromDate);

            // Fetch all inventories as map
            Map<String, InventoryResponseDto> inventoryMap = fetchAllInventoriesMap();

            List<ProductResponseDto> responseDtoList = productList
                    .stream()
                    .map(product -> {
                        ProductResponseDto dto = mapEntityToDto(product);
                        dto.setInventory(returnInventoryHashMap(inventoryMap.get(product.getProductId())));
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.NEW_ARRIVALS_FETCHED, responseDtoList));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Add product
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

            // check sub category
            Optional<SubCategory> subCategory = subCategoryRepo.findById(productData.getSubCategoryId());
            if (subCategory.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.SUB_CATEGORY_NOT_FOUND));
            }

            // Set last price
            double lastPrice = productData.getPrice();
            if (productData.getDiscountPercentage() != null && productData.getDiscountPercentage() > 0) {
                lastPrice = productData.getPrice() * (1 - productData.getDiscountPercentage() / 100);
            }

            Product product = mapDtoToEntity(productData,category.get(),subCategory.get(),lastPrice);
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

    @Override
    public ResponseEntity<ApiResponse<?>> updateProduct(String productId, ProductDto productData) {
        try{
            Optional<Product> optionalProduct = productRepo.findByProductId(productId);

            if (optionalProduct.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.PRODUCT_NOT_FOUND));
            }

            // Check category
            Optional<Category> category = categoryRepo.findById(productData.getCategoryId());
            if(category.isEmpty()){
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.CATEGORY_NOT_FOUND));
            }

            // Check sub category
            Optional<SubCategory> subCategory = subCategoryRepo.findById(productData.getSubCategoryId());
            if (subCategory.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.SUB_CATEGORY_NOT_FOUND));
            }

            // Set last price
            double lastPrice = productData.getPrice();
            if (productData.getDiscountPercentage() != null && productData.getDiscountPercentage() > 0) {
                lastPrice = productData.getPrice() * (1 - productData.getDiscountPercentage() / 100);
            }

            Product product = optionalProduct.get();
            product.setImageUrl(productData.getImageUrl());
            product.setName(productData.getName());
            product.setDescription(productData.getDescription());
            product.setPrice(productData.getPrice());
            product.setDiscountPercentage(productData.getDiscountPercentage());
            product.setLastPrice(lastPrice);
            product.setAvailable(productData.getAvailable());
            product.setCategory(category.get());
            product.setSubCategory(subCategory.get());
            product.setColorCodes(productData.getColorCodes());
            product.setSizes(productData.getSizes());
            product.setUpdatedAt(LocalDate.now());

            productRepo.save(product);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(HttpStatus.CREATED.value(), AppConstants.PRODUCT_UPDATED));


        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    @Override
    public ResponseEntity<ApiResponse<?>> deleteProduct(String productId) {
        try {

            Optional<Product> optionalProduct = productRepo.findByProductId(productId);
            if (optionalProduct.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), AppConstants.PRODUCT_NOT_FOUND));
            }

            productRepo.delete(optionalProduct.get());

            // Notify inventory-service to remove the inventory record for this product
            kafkaProducerService.sendProductDeleteEvent(productId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ApiResponse<>(HttpStatus.OK.value(), AppConstants.PRODUCT_DELETED));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstants.SERVER_ERROR));
    }

    // Map product dto to entity
    Product mapDtoToEntity(ProductDto productData, Category category, SubCategory subCategory, Double lastPrice){
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
        mappedProduct.setSubCategory(subCategory);
        mappedProduct.setColorCodes(productData.getColorCodes());
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
        responseDto.setSubCategoryName(product.getSubCategory().getSubCategory());

        // Set Colors
        responseDto.setColorCodes(product.getColorCodes());

        List<Review> reviewList = new ArrayList<>();
        responseDto.setAverageRating(0.0);
        responseDto.setTotalReviews(reviewList.size());
        return responseDto;
    }

    // Fetch all inventories and index with product id
    private Map<String, InventoryResponseDto> fetchAllInventoriesMap() {
        try {
            ResponseEntity<ApiResponse<List<InventoryResponseDto>>> response = inventoryServiceClient.getAllInventories();

            if (response != null && response.getBody() != null && response.getBody().getData() != null) {
                return response.getBody().getData()
                        .stream()
                        .collect(Collectors.toMap(InventoryResponseDto::getProductId, inventory -> inventory, (a, b) -> a));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    // Map inventory data to hash map without id and product id
    private HashMap<String, Integer> returnInventoryHashMap(InventoryResponseDto responseDto){
        if(responseDto != null){
            HashMap<String,Integer> inventoryHashMap = new HashMap<>();
            inventoryHashMap.put("total" , responseDto.getTotalInventory());
            inventoryHashMap.put("available" , responseDto.getAvailable());
            inventoryHashMap.put("sell" , responseDto.getSell());
            return  inventoryHashMap;
        }
        return new HashMap<>();
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
