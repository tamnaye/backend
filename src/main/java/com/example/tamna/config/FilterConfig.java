package com.example.tamna.config;

import com.example.tamna.filter.MyFilter1;
import com.example.tamna.filter.MyFilter2;
import org.springframework.boot.web.servlet.AbstractFilterRegistrationBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// SecurityFilterChain에 필터를 거는 것이 아닌 필터 새로 생성
@Configuration
public class FilterConfig {

    @Bean
    public AbstractFilterRegistrationBean<MyFilter1> filter1(){
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("*");
        bean.setOrder(1); // 낮은 번호가 새로 만든 필터 중 가장 먼저 실행됨
        return bean;

    }

    @Bean
    public AbstractFilterRegistrationBean<MyFilter2> filter2(){
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("*");
        bean.setOrder(0);
        return bean;

    }
}
