package com.njarach.scrabble_ods_api.service;

import com.njarach.scrabble_ods_api.repository.WordRepository;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WordService {
  private final WordRepository wordRepository;

  public WordService(WordRepository wordRepository) {
    this.wordRepository = wordRepository;
  }

  public boolean wordExists(String letters) {
    validateLetters(letters, 15);
    return wordRepository.existsWordByLetters(letters.toUpperCase(Locale.ROOT));
  }

  // This will be useful for the god-forsaken cheating feature
  public String findWordByLetters(String letters) {
    validateLetters(letters, 15);
    return wordRepository.findWordByLetters(letters.toUpperCase(Locale.ROOT)).getLetters();
  }

  private void validateLetters(String letters, int maxLength) {
    if (letters == null || !letters.matches("(?iu)[\\p{L}]{1," + maxLength + "}")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Le mot doit contenir de 1 à 15 lettres");
    }
  }
}
