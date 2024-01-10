package com.parkchoi.scrum.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    //swagger 주소 : http://localhost:8080/swagger-ui/index.html#/

    private static final String API_NAME = "Scrum API";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "Scrum API 명세서입니다.";


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(info());
    }


    public Info info(){
        return new Info()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION);
    }
}
