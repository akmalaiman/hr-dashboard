package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.dto.JwtResponseDto;
import aaiman.hrdashboardapi.dto.LoginDto;
import aaiman.hrdashboardapi.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;

        public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
                this.authenticationManager = authenticationManager;
                this.jwtService = jwtService;
        }

        @PostMapping("/login")
        public ResponseEntity<JwtResponseDto> authenticateAndGetToken(@RequestBody LoginDto loginDto) {

                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
                if (authentication.isAuthenticated()) {

                        String username = loginDto.getUsername();
                        String authorities = authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(","));

                        String token = jwtService.generateToken(username);

                        JwtResponseDto jwtResponse = JwtResponseDto.builder()
                                .accessToken(token)
                                .authorities(authorities)
                                .build();

                        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
                } else {
                        throw new UsernameNotFoundException("Invalid user request..!!");
                }

        }

}
