package com.example.tamna.config.auth;

import com.example.tamna.config.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

public class CustomAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public CustomAuthenticationProcessingFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    @Autowired
    PrincipalDetailsService principalDetailsService;

    @Autowired
    JwtProvider jwtProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REAUTHORIZATION_HEADER = "reAuthorization";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException  {
        String requestBody = request.getReader().lines().collect(Collectors.joining());
        String[] stringJsonParsing =requestBody.split(":");
        String jsonValue = stringJsonParsing[1];
        String userId = jsonValue.substring(1, jsonValue.length() -2);
        System.out.println(userId);
        UserDetails userDetails = principalDetailsService.loadUserByUsername(userId);
        if(userDetails != null){
           String accessToken = jwtProvider.createAccessToken(userDetails.getUsername(), userDetails.getAuthorities().toString());
           String refreshToken = jwtProvider.createRefreshToken(userDetails.getUsername());
            response.setHeader(AUTHORIZATION_HEADER, accessToken);
            response.addHeader(REAUTHORIZATION_HEADER, refreshToken);
        }
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(userId, null, userDetails.getAuthorities()));
    }
}
