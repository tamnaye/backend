package com.example.tamna.config;

import com.example.tamna.config.jwt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.web.filter.CorsFilter;

//시큐리티 관련 설정
@Configuration //IoC
@EnableWebSecurity // Security 활성화
@RequiredArgsConstructor
//@Component
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션사용 x
                .and()
                .addFilter(corsFilter) // @CrossOrigin(인증이 없을 때만 사용), 인증이 있는 곳에서도 사용하려면 필터에 등록해야 함
                .formLogin().disable()// formLogin 사용 x
                .httpBasic().disable() // 기본 http basic 인증방식 사용 x

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .authorizeRequests()
                .antMatchers("/auth/login").permitAll();// 403에러 임시 방편

        return http.build();
    }



}
