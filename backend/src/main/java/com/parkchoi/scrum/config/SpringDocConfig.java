package com.parkchoi.scrum.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Scrum API 문서",
                description = "스크럼 프로젝트 문서입니다.",
                version = "v1.0",
                contact = @Contact(
                        name = "dev",
                        email = "s4078942@naver.com"
                )
        ),
        tags = {
                @Tag(name = "01.User", description = "사용자 기능"),
                @Tag(name = "~~", description = "~~"),
                @Tag(name = "~~", description = "~~"),
        }
)
@Configuration
public class SpringDocConfig {
}
