package com.example.tamna.config.jwt;

import com.example.tamna.mapper.TokenMapper;
import com.example.tamna.mapper.UserMapper;
import com.example.tamna.model.Token;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.*;

// 토큰 생성, 유효성 검증
@Service
@RequiredArgsConstructor
public class JwtProvider implements InitializingBean {

    private final TokenMapper tokenMapper;
    private final UserMapper userMapper;


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
    public void afterPropertiesSet(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    public java.sql.Date time() {
        final long miliseconds = System.currentTimeMillis();
        return new java.sql.Date(miliseconds);
    }

    //accessToken 생성
    public String createAccessToken(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
//        System.out.println("id: " + userId);

        Date now = new Date();
        Date accessValidity = new Date(now.getTime() + accessTokenValidityInMilliSeconds * 1000);

        String accessToken = Jwts.builder()
                .setClaims(claims) // user 정보
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(accessValidity) // 만료시간 설정
                .compact();

//        System.out.println(accessToken);
        return accessToken;
    }

    // refreshToken 생성
    public String createRefreshToken(String userId){

        Date now = new Date();
        System.out.println("now: " + now);

        Date refreshValidity = new Date(now.getTime() + refreshTokenValidityInMilliSeconds * 1000);

        String refreshToken = Jwts.builder()
                .setSubject("") // 유저 데이터 x
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(refreshValidity)
                .compact();

        System.out.println("refreshToken" + refreshToken);

        java.sql.Date today = time();
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
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        System.out.println("claims= " + claims);

        return claims.getSubject();
    }

    // 헤더에서 accessToken 가져오기
    public String getHeaderAccessToken(HttpServletRequest request){
        String bearerAccessToken = request.getHeader(AUTHORIZATION_HEADER);
        System.out.println("헤더 토큰: " + bearerAccessToken);
        if (StringUtils.hasText(bearerAccessToken) && bearerAccessToken.startsWith("Bearer ")){
            bearerAccessToken = bearerAccessToken.substring(7);
        }
        return bearerAccessToken;
    }

    public String getResHeaderAccessToken(HttpServletResponse response){
        String bearerAccessToken = response.getHeader(AUTHORIZATION_HEADER);
        System.out.println("헤더 토큰: " + bearerAccessToken);
        if (StringUtils.hasText(bearerAccessToken) && bearerAccessToken.startsWith("Bearer ")){
            bearerAccessToken = bearerAccessToken.substring(7);
        }
        return bearerAccessToken;
    }

    // 헤더에서 refreshToken 가져오기
    public String getHeaderRefreshToken(HttpServletRequest request){
        String bearerRefreshToken = request.getHeader(REAUTHORIZATION_HEADER);
        System.out.println(bearerRefreshToken);
        if (StringUtils.hasText(bearerRefreshToken) && bearerRefreshToken.startsWith("Bearer ")){
            bearerRefreshToken = bearerRefreshToken.substring(7);
        }

        return bearerRefreshToken;
    }


    // Jwt 유효성 검사
    public Map<Boolean, String> validateToken(String token){
        Map<Boolean, String> result = new HashMap<>();
        try{
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            result.put(!claims.getBody().getExpiration().before(new Date()), "success");
            System.out.println("토큰 검증 결과: " + result);
            return result;
        }catch (ExpiredJwtException e){
            System.out.println("만료된 JWT");
            result.put(true, "fail");
            System.out.println("토큰 검증 결과: " + result);
            return result;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명");
        }catch (UnsupportedJwtException e){
            System.out.println("지원되지 않는 JWT");
        }catch (IllegalStateException e){
            System.out.println("JWT 토큰 잘못됨");
        }catch (IllegalArgumentException e){
            System.out.println("JWT 토큰 없음");
        }
        result.put(false, "유호하지 않음");
        System.out.println("토큰 검증 결과: " + result);
        return result;
    }


    // refresh토큰 DB 삭제
    public String deleteToken(String userId){
        int result = tokenMapper.deleteToken(userId);
        if(result != 0){
            return "success";
        }
        return "fail";
    }

}
