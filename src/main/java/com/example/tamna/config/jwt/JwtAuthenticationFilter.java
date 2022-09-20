package com.example.tamna.config.jwt;

import com.example.tamna.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class  JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private static String AUTHORIZATION_HEADER = "Authorization";
    private static String REAUTHORIZATION_HEADER = "reAuthorization";

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 로그인시 토큰 발급 라우터는 토큰 검증 없이 진행
        if(request.getRequestURI().startsWith("/auth/")){
            System.out.println("토큰검증없이 로그인");
            log.info("doFilter JWTFilter, uri : {}", ((HttpServletRequest) request).getRequestURI());
        }
        else if(request.getRequestURI().startsWith("/admin")){
            // 어드민 페이지 개발을 위한 임시 방편
            System.out.println("토큰검증없이 어드민페이지");
            log.info("doFilter JWTFilter, uri : {}", ((HttpServletRequest) request).getRequestURI());
        }
        else{ // /auth/로 시작하는 라우터가 아닌경우 토큰 검증
            System.out.println("########토큰 검증 필터###########");
            // 헤더에서 access토큰 & refresh토큰 가져옴
            String accessToken = jwtProvider.getHeaderAccessToken(request);
            String refreshToken = jwtProvider.getHeaderRefreshToken(request);
            System.out.println("accessToken: " + accessToken);
            System.out.println("refreshToken: " + refreshToken);
            // 로그 기록 : 요청 uri
            log.info("doFilter JWTFilter, uri : {}", ((HttpServletRequest) request).getRequestURI());
            if (accessToken != null && refreshToken != null) {
                Map<Boolean, String> accessResult = jwtProvider.validateToken(accessToken);
//                try{
                    if (!accessResult.isEmpty() && accessResult.keySet().contains(true) && accessResult.values().contains("success")) {
                        System.out.println("accessToken 유효함");
                        response.setHeader(AUTHORIZATION_HEADER, tokenPrefix + accessToken);
                        response.setHeader(REAUTHORIZATION_HEADER, tokenPrefix + refreshToken);
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
                                response.setHeader(AUTHORIZATION_HEADER, tokenPrefix + newAccessToken);
                                response.setHeader(REAUTHORIZATION_HEADER, tokenPrefix + refreshToken);

                            } else {
                                // 로그아웃
                                jwtProvider.deleteToken(refreshToken);
                                response.sendError(403);
                            }
                        }
                        else{
                            // refreshToken 오류
                            response.sendError(403);}
                    } else {
                        // accessToken 오류
                        response.sendError(403);
                    }
//                }catch (ExpiredJwtException e){
//                response.sendError(403);
//                }
            }
//            else {
//                // accessToken이 null일 경우
//                response.sendError(403);
//            }
        }
        filterChain.doFilter(request, response);
    }
}