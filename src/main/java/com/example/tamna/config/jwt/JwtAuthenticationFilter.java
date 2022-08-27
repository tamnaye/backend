package com.example.tamna.config.jwt;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class JwtAuthenticationFilter extends GenericFilterBean {

    private JwtProvider jwtProvider;

    // response.addHeader 해더로 보내는 함수!!!

    public JwtAuthenticationFilter(JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

//        String accessToken = getAccessToken(httpServletRequest);
//        String requestURI = httpServletRequest.getRequestURI();
//        System.out.println("requestURI: " + requestURI);
//        if(StringUtils.hasText(accessToken) && jwtProvider.validateToken(accessToken)){
////            Authentication authentication = JwtProvider
//        String token = jwtProvider.
//        }

    }

}
