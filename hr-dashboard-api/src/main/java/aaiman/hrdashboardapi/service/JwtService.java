package aaiman.hrdashboardapi.service;

import aaiman.hrdashboardapi.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtService {

        public static final String SECRET = generateSecretKey();
        private long jwtExpiration = 3600000;   //this is in millisecond which is equivalent to 1 hour

        public String extractUsername(String token) {

                try {
                        return extractClaim(token, Claims::getSubject);
                } catch(JwtException e) {
                        log.error("Failed to extract username from token: {}", e.getMessage());
                        throw new JwtAuthenticationException("Invalid JWT token");
                }

        }

        public Date extractExpiration(String token) {
                return extractClaim(token, Claims::getExpiration);
        }

        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
                final Claims claims = extractAllClaims(token);
                return claimsResolver.apply(claims);
        }

        public int extractUserId(String token) {
                return extractClaim(token, claims -> claims.get("userId", Integer.class));
        }

        public String extractFirstName(String token) {
                return extractClaim(token, claims -> claims.get("firstName", String.class));
        }

        public String extractRole(String token) {
                return extractClaim(token, claims -> claims.get("role", String.class));
        }

        public Boolean validateToken(String token, UserDetails userDetails) {

                try {

                        final String username = extractUsername(token);
                        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

                } catch(JwtException e) {
                        log.error("Token validation failed: {}", e.getMessage());
                        throw  new JwtAuthenticationException("Invalid JWT token");
                }

        }

        public String generateToken(String username, String firstName, int userId, String userRole){
                Map<String, Object> claims = new HashMap<>();
                claims.put("userId", userId);
                claims.put("firstName", firstName);
                claims.put("roles", userRole);
                return createToken(claims, username);
        }

        private String createToken(Map<String, Object> claims, String username) {

                return Jwts.builder()
                        .claims(claims)
                        .subject(username)
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                        .signWith(getSignKey())
                        .compact();
        }

        private Claims extractAllClaims(String token) {

                return Jwts
                        .parser()
                        .verifyWith(getSignKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        }

        private Boolean isTokenExpired(String token) {
                return extractExpiration(token).before(new Date());
        }

        private static String generateSecretKey() {

                int length = 32;

                SecureRandom secureRandom = new SecureRandom();

                byte[] bytes = new byte[length];

                secureRandom.nextBytes(bytes);

                return Base64.getEncoder().encodeToString(bytes);

        }

        private SecretKey getSignKey() {
                byte[] keyBytes = Decoders.BASE64.decode(SECRET);
                return Keys.hmacShaKeyFor(keyBytes);
        }

}
