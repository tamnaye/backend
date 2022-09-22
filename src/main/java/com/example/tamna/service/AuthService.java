package com.example.tamna.service;

import com.example.tamna.config.jwt.JwtProvider;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;

    // 로그인 시 토큰 생성
    public Map<String, String> login(String userId) {
        Map<String, String> map = new HashMap<>();
        UserDto user = userMapper.findByUserId(userId);
        if (user != null) {
            System.out.println(user + "토큰 발급 성공");
            // accessToken 생성
            String access = jwtProvider.createAccessToken(user.getUserId());
            System.out.println("로그인 에세스: "+access);
            //refreshToken 생성
            String refresh = jwtProvider.createRefreshToken(user.getUserId());
            System.out.println("로그엔 리프레쉬: " + refresh);
            map.put("access", access);
            map.put("refresh", refresh);
        } else {
            map.put("message", "fail");

        }
        return map;
    }

    // 유저 확인
    public UserDto checkUser(HttpServletResponse response){
        String accessToken = jwtProvider.getResHeaderAccessToken(response);
        if(accessToken!= null) {
           return jwtProvider.checkUser(accessToken);
        }
        return null;
    }

    // 로그아웃 refreshToken 삭제
    public String logOutCheckUser(HttpServletResponse response){
        String accessToken = jwtProvider.getResHeaderAccessToken(response);
        String refreshToken = jwtProvider.getHeaderRefreshToken(null, response);
        String result;
        if(accessToken != null && refreshToken != null) {
            return result = jwtProvider.deleteToken(refreshToken);
        }else{
            System.out.println("헤더에 토큰이 없는 경우");
            return null;
        }
    }

}
