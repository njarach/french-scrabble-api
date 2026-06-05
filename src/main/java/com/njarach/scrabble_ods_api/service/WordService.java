package com.njarach.scrabble_ods_api.service;

import com.njarach.scrabble_ods_api.repository.WordRepository;
import org.springframework.stereotype.Service;

@Service
public class WordService {
    private final WordRepository wordRepository;
    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public boolean wordExists(String letters) {
        return wordRepository.existsWordByLetters(letters.toUpperCase());
    }

    // This will be useful for the god-forsaken cheating feature
    public String findWordByLetters(String letters) {
        return wordRepository.findWordByLetters(letters.toUpperCase()).getLetters();
    }
}
