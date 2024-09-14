package aaiman.hrdashboardapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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
                        .setClaims(claims)      //TODO: replace deprecated
                        .setSubject(username)   //TODO: replace deprecated
                        .setIssuedAt(new Date(System.currentTimeMillis()))      //TODO: replace deprecated
                        .setExpiration(new Date(System.currentTimeMillis()+1000*60*1))  //TODO: replace deprecated
                        .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();    //TODO: replace deprecated
        }

        private Claims extractAllClaims(String token) {
                return Jwts
                        .parser()
                        .setSigningKey(getSignKey())    //TODO: replace deprecated
                        .build()
                        .parseClaimsJws(token)  //TODO: replace deprecated
                        .getBody();     //TODO: replace deprecated
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

        private Key getSignKey() {
                byte[] keyBytes = Decoders.BASE64.decode(SECRET);
                return Keys.hmacShaKeyFor(keyBytes);
        }

}
