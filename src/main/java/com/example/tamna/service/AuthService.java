package com.example.tamna.service;

import com.example.tamna.config.auth.PrincipalDetails;
import com.example.tamna.config.jwt.JwtProvider;
import com.example.tamna.mapper.AuthMapper;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserMapper userMapper;
    private final AuthMapper authMapper;
    private final JwtProvider jwtProvider;

    // 로그인 시 토큰 생성
    public Map<String, String> login(String userId) {
        Map<String, String> map = new HashMap<>();

        UserDto user = userMapper.findByUserId(userId);
        if (user != null) {
            System.out.println(user);
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            System.out.println(principalDetails);
            System.out.println(principalDetails.getAuthorities());
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
            System.out.println(authentication);
            // accessToken 생성
            String access = jwtProvider.createAccessToken(user.getUserId());
            //refreshToken 생성
            String refresh = jwtProvider.createRefreshToken(user.getUserId());
            map.put("access", access);
            map.put("refresh", refresh);
        } else {
            map.put("message", "fail");

        }
        return map;
    }

    // 로그인 유지
//    public Map<String, Object> jwt
    // 2) 로그인 유지
    //refreshtoken 유효시 access 재발급

    // 로그아웃
    public String logOut(String userId){

        int result = authMapper.deleteToken(userId);
        if(result != 0){
            return "success";
        }
        return "fail";
    }
}
