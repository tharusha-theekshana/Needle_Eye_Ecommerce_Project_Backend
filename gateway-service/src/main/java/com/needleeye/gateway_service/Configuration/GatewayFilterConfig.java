package com.needleeye.gateway_service.Configuration;

import com.needleeye.gateway_service.Dto.ApiResponse.ApiResponse;
import com.needleeye.gateway_service.Utils.Jwt.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class GatewayFilterConfig {

    private final JwtUtil jwtUtil;

    public GatewayFilterConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> jwtFilter() {
        return jwtFilterWithRoles();
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> jwtFilterExcluding(String... publicPaths) {

        return (request, next) -> {
            String path = request.path();

            for (String publicPath : publicPaths) {
                if (path.equals(publicPath)) {
                    return next.handle(request);
                }
            }

            return jwtFilter().filter(request, next);
        };
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> jwtFilterWithRoles(String... allowedRoles) {

        return (request, next) -> {
            String authHeader = request.headers()
                    .firstHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ServerResponse
                        .status(401)
                        .body(new ApiResponse<Void>(401, "JWT token is required"));
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ServerResponse
                        .status(401)
                        .body(new ApiResponse<Void>(401, "JWT token is invalid or expired"));
            }

            if (allowedRoles.length > 0) {
                String role = jwtUtil.getRole(token);
                boolean allowed = false;

                if (role != null) {
                    for (String allowedRole : allowedRoles) {
                        if (allowedRole.equalsIgnoreCase(role)) {
                            allowed = true;
                            break;
                        }
                    }
                }

                if (!allowed) {
                    return ServerResponse
                            .status(403)
                            .body(new ApiResponse<Void>(403, "Admin user JWT token required"));
                }
            }

            return next.handle(request);
        };
    }
}