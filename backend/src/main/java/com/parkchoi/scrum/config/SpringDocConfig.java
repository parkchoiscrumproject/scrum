package com.parkchoi.scrum.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
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
        },
        security = {
                //여기서 name은 밑의 이름과 같아야 밑에서 설명한 securityScheme를 사용한다는 뜻입니다.
                @SecurityRequirement(name = "AuthToken"),
        }
)
@SecuritySchemes({
        @SecurityScheme(name = "AuthToken",
                //타입은 apikey, http, openinconnect, ouath2방식이 존재
                type = SecuritySchemeType.APIKEY,
                //in은 인증키의 입력 위치. Header,Query,Cookie등이 존재
                in = SecuritySchemeIn.COOKIE,
                //파라미터명
                paramName = "aaaaaaaaaa"),
})
@Configuration
public class SpringDocConfig {
}
