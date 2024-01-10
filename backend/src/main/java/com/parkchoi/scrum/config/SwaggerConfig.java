package com.parkchoi.scrum.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    private static final String API_NAME = "Scrum API";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "Scrum API 명세서입니다.";


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info());
    }

//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("스크럼 프로젝트 API")
//                        .description("팀을 만들고, 회의를 진행하는 프로젝트입니다.")
//                        .version("1.0.0"))
//                .components(new Components()
//                        .addSecuritySchemes("bearer-key",
//                                new io.swagger.v3.oas.models.security.SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")));
//
//    }


    public Info info(){
        return new Info()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION);
    }
}
