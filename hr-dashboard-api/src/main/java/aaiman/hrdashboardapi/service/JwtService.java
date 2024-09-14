package aaiman.hrdashboardapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

        public static final String SECRET = generateSecretKey();
        private long jwtExpiration = 3600000;   //this is in millisecond which is equivalent to 1 hour

        public String extractUsername(String token) {
                return extractClaim(token, Claims::getSubject);
        }

        public Date extractExpiration(String token) {
                return extractClaim(token, Claims::getExpiration);
        }

        public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
                final Claims claims = extractAllClaims(token);
                return claimsResolver.apply(claims);
        }

        public Boolean validateToken(String token, UserDetails userDetails) {
                final String username = extractUsername(token);
                return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        }

        public String GenerateToken(String username){
                Map<String, Object> claims = new HashMap<>();
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
