package com.njarach.scrabble_ods_api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTests {
  @Autowired private MockMvc mockMvc;

  @Test
  void registrationIssuesTokenAndTokenAllowsScrabbleApiAccess() throws Exception {
    String username = "joueur-" + UUID.randomUUID().toString().substring(0, 8);
    MvcResult result =
        mockMvc
            .perform(
                post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(credentials(username, "motdepasse123")))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andExpect(jsonPath("$.username").value(username))
            .andReturn();

    String token =
        com.fasterxml.jackson.databind.json.JsonMapper.builder()
            .build()
            .readTree(result.getResponse().getContentAsString())
            .get("accessToken")
            .asText();

    mockMvc
        .perform(
            get("/api/words/check")
                .param("letters", "ZZZZ")
                .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());
  }

  @Test
  void scrabbleApiRejectsAnonymousAndInvalidJwtRequests() throws Exception {
    mockMvc
        .perform(get("/api/words/check").param("letters", "CHAT"))
        .andExpect(status().isUnauthorized());
    mockMvc
        .perform(
            get("/api/words/check")
                .param("letters", "CHAT")
                .header("Authorization", "Bearer jeton-invalide"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void loginReturnsTokenAndRejectsWrongPassword() throws Exception {
    String username = "joueur-" + UUID.randomUUID().toString().substring(0, 8);
    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(username, "motdepasse123")))
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(username, "motdepasse123")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").isNotEmpty());

    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials(username, "incorrect")))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void registrationRejectsShortPasswords() throws Exception {
    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials("joueur-valide", "court")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void registrationRejectsNullAndOversizedCredentials() throws Exception {
    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":null,\"password\":\"motdepasse123\"}"))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials("joueur-valide", "é".repeat(37))))
        .andExpect(status().isBadRequest());
  }

  private String credentials(String username, String password) {
    return "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
  }
}
