package com.njarach.scrabble_ods_api.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  private final SecretKey key;
  private final long expirationSeconds;

  public JwtService(
      @Value("${security.jwt.secret}") String secret,
      @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    this.expirationSeconds = expirationSeconds;
  }

  public String createToken(String username) {
    Instant now = Instant.now();
    return Jwts.builder()
        .subject(username)
        .issuedAt(Date.from(now))
        .expiration(Date.from(now.plusSeconds(expirationSeconds)))
        .signWith(key)
        .compact();
  }

  public String extractUsername(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getSubject();
  }

  public long getExpirationSeconds() {
    return expirationSeconds;
  }
}
