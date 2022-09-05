package com.example.tamna.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomAuthenticationManager implements AuthenticationManager {

    @Autowired
    private PrincipalDetailsService principalDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        System.out.println("AuthenticationManager 진입");
        UserDetails userDetails = principalDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

}
