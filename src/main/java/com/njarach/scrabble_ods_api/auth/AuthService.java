package com.njarach.scrabble_ods_api.auth;

import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
  private final AppUserRepository users;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(
      AppUserRepository users, PasswordEncoder passwordEncoder, JwtService jwtService) {
    this.users = users;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public AuthResponse register(AuthController.Credentials credentials) {
    if (credentials == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identifiants requis");
    }
    String username = normalizeUsername(credentials.username());
    String password = credentials.password();
    if (password == null
        || password.length() < 8
        || password.getBytes(StandardCharsets.UTF_8).length > 72) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Le mot de passe doit contenir au moins 8 caractères et au maximum 72 octets UTF-8");
    }
    if (users.existsByUsername(username)) {
      throw new ResponseStatusException(
          HttpStatus.CONFLICT, "Ce nom d'utilisateur est déjà utilisé");
    }
    AppUser user = users.save(new AppUser(username, passwordEncoder.encode(password)));
    return issueToken(user);
  }

  public AuthResponse login(AuthController.Credentials credentials) {
    String username =
        credentials == null || credentials.username() == null ? "" : credentials.username().trim();
    String password = credentials == null ? null : credentials.password();
    AppUser user =
        users
            .findByUsername(username)
            .orElseThrow(
                () ->
                    new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides"));
    if (password == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides");
    }
    return issueToken(user);
  }

  private String normalizeUsername(String value) {
    if (value == null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Le nom d'utilisateur doit contenir de 3 à 30 lettres, chiffres, points, tirets ou tirets bas");
    }
    value = value.trim();
    if (!value.matches("[\\p{L}0-9_.-]{3,30}")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Le nom d'utilisateur doit contenir de 3 à 30 lettres, chiffres, points, tirets ou tirets bas");
    }
    return value;
  }

  private AuthResponse issueToken(AppUser user) {
    return new AuthResponse(
        jwtService.createToken(user.getUsername()),
        "Bearer",
        jwtService.getExpirationSeconds(),
        user.getUsername());
  }

  public record AuthResponse(
      String accessToken, String tokenType, long expiresIn, String username) {}
}
