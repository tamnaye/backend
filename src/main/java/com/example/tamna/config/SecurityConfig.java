package com.example.tamna.config;

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
public class SecurityConfig {

    private final CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션사용 x
                .and()
                .addFilter(corsFilter) // @CrossOrigin(인증이 없을 때만 사용), 인증이 있는 곳에서도 사용하려면 필터에 등록해야 함
                .formLogin().disable() // formLogin 사용 x
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/api/tamna/admin/**")
                .access("hasRole('ROLE_ADMIN')");
        return http.build();

    }

}
