package org.example.springboot_wms.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 1. 允许任何域名使用（使用 Pattern 模式来兼容 withCredentials）
        // 这样 cpolar 的 https 域名也能通过校验
        config.addAllowedOriginPattern("*");

        // 2. 允许携带 Cookie/Session (非常重要，否则登录状态无法保存)
        config.setAllowCredentials(true);

        // 3. 允许所有的请求头
        config.addAllowedHeader("*");

        // 4. 允许所有的请求方法 (GET, POST, PUT, DELETE, OPTIONS)
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有后端接口
                .allowedOrigins("http://localhost:5177") // 允许所有前端源（开发环境用，生产环境要写具体域名）
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许所有请求方法
                .allowedHeaders("*") // 允许所有请求头
                .maxAge(3600); // 预检请求的有效期（避免频繁发预检请求）
    }
}