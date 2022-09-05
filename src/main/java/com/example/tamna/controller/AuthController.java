package com.example.tamna.controller;

import com.example.tamna.config.auth.PrincipalDetails;
import com.example.tamna.config.auth.PrincipalDetailsService;
import com.example.tamna.config.jwt.JwtProvider;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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


    private JwtProvider jwtProvider;
    private UserMapper userMapper;
    private PrincipalDetailsService principalDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(JwtProvider jwtProvider, UserMapper userMapper,PrincipalDetailsService principalDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userMapper = userMapper;
        this.principalDetailsService = principalDetailsService;
    }


    @GetMapping("/login/token/{userId}")
    public void reqToken(@PathVariable("userId") String userId){
//        jwtProvider.createAccessToken(userId);
//        jwtProvider.createRefreshToken(userId);

    }

    @PostMapping("/token")
    public String token() {
        return "<h1>token</h1>";
    }

    @Data
    static class UserId{
        private String userId;
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<?> createToken(@RequestBody UserId userId){
        String getUserId = userId.userId;
        System.out.println(getUserId);
        User user = userMapper.findByUserId(getUserId);
//        System.out.println("user: " + user);
        UserDetails principalDetails = principalDetailsService.loadUserByUsername(getUserId);
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities()));
//        System.out.println(principalDetails.getAuthorities());
//        System.out.println(principalDetails.getAuthorities().toString());
//       String accessToken = jwtProvider.createAccessToken(getUserId, principalDetails.getAuthorities().toString());
////        String refreshToken = jwtProvider.createRefreshToken(getUserId);
//        HttpServletResponse response;
//        response.setHeader(AUTHORIZATION_HEADER, accessToken);
//        response.addHeader(REAUTHORIZATION_HEADER, refreshToken);
        System.out.println("success");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "success");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
//
//    private void authenticate(String userId) throws Exception{
//        try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToke)
//        }



}
