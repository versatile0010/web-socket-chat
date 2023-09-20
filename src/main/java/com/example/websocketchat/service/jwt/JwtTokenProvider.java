package com.example.websocketchat.service.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Service
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.validate}")
    private Long validateMilisecond;

    public String generateToken(String name) {
        Date now = new Date();
        return Jwts.builder()
                .setId(name)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validateMilisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return getClaims(token).getBody().getId();
    }

    public boolean validateToken(String token) {
        return !Objects.isNull(getClaims(token));
    }

    private Jws<Claims> getClaims(String jwt) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt);
        } catch (SignatureException e) {
            log.error("SignatureException occurs!");
            throw e;
        } catch (MalformedJwtException e) {
            log.error("MalformedJwtException occurs!");
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException occurs");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("UnsupportedJwtException occurs");
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException e");
            throw e;
        }
    }
}
