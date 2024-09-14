package aaiman.hrdashboardapi.config;

import aaiman.hrdashboardapi.exception.JwtAuthenticationException;
import aaiman.hrdashboardapi.service.CustomUserDetailsService;
import aaiman.hrdashboardapi.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

        @Autowired
        private JwtService jwtService;

        @Autowired
        private CustomUserDetailsService customUserDetailsService;

        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

                try {

                        String authHeader = request.getHeader("Authorization");
                        String token = null;
                        String username = null;

                        if (authHeader != null && authHeader.startsWith("Bearer ")) {
                                token = authHeader.substring(7);
                                username = jwtService.extractUsername(token);
                        }

                        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                                if (jwtService.validateToken(token, userDetails)) {
                                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                        SecurityContextHolder.getContext().setAuthentication(authentication);
                                }

                        }

                } catch (JwtAuthenticationException e) {
                        log.error("JWT authentication failed: {}", e.getMessage());
                        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid authentication token");
                        return;
                }

                filterChain.doFilter(request, response);

        }

}
