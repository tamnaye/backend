package com.example.tamna.config.jwt;

import com.example.tamna.model.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//@RequiredArgsConstructor
public class  JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtProvider jwtProvider;
    private static String AUTHORIZATION_HEADER = "Authorization";
    private static String REAUTHORIZATION_HEADER = "reAuthorization";

//    @Autowired
    public JwtAuthenticationFilter(JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        System.out.println("$#%#^$$$$$$$$$$$$$$$토큰 검증 필터를 !!!! $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        // 해더에서 accessToken 가져옴
        String accessToken = jwtProvider.getHeaderAccessToken(request);
        System.out.println("accessToken: " + accessToken);
        if(accessToken != null){ // << 오류때문에 임시로
        // 토큰 유효성 검증
        List<Object> accessResult = jwtProvider.validateToken(accessToken);
        System.out.println("accessReuslt"+accessResult);
        if(accessToken != null && accessResult.toArray().length != 0 && accessResult.get(0).equals(true)){
            if(accessResult.toArray().length > 1 && accessResult.get(1).equals("success")){
                Authentication authentication = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else{// accessToken 만료시
                // 해더에서 refreshToken 가져옴
                System.out.println("여기로 오냐");
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
                             response.setHeader(REAUTHORIZATION_HEADER, refreshToken);
                        }else{
                            // 로그아웃
                            jwtProvider.deleteToken(tokenData.getUserId());
                        }
                    }
                    }}//
//                jwtProvider.validateToken()
            }
        } else{
            response.sendError(403);
        }
        filterChain.doFilter(request, response);
        }



    }
