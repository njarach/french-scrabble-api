package com.njarach.scrabble_ods_api.repository;

import com.njarach.scrabble_ods_api.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<Word, Long> {
    boolean existsWordByLetters(String letters);

    Word findWordByLetters(String letters);
}
