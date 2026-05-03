package com.example.device_apisever.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Sinh JWT token cho NguoiDung (cả NV lẫn KH).
     * Payload chứa: nguoiDungId, vaiTroId, khoId (nullable), hoTen
     */
    public String generateToken(Integer nguoiDungId, String taiKhoan, Integer vaiTroId, Integer khoId, String hoTen) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("nguoiDungId", nguoiDungId);
        claims.put("vaiTroId", vaiTroId);
        if (khoId != null) {
            claims.put("khoId", khoId);
        }
        claims.put("hoTen", hoTen);

        return Jwts.builder()
                .claims(claims)
                .subject(taiKhoan)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Integer extractNguoiDungId(String token) {
        return extractAllClaims(token).get("nguoiDungId", Integer.class);
    }

    public Integer extractVaiTroId(String token) {
        return extractAllClaims(token).get("vaiTroId", Integer.class);
    }

    public Integer extractKhoId(String token) {
        return extractAllClaims(token).get("khoId", Integer.class);
    }

    public boolean isTokenValid(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return tokenUsername.equals(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
