package com.example.tamna.config;

import com.example.tamna.config.auth.CustomAuthenticationManager;
import com.example.tamna.config.auth.CustomAuthenticationProcessingFilter;
import com.example.tamna.config.jwt.JwtAuthenticationFilter;
import com.example.tamna.config.jwt.JwtProvider;
import com.example.tamna.filter.MyFilter3;
import com.example.tamna.service.UserService;
import jdk.jshell.spi.ExecutionControlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.filter.CorsFilter;

//시큐리티 관련 설정
@Configuration //IoC
@EnableWebSecurity // Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final AuthenticationConfiguration authConfiguration;
    private CustomAuthenticationManager customAuthenticationManager;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        // SecurityFilterChain이 내가 만든 필터(ex. FilterConfig파일)보다 항상 먼저 실행됨. 먼저 실행하게 하고싶으면 시큐리티 필터체인에 등록시킨 후 addFilterBefore를 통해 가장 먼저 실행되는 필터보다 우선으로 적용하면 됨
//        http.addFilterBefore(new JwtAuthenticationFilter(), SecurityContextHolderFilter.class); //BasicAuthenticationFilter가 실행되기 전에 동작
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션사용 x
                .and()
                .addFilter(corsFilter) // @CrossOrigin(인증이 없을 때만 사용), 인증이 있는 곳에서도 사용하려면 필터에 등록해야 함
                .formLogin().disable()// formLogin 사용 x
                .httpBasic().disable() // 기본 http basic 인증방식 사용 x
                .authorizeRequests()
//                .antMatchers("/api/**")
//                .access("hasRole('ROLE_USER') OR hasRole('ROLE_MANAGER')")
//                .antMatchers("/api/auth/**").permitAll() // /api/auth/ 는 security 적용 x
//                .anyRequest().authenticated();// 나머지 API 전부 인증 필요
                .antMatchers("/api/**").permitAll();// 403에러 임시 방편
//                .and().addFilterBefore(customAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    public void configure(WebSecurity webSecurity) throws Exception{
        System.out.println("여긴가");
        webSecurity.ignoring().antMatchers(HttpMethod.POST, "/api/auth/login");
    }

        @Bean
        public CustomAuthenticationProcessingFilter customAuthenticationProcessingFilter(){
            System.out.println("여긴가");
            CustomAuthenticationProcessingFilter filter = new CustomAuthenticationProcessingFilter("/api/auth/login");
            System.out.println("filter");
            filter.setAuthenticationManager(customAuthenticationManager());

            return filter;
        }

        @Bean
        public CustomAuthenticationManager customAuthenticationManager(){

        return new CustomAuthenticationManager();
        }



}
