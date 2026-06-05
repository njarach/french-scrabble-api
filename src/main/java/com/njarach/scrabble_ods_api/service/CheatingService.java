package com.njarach.scrabble_ods_api.service;

import com.njarach.scrabble_ods_api.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CheatingService {

    private final WordRepository wordRepository;

    public CheatingService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    /* My memo for this :
    - I need to split the letters arg into an array of letters
    - I need to initialize an empty array result
    - I need to generate all the possible combinations with a recursive function that checks for words against the db and
    populate the empty result array. The result array is an ArrayList<String>
    - I need to return the result. I need to pass array_unique if I have the same letter in letters multiple times
    - Now to the recursive : it takes 3 arguments -> the array of remaining letters, the current letter, the results array
    to keep populating it
    - It works by first checking the current letter(s) combo. If it's valid (exists)and not empty ("") it adds to the
     result array. Reminder that java uses Objects so even if passed-by-value, I still point to the 'reference' (semantics trap)
     - Then it loops on the remaining letters, by index=>value. It takes those remaining values, and makes a new remaining
     array by unsetting the current index from it. It then calls for the recursive function again which takes in args
     the new remaining array, the current (reminder current starts as an empty "") + appends the value (letter), and then
     the array result by reference.
     */

    public Set<String> cheat(String letters) {
        List<String> arrayOfLetters = new ArrayList<>(Arrays.asList(letters.split("")));
        // Reminder, Set ensures no duplicates
       Set<String> result = new HashSet<>();

       this.generateCombinations(arrayOfLetters,"", result);

       return result;
    }

    private void generateCombinations(List<String> remainingLetters, String current, Set<String> result) {

        String stringifiedRemainingLetters = remainingLetters.toString();
        if (!current.isEmpty()  && this.wordRepository.existsWordByLetters(stringifiedRemainingLetters)) {
            result.add(current);
        }

        for (int i = 0; i < remainingLetters.size(); i++) {
            List<String> newRemainingLetters = remainingLetters;
            newRemainingLetters.remove(i);

            this.generateCombinations(newRemainingLetters,"".concat(current),result);
        }
    }
}
