package com.jmca.easyauthapplication.security.jwt;

import com.jmca.easyauthapplication.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jmc.app.jwtSecret}")
    private String jwtSecret;

    @Value("${jmc.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", extractRoles(userPrincipal.getAuthorities()));
        return generateTokenWithClaims(claims, userPrincipal.getUsername());
    }

    private Collection<? extends GrantedAuthority> extractRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities;
    }

    // Generate token with additional claims
    public String generateTokenWithClaims(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username from token
    public String getUserNameFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date getExpirationDateFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // Extract roles or any claim from token
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token has expired
    public boolean isTokenExpired(String token) {
        return getExpirationDateFromJwtToken(token).before(new Date());
    }

    // Validate JWT token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // Refresh token (generate a new token before it expires)
    public String refreshJwtToken(String token) {
        Claims claims = extractAllClaims(token);
        claims.setIssuedAt(new Date());
        claims.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs));
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Create a new signing key from the jwtSecret
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Collection<? extends GrantedAuthority> getRolesFromJwtToken(String token) {
        Claims claims = extractAllClaims(token);
        Object roles = claims.get("roles");

        if (roles instanceof Collection<?>) {
            return ((Collection<?>) roles).stream()
                    .map(role -> (GrantedAuthority) () -> role.toString())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    // Check if the token is valid for the user
    public boolean isTokenValidForUser(String token, UserDetailsImpl userDetails) {
        final String username = getUserNameFromJwtToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Extract issue date from JWT token
    public Date getIssuedAtDateFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }


}