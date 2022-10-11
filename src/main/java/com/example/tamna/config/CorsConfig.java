package com.example.tamna.config;

import io.swagger.models.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer{

    @Value("${AUTHORIZATION_HEADER}")
    private String ACCESSTOKEN_HEADER;

    @Value("${REAUTHORIZATION_HEADER}")
    private String REFRESHTOKEN_HEADER;

    @Value("${ADMIN_HEADER}")
    private String ADMINTOKEN_HEADER;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("corsFilter");
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.POST.name())
                .allowedHeaders("*")
                .exposedHeaders(ACCESSTOKEN_HEADER, REFRESHTOKEN_HEADER, ADMINTOKEN_HEADER);
//                .maxAge(3000);
        WebMvcConfigurer.super.addCorsMappings(registry);

    }

//    @Bean
//    public CorsFilter corsFilter() {
//        System.out.println("cors 설정");
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
//        return new CorsFilter(source);
//    }
}