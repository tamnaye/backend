package com.example.tamna.config.jwt;

import com.example.tamna.mapper.AuthMapper;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REAUTHORIZATION_HEADER = "reAuthorization";

    private JwtProvider jwtProvider;



    public JwtAuthenticationFilter(JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String accessToken = getAccessToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();
        System.out.println("requestURI: " + requestURI);
        if(StringUtils.hasText(accessToken) && jwtProvider.validateToken(accessToken)){
//            Authentication authentication = JwtProvider

        }
    }

    // request Header에서 access토큰 가져옴
    private String getAccessToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    // request Header에서 refresh토큰 가져옴
    private String getRefreshToken(HttpServletRequest request){
        String bearerToken = request.getHeader(REAUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }


}
