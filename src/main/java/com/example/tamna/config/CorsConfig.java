package com.example.tamna.config;

import io.swagger.models.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.*;
import java.io.IOException;


@Configuration
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig {

    @Value("${AUTHORIZATION_HEADER}")
    private String ACCESSTOKEN_HEADER;

    @Value("${REAUTHORIZATION_HEADER}")
    private String REFRESHTOKEN_HEADER;

    @Value("${ADMINAUTHORIZATION_HEADER}")
    private String ADMINTOKEN_HEADER;

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        System.out.println("cors");
//        registry.addMapping("/auth/**");
//        registry.addMapping("/admin/**");
//        registry.addMapping("/api/**")
//                .allowCredentials(true)
//                .allowedOrigins("*")
//                .allowedHeaders("*")
//                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name())
//                .exposedHeaders(ACCESSTOKEN_HEADER)
//                .exposedHeaders(REFRESHTOKEN_HEADER)
//                .exposedHeaders(ADMINTOKEN_HEADER);
//        System.out.println(registry);
//        WebMvcConfigurer.super.addCorsMappings(registry);
//    }

//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true); // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있도록 설정
//        config.addAllowedOriginPattern("*"); //  모든 ip에 응답 허용
//        config.addAllowedHeader("*"); // 모든 header에 응답 허용
//        config.addAllowedMethod("*"); // 모든 메소드(get, post, put, delete) 허용
//        config.addExposedHeader(ACCESSTOKEN_HEADER);
//        config.addExposedHeader(REFRESHTOKEN_HEADER);
//        config.addExposedHeader(ADMINTOKEN_HEADER);
//        source.registerCorsConfiguration("/api/**", config);
//        source.registerCorsConfiguration("/auth/**", config);
//        source.registerCorsConfiguration("/admin/**", config);
////        new CorsFilter(source);
//        filterChain.doFilter(request, response);
//    }

    @Bean
    public CorsFilter corsFilter() {
        System.out.println("cors 설정");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있도록 설정
        config.addAllowedOriginPattern("*"); //  모든 ip에 응답 허용
        config.addAllowedHeader("*"); // 모든 header에 응답 허용
        config.addAllowedMethod("*"); // 모든 메소드(get, post, put, delete) 허용
        config.addExposedHeader(ACCESSTOKEN_HEADER);
        config.addExposedHeader(REFRESHTOKEN_HEADER);
        config.addExposedHeader(ADMINTOKEN_HEADER);
        source.registerCorsConfiguration("/api/**", config);
        source.registerCorsConfiguration("/auth/**", config);
        source.registerCorsConfiguration("/admin/**", config);
        return new CorsFilter(source);
    }
}