package com.njarach.scrabble_ods_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Word {

  // Constructeur utile ici pour définir facilement letters. Le constructeur vide généré par Lombok
  // est utile pour JPA
  public Word(String letters) {
    this.letters = letters;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  String letters;
}
