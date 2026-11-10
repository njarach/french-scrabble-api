package com.njarach.scrabble_ods_api.service;

import com.njarach.scrabble_ods_api.model.Word;
import com.njarach.scrabble_ods_api.repository.WordRepository;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/*On utilise ceci pour créer le jeu de données en dev local. On pourrait créer un fichier sql qui s'exécute auto avec
un jeu de données tests, mais on veut utiliser l'appli 100% en local aussi potentiellement. */
@Component
class DataLoaderComponent implements CommandLineRunner {
  private final WordRepository wordRepository;

  public DataLoaderComponent(WordRepository wordRepository) {
    this.wordRepository = wordRepository;
  }

  @Override
  public void run(String... args) throws Exception {
    if (wordRepository.count() > 0) {
      System.out.println("DB déjà chargée, skip.");
      return;
    }

    System.out.println("Chargement ODS9...");

    var reader =
        new BufferedReader(
            new InputStreamReader(
                Objects.requireNonNull(
                    getClass().getClassLoader().getResourceAsStream("ods9.txt"))));

    List<Word> batch = new ArrayList<>();

    String line;
    while ((line = reader.readLine()) != null) {
      line = line.trim();
      if (!line.isBlank()) {
        batch.add(new Word(line));

        if (batch.size() == 1000) {
          wordRepository.saveAll(batch);
          batch.clear();
        }
      }
    }

    if (!batch.isEmpty()) {
      wordRepository.saveAll(batch);
    }

    System.out.println("ODS9 chargé : " + wordRepository.count() + " mots.");
  }
}
