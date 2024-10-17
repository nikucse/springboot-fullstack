package com.learn.fullstack.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class JWTUtil {

    private static final String SECRET_KEY = "secret_123456.secret_123456.secret_123456secret_123456.secret_123456.secret_123456";

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        var secret = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String issueToken(String subject) {
        return issueToken(subject, Map.of());
    }

    public String issueToken(String subject, String ...scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(String subject, List<String> scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }

    public String issueToken(String subject, Map<String,Object> claims) {

        Instant now = Instant.now();
        Instant expiration = now.plus(15, ChronoUnit.DAYS);

        return Jwts.builder()
                .claims(claims)
                .issuer("customer-app")
                .subject(subject)
                .audience().add("you").and()
                .expiration(Date.from(expiration))
                .issuedAt(Date.from(now)) // for example, now
                .id(UUID.randomUUID().toString()) //just an example id
                .signWith(this.secretKey)
                .compact();

    }


    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(this.secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, String userName) {
        String subject = getSubject(token);
        return subject.equals(userName) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date today = Date.from(Instant.now());
        return getClaims(token).getExpiration().before(today);
    }
}
