package com.example.tamna.config.jwt;

import com.example.tamna.config.auth.PrincipalDetailsService;
import com.example.tamna.mapper.AuthMapper;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 토큰 생성, 유효성 검증
@Component
public class JwtProvider implements InitializingBean {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REAUTHORIZATION_HEADER = "reAuthorization";

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.accesstoken-validity-in-seconds}")
    private long accessTokenValidityInMilliSeconds;

    @Value("${jwt.refreshtoken-validity-in-seconds}")
    private long refreshTokenValidityInMilliSeconds;

    private PrincipalDetailsService principalDetailsService;

    private AuthMapper authMapper;
    private Key key;


    @Autowired
    public JwtProvider(PrincipalDetailsService principalDetailsService,
             AuthMapper authMapper){
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
    public String createAccessToken(String userId, String userRole){
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", userRole);
        System.out.println("id: "+ userId);
        System.out.println("role: " + userRole);

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

        int success = authMapper.insertToken(userId, refreshToken);
        System.out.println(success);
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

        return claims.getSubject();
    }


    // 인증 선공시 SecurityContextHolder에 저장할 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        UserDetails principalDetails = principalDetailsService.loadUserByUsername(getUserIdFromJwt(token));
        return new UsernamePasswordAuthenticationToken(principalDetails, "", principalDetails.getAuthorities());
    }

    // 헤더에서 가져오기
    public List<String> getHeaderToken(HttpServletRequest request){
        List<String> bearerToken = new ArrayList<>();

        bearerToken.add(request.getHeader(AUTHORIZATION_HEADER));
        bearerToken.add(request.getHeader(REAUTHORIZATION_HEADER));

        for(int i=0; i < bearerToken.toArray().length; i++){
            String token = bearerToken.get(i);
            if (StringUtils.hasText(token) && token.startsWith("Bearer ")){
                bearerToken.set(i, token.substring(7));
            }
        }
        return bearerToken;
    }

    // Jwt 유효성 검사
    public boolean validateToken(String token){
        try{
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
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


}
