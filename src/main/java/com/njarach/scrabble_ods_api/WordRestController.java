package com.njarach.scrabble_ods_api;

import com.njarach.scrabble_ods_api.service.CheatingService;
import com.njarach.scrabble_ods_api.service.WordService;
import java.util.Set;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/words")
class WordRestController {

  private final WordService wordService;
  private final CheatingService cheatingService;

  public WordRestController(WordService wordService, CheatingService cheatingService) {
    this.wordService = wordService;
    this.cheatingService = cheatingService;
  }

  @GetMapping("/check")
  public boolean check(@RequestParam String letters) {
    return this.wordService.wordExists(letters);
  }

  @GetMapping("/cheat")
  public Set<String> cheat(@RequestParam String letters) {
    return this.cheatingService.cheat(letters);
  }
}
