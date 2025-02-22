package aaiman.hrdashboardapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

        @Bean
        public WebMvcConfigurer corsConfigurer() {

                return new WebMvcConfigurer() {
                        @Override
                        public void addCorsMappings(CorsRegistry registry) {
                                registry.addMapping("/api/**")
                                        .allowedOrigins("http://localhost:4200")
                                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                                        .allowedHeaders("Authorization", "Content-Type")
                                        .allowCredentials(true);

                                registry.addMapping("/v3/api-docs/**")
                                        .allowedOrigins("http://localhost:8080")
                                        .allowedMethods("GET")
                                        .allowedHeaders("Authorization", "Content-Type");

                                registry.addMapping("/swagger-ui/**")
                                        .allowedOrigins("http://localhost:8080")
                                        .allowedMethods("GET");
                        }
                };

        }

}
