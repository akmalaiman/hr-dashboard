package aaiman.hrdashboardapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {

                return new OpenAPI()
                        .info(new Info()
                                .title("HR Dashboard API")
                                .description("This API provides a comprehensive set of endpoints for managing employees, job positions, roles, attendance, performance reviews, and other HR functions. It's designed to support HR teams in managing and automating tasks effectively.")
                                .version("1.0")
                                .contact(new Contact()
                                        .name("HR Dashboard API Owner")
                                        .email("akmal.aaiman16@gmail.com")
                                )
                        );

        }

}
