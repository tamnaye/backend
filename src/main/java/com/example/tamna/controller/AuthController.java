package com.example.tamna.controller;

import com.example.tamna.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REAUTHORIZATION_HEADER = "reAuthorization";

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
        if(tokenMap.get("message") == null) {
            response.addHeader(AUTHORIZATION_HEADER, tokenMap.get("access"));
            response.addHeader(REAUTHORIZATION_HEADER, tokenMap.get("refresh"));
            Map<String, String> map = new HashMap<>();
            map.put("message", "success");
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(tokenMap);
        }

    }
}
