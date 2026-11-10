package com.njarach.scrabble_ods_api.service;

import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class CheatingService {

  private final WordService wordService;

  public CheatingService(WordService wordService) {
    this.wordService = wordService;
  }

  /* My memo for this :
  - I need to split the letters arg into an array of letters
  - I need to initialize an empty Set result because I don't want duplicates
  - I need to generate all the possible combinations with a recursive function that checks for words against the db and
  populate results.
  - Recursive takes 3 arguments -> remaining letters, current letter(s), results Set (to keep populating it)
  - 1.First check if current letter(s) exist in scrabble dictionnary and if yes add it to results
  - 2 For all remaining letters, do it recursively as long as there are remaining letters.
  - 3 ie : 'enoz' -> e ? en ? eno ? enz ? enoz ? enzo ? eo ? eon ? eoz ? eonz ? eozn ? ez ? ezo ? ezn ? ezon ? ezno ?
      n ? no ? noz ? noe ? noze ? noez ? nz ? nze ? nzo ? nzeo ? nzoe ? ne ? nez ? neo ? nezo ? neoz ?
      o ? oe ? oen ? oez ? oezn ? oenz ? on ? onz ? one ? onze ? onez ? oz ? oze ? ozn ? ozen ? ozne ?
      z ? ze ? zen ? zne ? zeno ? zeon ? zn ? zne ? zno ? zneo ? znoe ? zo ? zoe ? zon ? zoen ? zone ?
   */

  public Set<String> cheat(String letters) {
    List<String> arrayOfLetters = new ArrayList<>(Arrays.asList(letters.split("")));
    System.out.println(arrayOfLetters);
    // Reminder, Set ensures no duplicates
    Set<String> result = new HashSet<String>();

    this.generateCombinations(arrayOfLetters, "", result);
    return result;
  }

  private void generateCombinations(
      List<String> remainingLetters, String current, Set<String> result) {

    if (this.wordService.wordExists(current)) {
      System.out.println(current);
      result.add(current);
    }

    for (int i = 0; i < remainingLetters.size(); i++) {
      List<String> newRemainingLetters = new ArrayList<>(remainingLetters);
      String letter =
          newRemainingLetters.remove(
              i); // Ceci retire, ET récupère la lettre, donc on peut ensuite concaténer

      this.generateCombinations(newRemainingLetters, current + letter, result);
    }
  }
}
