package com.njarach.scrabble_ods_api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthService.AuthResponse register(@RequestBody Credentials credentials) {
    return authService.register(credentials);
  }

  @PostMapping("/login")
  public AuthService.AuthResponse login(@RequestBody Credentials credentials) {
    return authService.login(credentials);
  }

  public record Credentials(String username, String password) {}
}
