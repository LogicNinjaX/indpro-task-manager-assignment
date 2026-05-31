package com.nitish.task_manager_api.util;

import com.nitish.task_manager_api.config.JwtConfig;
import com.nitish.task_manager_api.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Component
public class JWTUtil {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    public final Duration expiration;


    private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);

    public JWTUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expiration = Duration.ofMillis(jwtConfig.getExpiration());
        logger.info("JWT secret and expiration loaded.");
    }


    private Optional<Claims> parseClaims(String jwtToken) {
        var jwtParser = Jwts.parser()
                .verifyWith(secretKey).build();

        try {
            return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("JWT exception occurred", e);
        }

        return Optional.empty();
    }


    public Optional<String> getUsername(String jwtToken) {
        var claimsOptional = parseClaims(jwtToken);
        return claimsOptional.map(Claims::getSubject);
    }

    public boolean validateToken(String jwtToken) {
        return parseClaims(jwtToken).isPresent();
    }

    public String generateToken(CustomUserDetails userDetails){
        var now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userDetails.getUsername())
                .claim("userId", userDetails.getUserId())
                .claim("role", "ROLE_USER")
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }
}
