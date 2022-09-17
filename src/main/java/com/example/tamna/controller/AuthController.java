package com.example.tamna.controller;

import com.example.tamna.model.Token;
import com.example.tamna.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${AUTHORIZATION_HEADER}")
    private String AUTHORIZATION_HEADER;

    @Value("${REAUTHORIZATION_HEADER}")
    private String REAUTHORIZATION_HEADER;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    private final AuthService authService;

    @Data
    static class UserId{
        private String userId;
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<Map<String, String>> createToken(@RequestBody UserId userId, HttpServletResponse response){
        String getUserId = userId.userId;
        System.out.println(getUserId);
        Map<String, String> tokenMap =  authService.login(getUserId);
        System.out.println("로그인 토큰 맵!!! " + tokenMap);
        System.out.println(tokenMap.get("message"));
        if(tokenMap.get("message") != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(tokenMap);

        }else{
            response.addHeader(AUTHORIZATION_HEADER, tokenPrefix + tokenMap.get("access"));
            response.addHeader(REAUTHORIZATION_HEADER, tokenPrefix + tokenMap.get("refresh"));
            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
    }



}
