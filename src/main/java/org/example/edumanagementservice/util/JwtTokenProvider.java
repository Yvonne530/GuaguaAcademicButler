package org.example.edumanagementservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.example.edumanagementservice.enums.RoleType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        if (!StringUtils.hasText(secret)) {
            throw new IllegalStateException("JWT secret 不能为空");
        }
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // 新增的核心方法（方案B）
    public Authentication getAuthentication(String token) {
        String username = getUsernameFromToken(token);
        List<GrantedAuthority> authorities = getAuthoritiesFromToken(token);
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }

    public String createToken(String username, RoleType roleType) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", roleType.getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 24))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            // 明确区分过期异常
            throw new JwtException("JWT 已过期", ex);
        } catch (JwtException | IllegalArgumentException ex) {
            throw new JwtException("无效的 JWT", ex);
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        String role = getRoleFromToken(token);
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
}