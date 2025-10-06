package org.pragma.creditya.security.jwt.provider;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.pragma.creditya.security.exception.SecurityInfraException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;

@Component
public class JwtProvider {

    private static final Logger LOGGER =  Logger.getLogger(JwtProvider.class.getName());

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.expiration}")
    private Integer EXPIRATION;

    private Integer EXPIRATION_MACHINE = 1000;



    private SecretKey KEY;

    @PostConstruct
    private void init () {
        LOGGER.info("INIT KEY.. ");
        if (SECRET_KEY == null) return;

        this.KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails) {
        if (Objects.isNull(userDetails))
            throw new SecurityInfraException("[infra.security] user detail is mandatory");

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY, Jwts.SIG.HS256)
                .compact();
    }

    public String generateMachineToken(String clientId) {
        if (Objects.isNull(clientId))
            throw new SecurityInfraException("[infra.security.jwt] ClientId must be mandatory");

        return Jwts.builder()
                .subject(clientId)
                .claim("scope", "INTERNAL_COMMUNICATION")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MACHINE))
                .signWith(KEY, Jwts.SIG.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            LOGGER.severe("Invalid Token: " + e.getMessage());
            return false;
        }
    }
}