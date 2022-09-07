package com.example.tamna.config.jwt;

import com.example.tamna.model.Token;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JwtProvider jwtProvider;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REAUTHORIZATION_HEADER = "reAuthorization";

    @Autowired
    public JwtAuthenticationFilter(JwtProvider jwtProvider, String pattern){
        this.jwtProvider = jwtProvider;
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        // 해더에서 accessToken 가져옴
        String accessToken = jwtProvider.getHeaderAccessToken(request);
        // 토큰 유효성 검증
        List<Object> accessResult = jwtProvider.validateToken(accessToken);
        if(accessToken != null && accessResult.toArray().length != 0 && accessResult.get(0).equals(true)){
            if(accessResult.toArray().length > 1 && accessResult.get(1).equals("success")){
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{// accessToken 만료시
                // 해더에서 refreshToken 가져옴
                String refreshToken = jwtProvider.getHeaderRefreshToken(request);
                // DB에 저장된 refreshToken 가져옴
                Token tokenData = jwtProvider.checkRefresh(refreshToken);
                List<Object> refreshResult = jwtProvider.validateToken(refreshToken);
                if(tokenData != null){
                    if(refreshToken != null && refreshResult.toArray().length!=0 && refreshResult.get(0).equals(true)) {
                        if (refreshResult.toArray().length > 1 && refreshResult.get(1).equals("success")) {
                            // accessToken 재발급
                            String newAccessToken = jwtProvider.createAccessToken(tokenData.getUserId());
                             response.setHeader(AUTHORIZATION_HEADER, newAccessToken);
                        }else{
                            // 로그아웃
                            jwtProvider.deleteToken(tokenData.getUserId());
                        } 
                    }
                    }}// 로그아웃
//                jwtProvider.validateToken()
            }
        filterChain.doFilter(request, response);
        }



    }


//public class JwtAuthenticationFilter extends GenericFilterBean {
//
//    private JwtProvider jwtProvider;
//
//    // response.addHeader 해더로 보내는 함수!!!
//
//    public JwtAuthenticationFilter(JwtProvider jwtProvider){
//        this.jwtProvider = jwtProvider;
//    }
//
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//
////        String accessToken = getAccessToken(httpServletRequest);
////        String requestURI = httpServletRequest.getRequestURI();
////        System.out.println("requestURI: " + requestURI);
////        if(StringUtils.hasText(accessToken) && jwtProvider.validateToken(accessToken)){
//////            Authentication authentication = JwtProvider
////        String token = jwtProvider.
////        }
//
//    }