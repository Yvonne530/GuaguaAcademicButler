package org.example.edumanagementservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.example.edumanagementservice.enums.RoleType;
import org.example.edumanagementservice.model.JwtUser;
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

    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_UID = "uid";
    private static final String ROLE_PREFIX = "ROLE_";

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

    /**
     * 生成 Access Token。
     * 携带 uid 与角色名（如 ADMIN），过滤器可直接构建出含用户ID的认证主体，
     * 控制器中 {@code @AuthenticationPrincipal JwtUser} 即可注入 id/username/role。
     */
    public String createToken(String username, Long userId, RoleType roleType) {
        return Jwts.builder()
                .setSubject(username)
                .claim(CLAIM_UID, userId)
                .claim(CLAIM_ROLE, roleType.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 生成 Refresh Token。同样携带 uid/role，刷新时无需回查数据库即可签发完整 Access Token。
     */
    public String createRefreshToken(String username, Long userId, RoleType roleType) {
        return Jwts.builder()
                .setSubject(username)
                .claim(CLAIM_UID, userId)
                .claim(CLAIM_ROLE, roleType.name())
                .setIssuedAt(new Date())
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
        return parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 从 Token 中解析角色枚举。
     * 兼容历史 Token：若 claim 中存的是 "ROLE_ADMIN" 这类带前缀的值，会自动去掉前缀。
     */
    public RoleType getRoleTypeFromToken(String token) {
        String role = parseClaimsJws(token).getBody().get(CLAIM_ROLE, String.class);
        if (role != null && role.startsWith(ROLE_PREFIX)) {
            role = role.substring(ROLE_PREFIX.length());
        }
        return RoleType.valueOf(role);
    }

    public Long getUserIdFromToken(String token) {
        Number uid = parseClaimsJws(token).getBody().get(CLAIM_UID, Number.class);
        return uid == null ? null : uid.longValue();
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        // 角色名只加一次前缀，避免出现 ROLE_ROLE_XXX
        RoleType roleType = getRoleTypeFromToken(token);
        return Collections.singletonList(new SimpleGrantedAuthority(ROLE_PREFIX + roleType.name()));
    }

    /**
     * 由 Token 构建 Spring Security 认证对象：
     * principal 为 JwtUser（实现 UserDetails），而非简单的用户名字符串，
     * 从而支持 {@code @AuthenticationPrincipal JwtUser user} 注入完整用户信息。
     */
    public Authentication getAuthentication(String token) {
        Claims body = parseClaimsJws(token).getBody();

        JwtUser jwtUser = new JwtUser();
        jwtUser.setId(getUserIdFromToken(token));
        jwtUser.setUsername(body.getSubject());
        jwtUser.setRoleType(getRoleTypeFromToken(token));

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(ROLE_PREFIX + jwtUser.getRoleType().name()));

        return new UsernamePasswordAuthenticationToken(jwtUser, null, authorities);
    }

    private Jws<Claims> parseClaimsJws(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}