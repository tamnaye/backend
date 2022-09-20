package com.example.tamna.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//http://localhost:8080/swagger-ui.html
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${AUTHORIZATION_HEADER}")
    String AUTHORIZATION_HEADER;

    @Value("${REAUTHORIZATION_HEADER}")
    String REAUTHORIZATION_HEADER;

    @Bean
    public Docket api(){
        Parameter parameterBuilder1 = new ParameterBuilder().name(HttpHeaders.AUTHORIZATION).description("Access Token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();

        Parameter parameterBuilder2 = new ParameterBuilder().name(REAUTHORIZATION_HEADER).description("refresh Token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();

        List<Parameter> globalParamters = new ArrayList<>();
        globalParamters.add(parameterBuilder1);
        globalParamters.add(parameterBuilder2);

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(globalParamters)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.tamna"))
                .paths(PathSelectors.any())
                .build();

    }


    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("TAMNAYEAH Open API Documentation")
                .description("더큰내일센터 회의실 예약 시스템 API")
                .version("1.0.0")
                .build();
    }


    
}
