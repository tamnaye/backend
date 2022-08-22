package com.example.tamna.controller;

import com.example.tamna.config.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final JwtProvider jwtProvider;

    @GetMapping("/login/token/{userId}")
    public void reqToken(@PathVariable("userId") String userId){
        jwtProvider.createAccessToken(userId);
        jwtProvider.createRefreshToken(userId);

    }

    @PostMapping("token")
    public String token() {
        return "<h1>token</h1>";
    }
}
