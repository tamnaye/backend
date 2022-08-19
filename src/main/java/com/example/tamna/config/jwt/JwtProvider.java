package com.example.tamna.config.jwt;

import com.example.tamna.config.auth.PrincipalDetailsService;
import com.example.tamna.dto.TokenDto;
import com.example.tamna.mapper.AuthMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

// 토큰 생성, 유효성 검증
@Component
public class JwtProvider implements InitializingBean {

    private static final String AUTHORITIES_KEY = "auth";

    private final String secretKey;
    private final long accessTokenValidityInMilliSeconds;
    private final long refreshTokenValidityInMilliSeconds;

    private PrincipalDetailsService principalDetailsService;

    private AuthMapper authMapper;

    private Key key;

    // 생성자
    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.accesstoken-validity-in-seconds}") long accessTokenValidityInMilliSeconds,
            @Value("${jwt.refreshtoken-validity-in-seconds}") long refreshTokenValidityInMilliSeconds, AuthMapper authMapper){

        this.secretKey = secretKey;
        this.accessTokenValidityInMilliSeconds = accessTokenValidityInMilliSeconds * 1000;
        this.refreshTokenValidityInMilliSeconds = refreshTokenValidityInMilliSeconds * 1000;
        this.authMapper = authMapper;
    }


    // afterPropertiesSet() 빈 초기화 시 코드 구현
    @Override // Bean이 생성되고 주입받은 후 secretKey값을 Base64 Decode해서 Key변수에 할당
    public void afterPropertiesSet(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);

    }


    // accessToken 생성
//    public String createAccessToken(Authentication authentication){
    public String createAccessToken(String userId){

//            String userId = authentication.getName();
        System.out.println("name: "+ userId);

        long now = (new Date()).getTime();
        System.out.println("now: " + now);

        Date accessValidity = new Date(now + this.accessTokenValidityInMilliSeconds);

        String accessToken = Jwts.builder()
                .setSubject(userId)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(accessValidity)
                .compact();

        System.out.println(accessToken);
        return accessToken;
    }


    // refreshToken 생성
//    public String createRefreshToken(Authentication authentication){
    public String createRefreshToken(String userId){

//    String userId = authentication.getName();

        long now = (new Date()).getTime();
        System.out.println("now: " + now);

        Date refreshValidity = new Date(now + this.refreshTokenValidityInMilliSeconds);

        String refreshToken = Jwts.builder()
                .setSubject("")
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(refreshValidity)
                .compact();

//        TokenDto success = authMapper.updateToken(userId,refreshToken);
//        System.out.println("getUserId= " + success.getUserId() + "getRefresh= " + success.getRefreshToken());
        System.out.println(refreshToken);
        return refreshToken;
    }

    // access토큰에서 아이디 추출
    public String getUserIdFromJwt(String accessToken){

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        System.out.println("claims= " + claims);
//        UserDetails principal = new PrincipalDetails(claims.getSubject(), "", "");

        return claims.getSubject();
    }
    // jwt 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails principalDetails = principalDetailsService.loadUserByUsername(getUserIdFromJwt(token));
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }

    // Jwt 유효성 검사
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            System.out.println("잘못된 JWT 서명");
        }catch (ExpiredJwtException e){
            System.out.println("만료된 JWT");
        }catch (UnsupportedJwtException e){
            System.out.println("지원되지 않는 JWT");
        }catch (IllegalStateException e){
            System.out.println("JWT 토큰 잘못됨");
        }
        return false;
    }

    // access토큰 재발급

}
