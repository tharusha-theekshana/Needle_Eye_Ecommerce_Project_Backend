package com.needleeye.gateway_service.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;
import static org.springframework.web.servlet.function.RequestPredicates.method;

@Configuration
public class GatewayRoutingConfig {

    private final GatewayFilterConfig gatewayFilterConfig;

    public GatewayRoutingConfig(GatewayFilterConfig gatewayFilterConfig) {
        this.gatewayFilterConfig = gatewayFilterConfig;
    }

    @Bean
    public RouterFunction<ServerResponse> authServiceRoutes() {
        return route("auth-service")
                .route(path("/api/v1/auth/**"), http())
                .filter(gatewayFilterConfig.jwtFilterExcluding(
                        "/api/v1/auth/register",
                        "/api/v1/auth/login",
                        "/api/v1/auth/forgot-password",
                        "/api/v1/auth/otp-verification",
                        "/api/v1/auth/reset-password"
                ))
                .filter(lb("AUTH-SERVICE"))
                .build();
    }

    // Grant access to all GET endpoints in product service
    @Bean
    public RouterFunction<ServerResponse> productServiceRoutes() {
        return route("product-service")
                .route(
                        (path("/api/v1/product/**")
                                .or(path("/api/v1/color/**"))
                                .or(path("/api/v1/category/**"))
                                .or(path("/api/v1/subcategory/**"))
                                .or(path("/api/v1/review/**")))
                                .and(method(HttpMethod.GET)),
                        http()
                )
                .filter(lb("PRODUCT-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> reviewRoutes() {
        return route("product-service")
                .route(
                        (path("/api/v1/review/**"))
                                .and(method(HttpMethod.POST)
                                        .or(method(HttpMethod.PUT))
                                        .or(method(HttpMethod.DELETE))),
                        http()
                )
                .filter(gatewayFilterConfig.jwtFilter())
                .filter(lb("PRODUCT-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceAdminRoutes() {
        return route("product-service-admin")
                .route(
                        (path("/api/v1/product/**")
                                .or(path("/api/v1/color/**"))
                                .or(path("/api/v1/category/**")))
                                .or(path("/api/v1/subcategory/**"))
                                .and(method(HttpMethod.POST)
                                        .or(method(HttpMethod.PUT))
                                        .or(method(HttpMethod.DELETE))),
                        http()
                )
                .filter(gatewayFilterConfig.jwtFilterWithRoles("ADMIN"))
                .filter(lb("PRODUCT-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoutes() {
        return route("user-service")
                .route(
                        (path("/api/v1/user/**")), http()
                )
                .filter(gatewayFilterConfig.jwtFilter())
                .filter(lb("USER-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> cartServiceRoutes() {
        return route("cart-service")
                .route(
                        (path("/api/v1/cart/**")), http()
                )
                .filter(gatewayFilterConfig.jwtFilter())
                .filter(lb("CART-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoutes() {
        return route("inventory-service")
                .route(
                        (path("/api/v1/inventory/**")), http()
                )
                .filter(gatewayFilterConfig.jwtFilterWithRoles("ADMIN"))
                .filter(lb("INVENTORY-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoutes() {
        return route("order-service")
                .route(
                        (path("/api/v1/order/**")).and(method(HttpMethod.GET)
                                .or(method(HttpMethod.POST))
                                .or(method(HttpMethod.DELETE))), http()
                )
                .filter(gatewayFilterConfig.jwtFilter())
                .filter(lb("ORDER-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceAdminRoutes() {
        return route("order-service-admin")
                .route(
                        (path("/api/v1/order/**")).and(method(HttpMethod.PUT)), http()
                )
                .filter(gatewayFilterConfig.jwtFilterWithRoles("ADMIN"))
                .filter(lb("ORDER-SERVICE"))
                .build();
    }

}