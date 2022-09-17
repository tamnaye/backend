package com.example.tamna.config.jwt;

import com.example.tamna.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
//@WebFilter(urlPatterns ="/api/*")
public class  JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private static String AUTHORIZATION_HEADER = "Authorization";
    private static String REAUTHORIZATION_HEADER = "reAuthorization";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("########토큰 검증 필터###########");
        // 헤더에서 access토큰 & refresh토큰 가져옴
        String accessToken = jwtProvider.getHeaderAccessToken(request);
        String refreshToken = jwtProvider.getHeaderRefreshToken(request);
        System.out.println("accessToken: " + accessToken);
        System.out.println("refreshToken: " + refreshToken);
        if (accessToken != null) {
            Map<Boolean, String> accessResult = jwtProvider.validateToken(accessToken);
            if (!accessResult.isEmpty() && accessResult.keySet().contains(true) && accessResult.values().contains("success")) {
                System.out.println("accessToken 유효함");
                response.setHeader(AUTHORIZATION_HEADER, accessToken);
                response.setHeader(REAUTHORIZATION_HEADER, refreshToken);
                filterChain.doFilter(request, response);
            } else if (!accessResult.isEmpty() && accessResult.keySet().contains(true) && accessResult.values().contains("fail")) {
                System.out.println("accessToken 만료됨");
                // refreshToken 디비랑 같은지 확인
                Token checkRefresh = jwtProvider.checkRefresh(refreshToken);
                System.out.println(checkRefresh);
                if (checkRefresh != null) {
                    Map<Boolean, String> refreshResult = jwtProvider.validateToken(refreshToken);
                    if (!refreshResult.isEmpty() && refreshResult.keySet().contains(true) && refreshResult.values().contains("success")) {
                        String newAccessToken = jwtProvider.createAccessToken(checkRefresh.getUserId());
                        System.out.println("newAccessToken: " + newAccessToken);
                        response.setHeader(AUTHORIZATION_HEADER, newAccessToken);
                        response.setHeader(REAUTHORIZATION_HEADER, refreshToken);
                        filterChain.doFilter(request, response);
                    } else {
                        // 로그아웃
                        jwtProvider.deleteToken(refreshToken);
                        response.sendError(403);
                    }
                }
                response.sendError(403);
            } else {
                // accessToken 오류
                response.sendError(403);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}