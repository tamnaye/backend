package com.example.tamna.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
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
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.tamna"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey(), anotherApiKey()));

    }

    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER, "header");
    }

    private ApiKey anotherApiKey(){
        return new ApiKey(REAUTHORIZATION_HEADER, REAUTHORIZATION_HEADER, "header");
    }

    private SecurityContext securityContext() {
        return springfox
                .documentation
                .spi.service
                .contexts
                .SecurityContext
                .builder()
                .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope authorizationScope1 = new AuthorizationScope("global", "refreshEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        AuthorizationScope[] authorizationScopes1 = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        authorizationScopes1[0] = authorizationScope1;
        return Arrays.asList(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes), new SecurityReference(REAUTHORIZATION_HEADER, authorizationScopes1));
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("TAMNAYEAH Open API Documentation")
                .description("더큰내일센터 회의실 예약 시스템 API")
                .version("1.0.0")
                .build();
    }



}
