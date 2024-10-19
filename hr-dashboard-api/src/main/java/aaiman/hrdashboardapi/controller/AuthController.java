package aaiman.hrdashboardapi.controller;

import aaiman.hrdashboardapi.dto.JwtResponseDto;
import aaiman.hrdashboardapi.dto.LoginDto;
import aaiman.hrdashboardapi.model.User;
import aaiman.hrdashboardapi.repository.UserRepository;
import aaiman.hrdashboardapi.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

        private final UserRepository userRepository;
        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;

        public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
                this.authenticationManager = authenticationManager;
                this.jwtService = jwtService;
                this.userRepository = userRepository;
        }

        @PostMapping("/login")
        public ResponseEntity<JwtResponseDto> authenticateAndGetToken(@RequestBody LoginDto loginDto) {

                String username = loginDto.getUsername();

                User user = userRepository.findByUsernameAndStatus(username, "Active");
                if (user != null) {

                        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
                        if (authentication.isAuthenticated()) {

                                String role = authentication.getAuthorities().stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.joining(","));

                                String firstName = user.getFirstName();
                                int userId = user.getId();

                                String token = jwtService.generateToken(username, firstName, userId, role);

                                JwtResponseDto jwtResponse = JwtResponseDto.builder()
                                        .accessToken(token)
                                        .ok(true)
                                        .status(HttpStatus.OK.value())
                                        .build();

                                return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
                        } else {

                                JwtResponseDto jwtResponse = JwtResponseDto.builder()
                                        .ok(false)
                                        .status(HttpStatus.UNAUTHORIZED.value())
                                        .build();

                                return new ResponseEntity<>(jwtResponse, HttpStatus.UNAUTHORIZED);

                        }

                } else {

                        JwtResponseDto jwtResponse = JwtResponseDto.builder()
                                .ok(false)
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .build();

                        return new ResponseEntity<>(jwtResponse, HttpStatus.UNAUTHORIZED);

                }

        }

}
