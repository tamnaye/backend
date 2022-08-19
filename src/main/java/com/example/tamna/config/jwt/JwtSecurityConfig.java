package com.example.tamna.config.jwt;

import io.jsonwebtoken.Jwt;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecurityConfig {

    private JwtProvider jwtProvider;

    public JwtSecurityConfig(JwtProvider jwtProvider){
        this.jwtProvider = jwtProvider;
    }


}
