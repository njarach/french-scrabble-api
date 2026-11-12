package com.njarach.scrabble_ods_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

class CheatingServiceTests {

  @Test
  void cheatSkipsEquivalentBranchesForRepeatedLetters() {
    List<String> checkedWords = new ArrayList<>();
    WordService wordService =
        new WordService(null) {
          @Override
          public boolean wordExists(String letters) {
            checkedWords.add(letters);
            return letters.equals("AA");
          }
        };

    Set<String> result = new CheatingService(wordService).cheat("AAA");

    assertEquals(Set.of("AA"), result);
    assertEquals(List.of("A", "AA", "AAA"), checkedWords);
  }

  @Test
  void cheatRejectsMoreThanSevenTilesBeforeSearching() {
    WordService wordService =
        new WordService(null) {
          @Override
          public boolean wordExists(String letters) {
            throw new AssertionError("Aucune recherche ne doit démarrer");
          }
        };

    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class,
            () -> new CheatingService(wordService).cheat("ABCDEFGH"));

    assertEquals(400, exception.getStatusCode().value());
  }

  @Test
  void cheatRejectsNonLetterInputBeforeSearching() {
    WordService wordService =
        new WordService(null) {
          @Override
          public boolean wordExists(String letters) {
            throw new AssertionError("Aucune recherche ne doit démarrer");
          }
        };

    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class, () -> new CheatingService(wordService).cheat("AB1"));

    assertEquals(400, exception.getStatusCode().value());
  }
}
