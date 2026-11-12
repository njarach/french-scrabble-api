package com.njarach.scrabble_ods_api.service;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CheatingService {

  private static final int MAX_TILES = 7;

  private final WordService wordService;

  public CheatingService(WordService wordService) {
    this.wordService = wordService;
  }

  public Set<String> cheat(String letters) {
    if (letters == null || !letters.matches("(?iu)[\\p{L}]+")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Fournissez uniquement des lettres");
    }
    if (letters.codePointCount(0, letters.length()) > MAX_TILES) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "La recherche est limitée à 7 lettres");
    }

    List<String> arrayOfLetters =
        new ArrayList<>(Arrays.asList(letters.toUpperCase(Locale.ROOT).split("")));
    Set<String> result = new HashSet<>();

    this.generateCombinations(arrayOfLetters, "", result);
    return result;
  }

  private void generateCombinations(
      List<String> remainingLetters, String current, Set<String> result) {

    if (!current.isEmpty() && this.wordService.wordExists(current)) {
      result.add(current);
    }

    Set<String> triedAtThisPosition = new HashSet<>();
    for (int i = 0; i < remainingLetters.size(); i++) {
      List<String> newRemainingLetters = new ArrayList<>(remainingLetters);
      String letter = newRemainingLetters.remove(i);
      // Skip equivalent branches when the rack contains repeated letters.
      if (!triedAtThisPosition.add(letter)) {
        continue;
      }

      this.generateCombinations(newRemainingLetters, current + letter, result);
    }
  }
}
