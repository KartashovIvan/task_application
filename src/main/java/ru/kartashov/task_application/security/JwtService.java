package ru.kartashov.task_application.security;

import ru.kartashov.task_application.dto.response.secret.JwtResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.security.token}")
    private String jwt;

    public JwtResponse generateAuthToken(String email) {
        JwtResponse jwtDTO = new JwtResponse();
        jwtDTO.setToken(generateJwtToken(email));
        return jwtDTO;
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSingInKey())
                    .build();
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Expire JwtException ", e);
        } catch (UnsupportedJwtException e){
            log.error("Unsupported JwtException", e);
        } catch (MalformedJwtException e) {
            log.error("Malformed JwtException", e);
        } catch (SecurityException e) {
            log.error("Security Exception", e);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    private String generateJwtToken(String email) {
        Date date = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .claims(Map.of("name","test","age",15))
                .subject(email)
                .expiration(date)
                .signWith(getSingInKey())
                .compact();
    }

    private SecretKey getSingInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwt);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
