package com.dawn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 在开发时，通常 Vue 项目运行在本地（如 http://localhost:8080），
    // 而 Spring Boot 后端运行在另一个端口（如 http://localhost:8081）。
    // 由于端口不同，浏览器会认为这是跨源请求，你需要在 Spring Boot 中配置 CORS，允许前端访问后端 API。
    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/api/**") // Allow CORS for all API endpoints under /api
                .allowedOrigins("http://localhost:5173") // Allow requests from the default Vite dev server origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allowed headers
                .allowCredentials(true); // Allow credentials (e.g., cookies)
    }
}