package com.group5.rental_room.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.group5.rental_room.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;

@Service
public class JwtService {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    public JwtService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

//    public String generateToken(UserDetails userDetails) {
//        return generateToken(new HashMap<>(), userDetails);
//    }
//
//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//        // Correctly extract role names as Strings
//        List<String> roles = userDetails.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        return Jwts.builder()
//                .setClaims(extraClaims)
//                .setSubject(userDetails.getUsername())
//                .claim("roles", roles) // Now stores as ["ROLE_USER", "ROLE_AGENT"]
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30)) // Set to 30 days or preferred time
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
// NEW METHOD — Add this to your JwtService class
public String generateToken(UserEntity user) {
    List<String> roles = user.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

    Map<String, Object> extraClaims = new HashMap<>();
    extraClaims.put("roles", roles);
    extraClaims.put("userId", user.getId());  // ← This adds the ID to the token

    return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(user.getUsername())  // This is your email
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30)) // 30 days
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();
}

    // Fix: Using Map<String, Object> and ensuring claims are handled correctly
//    public String generateRefresh(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return Jwts.builder()
//                .setClaims(extraClaims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365)) // 1 year
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
// Optional: cleaner refresh token for UserEntity
    public String generateRefresh(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 365)) // 1 year
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateToken(String token) {
        String userEmail = extractUserName(token);
        if (StringUtils.isNotEmpty(userEmail) && !isTokenExpired(token)) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            return isTokenValid(token, userDetails);
        }
        return false;
    }
}