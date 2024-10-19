package aaiman.hrdashboardapi.config;

import aaiman.hrdashboardapi.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        @Autowired
        private JwtAuthFilter jwtAuthFilter;

        @Bean
        public UserDetailsService userDetailsService() {
                return new CustomUserDetailsService();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                return http
                        .csrf(csrf -> csrf.disable())
                        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                        .authorizeHttpRequests((authorize) -> {
                                authorize.requestMatchers("/api/auth/**").permitAll();
                                authorize.anyRequest().authenticated();
                        })
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authenticationProvider(authenticationProvider())
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                        .logout(logout -> logout
                                .logoutUrl("/api/auth/logout")
                                .logoutSuccessHandler(((request, response, authentication) -> {
                                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                        response.setStatus(HttpServletResponse.SC_OK);
                                        response.getWriter().write("{\"message\": \"Successfully logged out\"}");
                                        response.getWriter().flush();
                                }))
                        )
                        .build();

        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();
                configuration.setAllowedOrigins(List.of("http://localhost:4200"));  // Allow Angular app's origin
                configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                configuration.setAllowCredentials(true);  // Allow credentials like cookies or authorization headers

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);  // Apply CORS settings to /api/** paths
                return source;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
                authenticationProvider.setUserDetailsService(userDetailsService());
                authenticationProvider.setPasswordEncoder(passwordEncoder());
                return authenticationProvider;

        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

}
