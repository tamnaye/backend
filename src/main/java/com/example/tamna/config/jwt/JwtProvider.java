package com.example.tamna.config.jwt;

import com.example.tamna.config.auth.PrincipalDetailsService;
import com.example.tamna.mapper.TokenMapper;
import com.example.tamna.model.Token;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 토큰 생성, 유효성 검증
@Component
public class JwtProvider implements InitializingBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REAUTHORIZATION_HEADER = "reAuthorization";
    private java.sql.Date today = java.sql.Date.valueOf(LocalDate.now(ZoneId.of("Asia/Seoul")));

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.accesstoken-validity-in-seconds}")
    private long accessTokenValidityInMilliSeconds;

    @Value("${jwt.refreshtoken-validity-in-seconds}")
    private long refreshTokenValidityInMilliSeconds;

    private PrincipalDetailsService principalDetailsService;

    private TokenMapper authMapper;
    private Key key;


    @Autowired
    public JwtProvider(PrincipalDetailsService principalDetailsService,
             TokenMapper authMapper){
        this.principalDetailsService = principalDetailsService;
        this.authMapper = authMapper;
    }


    // afterPropertiesSet() 빈 초기화 시 코드 구현
    @Override // Bean이 생성되고 주입받은 후 secretKey값을 Base64 Decode해서 Key변수에 할당
    public void afterPropertiesSet(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);

    }


    // accessToken 생성
    public String createAccessToken(String userId){
        Claims claims = Jwts.claims().setSubject(userId);
        System.out.println("id: "+ userId);

        long now = (new Date()).getTime();
        System.out.println("now: " + now);

        Date accessValidity = new Date(now + accessTokenValidityInMilliSeconds * 1000);

        String accessToken = Jwts.builder()
                .setClaims(claims) // user 정보
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(accessValidity) // 만료시간 설정
                .compact();

        System.out.println(accessToken);
        return accessToken;
    }


    // refreshToken 생성
    public String createRefreshToken(String userId){

        long now = (new Date()).getTime();
        System.out.println("now: " + now);

        Date refreshValidity = new Date(now + refreshTokenValidityInMilliSeconds * 1000);

        String refreshToken = Jwts.builder()
                .setSubject("") // 유저 데이터 x
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(refreshValidity)
                .compact();

        System.out.println("refreshToken" + refreshToken);

        int success = authMapper.insertToken(today, userId, refreshToken);
        System.out.println(success);
        return refreshToken;
    }

    // refresh DB 에서 검색
    public Token checkRefresh(String refreshToken){
        return authMapper.findToken(refreshToken);
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
    public List<Object> validateToken(String token){
        List<Object> result = new ArrayList<>();
        try{
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            result.add(!claims.getBody().getExpiration().before(new Date()));
            result.add("success");
            return result;
        }catch (ExpiredJwtException e){
            System.out.println("만료된 JWT");
            result.add(true);
            result.add("fail");
            return result;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            System.out.println("잘못된 JWT 서명");
        }catch (UnsupportedJwtException e){
            System.out.println("지원되지 않는 JWT");
        }catch (IllegalStateException e){
            System.out.println("JWT 토큰 잘못됨");
        }
        result.add(false);
        return result;
    }

    // 토큰 재발급

    // refresh토큰 DB 삭제
    public String deleteToken(String userId){
        int result = authMapper.deleteToken(userId);
        if(result != 0){
            return "success";
        }
        return "fail";
    }

}
