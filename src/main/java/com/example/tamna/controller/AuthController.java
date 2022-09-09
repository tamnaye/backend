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


    @GetMapping(value = "/authentication")
    public ResponseEntity<String> authentication(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("$#%#^$$$$$$$$$$$$$$$토큰 검증 필터를 !!!! $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

        // 해더에서 accessToken 가져옴
        String accessToken = authService.getHeaderAccessToken(request);
        System.out.println("accessToken: " + accessToken);
        if (accessToken != null) { // << 오류때문에 임시로
            // 토큰 유효성 검증
            List<Object> accessResult = authService.validateToken(accessToken);
            System.out.println("accessReuslt" + accessResult);
            if (accessToken != null && accessResult.toArray().length != 0 && accessResult.get(0).equals(true)) {
                if (accessResult.toArray().length > 1 && accessResult.get(1).equals("success")) {
                    Authentication authentication = authService.getAuthentication(accessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {// accessToken 만료시
                    // 해더에서 refreshToken 가져옴
                    System.out.println("여기로 오냐");
                    String refreshToken = authService.getHeaderRefreshToken(request);
                    // DB에 저장된 refreshToken 가져옴
                    Token tokenData = authService.checkRefresh(refreshToken);
                    List<Object> refreshResult = authService.validateToken(refreshToken);
                    if (tokenData != null) {
                        if (refreshToken != null && refreshResult.toArray().length != 0 && refreshResult.get(0).equals(true)) {
                            if (refreshResult.toArray().length > 1 && refreshResult.get(1).equals("success")) {
                                // accessToken 재발급
                                String newAccessToken = authService.createAccessToken(tokenData.getUserId());
                                response.setHeader(AUTHORIZATION_HEADER, newAccessToken);
                                response.setHeader(REAUTHORIZATION_HEADER, refreshToken);
                            } else {
                                // 로그아웃
                                authService.deleteToken(tokenData.getUserId());
                                return ResponseEntity.status(403).body("logout");
                            }
                        }
                    }
                }//
//                jwtProvider.validateToken()
            }
        }
        return ResponseEntity.status(403).body("logout");


    }

}
