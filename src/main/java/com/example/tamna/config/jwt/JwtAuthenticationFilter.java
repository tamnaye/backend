package com.example.tamna.config.jwt;

import com.example.tamna.model.Token;
import com.example.tamna.model.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class  JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Value("${AUTHORIZATION_HEADER}")
    private String AUTHORIZATION_HEADER;

    @Value("${REAUTHORIZATION_HEADER}")
    private String REAUTHORIZATION_HEADER;

    @Value("${ADMINAUTHORIZATION_HEADER}")
    private String ADMINAUTHORIZATION_HEADER;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 로그 기록 : 요청 uri
        log.info("doFilter JWTFilter, uri : {}", ((HttpServletRequest) request).getRequestURI());
        System.out.println(request.getMethod());
        // 로그인시 토큰 발급 라우터는 토큰 검증 없이 진행
        if(request.getRequestURI().startsWith("/auth")){
            System.out.println("토큰검증없이 로그인 및 로그아윳");
        }
        else if(request.getRequestURI().startsWith("/admin")){
            System.out.println("어드민 로그인 & 로그아웃");
            if(!request.getRequestURI().startsWith("/admin/login") && !request.getRequestURI().startsWith("/admin/logout")){

                System.out.println("여기로 오냐?");
                // 어드민 페이지 일 경우
                String accessToken = jwtProvider.getHeaderToken(ADMINAUTHORIZATION_HEADER, request);
                Map<Boolean, String> accessResult = jwtProvider.validateToken(accessToken);
                if (!accessResult.isEmpty() && accessResult.containsKey(true) && accessResult.containsValue("success")) {
                    System.out.println("accessToken 유효함");
                    UserDto user = jwtProvider.checkUser(accessToken);
                    if(!user.getRoles().equals("ADMIN")){
                        response.sendError(404);
                    }else{
                        response.setHeader(ADMINAUTHORIZATION_HEADER, tokenPrefix + accessToken);
                    }
                }else{
                    response.sendError(403);
                }
            }
        }
        else{
            System.out.println("########토큰 검증 필터###########");
            // 헤더에서 access토큰 & refresh토큰 가져옴
            String accessToken = jwtProvider.getHeaderToken(AUTHORIZATION_HEADER, request);
            String refreshToken = jwtProvider.getHeaderToken(REAUTHORIZATION_HEADER, request);
//            System.out.println("accessToken: " + accessToken);
//            System.out.println("refreshToken: " + refreshToken);

            if (accessToken != null && refreshToken != null) {
                Map<Boolean, String> accessResult = jwtProvider.validateToken(accessToken);
                if (!accessResult.isEmpty() && accessResult.containsKey(true) && accessResult.containsValue("success")) {
                    System.out.println("accessToken 유효함");
                    response.setHeader(AUTHORIZATION_HEADER, tokenPrefix + accessToken);
                    response.setHeader(REAUTHORIZATION_HEADER, tokenPrefix + refreshToken);
                } else if (!accessResult.isEmpty() && accessResult.containsKey(true) && accessResult.containsValue("fail")) {
                    System.out.println("accessToken 만료됨");
                    // refreshToken 디비랑 같은지 확인
                    Token checkRefresh = jwtProvider.checkRefresh(refreshToken);
                    System.out.println(checkRefresh);
                    if (checkRefresh != null) {
                        Map<Boolean, String> refreshResult = jwtProvider.validateToken(refreshToken);
                        if (!refreshResult.isEmpty() && refreshResult.containsKey(true) && refreshResult.containsValue("success")) {
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
                        response.sendError(403);
                    }
                } else {
                    // accessToken 오류
                    response.sendError(403);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}