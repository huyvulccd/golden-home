package com.example.timphongtrohanoi.service.impl;

import com.example.timphongtrohanoi.common.DateTimeUtil;
import com.example.timphongtrohanoi.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Objects;
import java.util.function.Function;

import static io.jsonwebtoken.io.Decoders.BASE64;

@Service
public class JwtServiceImpl implements JwtService {

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    private String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(DateTimeUtil.atTime())
                .setExpiration(DateTimeUtil.nextHour())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Key getSigningKey() {
        byte[] key = BASE64.decode("Decoders.BASE64.decode(413F4428472B4B62506553685660597033733676397924422645294840406351)");
        return Keys.hmacShaKeyFor(key);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolves) {
        final Claims claims = extractAllClaim(token);
        return claimsResolves.apply(claims);
    }

    private Claims extractAllClaim(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(DateTimeUtil.atTime());
    }
}
