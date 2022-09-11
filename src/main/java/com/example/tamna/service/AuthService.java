package com.example.tamna.service;

import com.example.tamna.config.auth.PrincipalDetails;
import com.example.tamna.config.auth.PrincipalDetailsService;
import com.example.tamna.config.jwt.JwtProvider;
import com.example.tamna.mapper.TokenMapper;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.model.Token;
import com.example.tamna.model.UserDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements InitializingBean {

    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final JwtProvider jwtProvider;
    private final PrincipalDetailsService principalDetailsService;

    @Value("${AUTHORIZATION_HEADER}")
    private String AUTHORIZATION_HEADER;

    @Value("${REAUTHORIZATION_HEADER}")
    private String REAUTHORIZATION_HEADER;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.accesstoken-validity-in-seconds}")
    private long accessTokenValidityInMilliSeconds;

    @Value("${jwt.refreshtoken-validity-in-seconds}")
    private long refreshTokenValidityInMilliSeconds;

    private Key key;

    // afterPropertiesSet() 빈 초기화 시 코드 구현
    @Override // Bean이 생성되고 주입받은 후 secretKey값을 Base64 Decode해서 Key변수에 할당
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Date time() {
        final long miliseconds = System.currentTimeMillis();
        return new Date(miliseconds);
    }

    //accessToken 생성
    public String createAccessToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        System.out.println("id: " + userId);

        java.util.Date now = new java.util.Date();
        java.util.Date accessValidity = new java.util.Date(now.getTime() + accessTokenValidityInMilliSeconds * 1000);

        String accessToken = Jwts.builder()
                .setClaims(claims) // user 정보
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(accessValidity) // 만료시간 설정
                .compact();

        System.out.println(accessToken);
        return accessToken;
    }

    // refreshToken 생성
    public String createRefreshToken(String userId){

        java.util.Date now = new java.util.Date();
//        long now = (new java.util.Date()).getTime();
        System.out.println("now: " + now);

        java.util.Date refreshValidity = new java.util.Date(now.getTime() + refreshTokenValidityInMilliSeconds * 1000);

        String refreshToken = Jwts.builder()
                .setSubject("") // 유저 데이터 x
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(refreshValidity)
                .compact();

        System.out.println("refreshToken" + refreshToken);

        Date today = time();
        int success = tokenMapper.insertToken(today, userId, refreshToken);
        System.out.println(success);
        return refreshToken;
    }

    // refresh DB 에서 검색
    public Token checkRefresh(String refreshToken){
        return tokenMapper.findToken(refreshToken);
    }

    // access토큰에서 아이디 추출
    public String getUserIdFromJwt(String accessToken){
        if(accessToken!=null) {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();

            System.out.println("claims= " + claims);

            return claims.getSubject();
        } return "fail";
    }



    // 인증 선공시 SecurityContextHolder에 저장할 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        UserDetails principalDetails = principalDetailsService.loadUserByUsername(getUserIdFromJwt(token));
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }


    // 헤더에서 accessToken 가져오기
    public String getHeaderAccessToken(HttpServletRequest request){
        String bearerAccessToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerAccessToken) && bearerAccessToken.startsWith("Bearer ")){
            bearerAccessToken = bearerAccessToken.substring(7);
        }
        return bearerAccessToken;
    }





    // 로그인 시 토큰 생성
    public Map<String, String> login(String userId) {
        Map<String, String> map = new HashMap<>();

        System.out.println(userId);

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

    // 유저 확인
    public UserDto checkUser(HttpServletRequest request){
        String accessToken = getHeaderAccessToken(request);
        String userId = getUserIdFromJwt(accessToken);
        return userMapper.findByUserId(userId);
    }

}
