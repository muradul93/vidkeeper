package dev.murad.vidkeeper.config;

import dev.murad.vidkeeper.exceptions.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtil {

  private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final int MINUTES = 60;

  public static String generateToken(String username) {
    var now = Instant.now();
    return Jwts.builder()
        .subject(username)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
        .compact();
  }

  public static String extractUsername(String token) {
    return getTokenBody(token).getSubject();
  }

  public static Boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private static Claims getTokenBody(String token) {
    try {
      return Jwts
          .parser()
          .setSigningKey(SECRET_KEY)
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (SignatureException | ExpiredJwtException e) { // Invalid signature or expired token
      throw new AccessDeniedException("Access denied: " + e.getMessage());
    }
  }

  private static boolean isTokenExpired(String token) {
    Claims claims = getTokenBody(token);
    return claims.getExpiration().before(new Date());
  }
}