package com.quasicontrol.wordle.data;

import com.quasicontrol.wordle.core.CharAt;
import com.quasicontrol.wordle.core.Result;
import com.quasicontrol.wordle.core.WordResult;

import java.util.*;
import java.util.stream.Collectors;

import static com.quasicontrol.wordle.core.Result.HIT;

public class WordleSolver {
  List<String> words;
  private Strategy strategy;
  Set<String> excludedWords;
  public WordleSolver(WordListProvider wordListProvider, Strategy strategy) {
    words = wordListProvider.get();
    this.strategy = strategy;
    excludedWords = new HashSet<>();
  }

  public String guess() {
    return guess(Collections.emptyList());
  }
  public String guess(List<WordResult> results) {
    Set<Character> notFounds = new HashSet<>();
    List<CharAt> hits = new ArrayList<>();
    List<CharAt> misses = new ArrayList<>();
    for (WordResult result : results) {
      for (int i = 0; i < 5; i++) {
        char c = result.guess().charAt(i);
        switch (result.results()[i]) {
          case HIT -> hits.add(new CharAt(c, i));
          case MISS -> misses.add(new CharAt(c, i));
          case NOT_FOUND -> notFounds.add(c);
        }
      }
    }

    List<String> potentialWords = words.stream()
        .filter(w -> isValid(w, hits, misses, notFounds))
        .collect(Collectors.toList());

    return pickWord(potentialWords, hits, misses, notFounds);
  }

  boolean isValid(String word, List<CharAt> hits, List<CharAt> misses, Set<Character> notFounds) {
    if (!containsRequiredLetters(word, hits, misses)) {
      return false;
    }

    Set<CharAt> usedMisses = new HashSet<>();
    Set<CharAt> usedHits = new HashSet<>();


    outer:
    for (int letterIndex = 0; letterIndex < 5; letterIndex++) {

      char letter = word.charAt(letterIndex);

      for (CharAt hit : hits) {
        if (usedHits.contains(hit)) {
          continue ;
        }
        if (hit.idx() == letterIndex) {
          usedHits.add(hit);
          continue outer;
        }
      }

      for (CharAt miss : misses) {
        if (usedMisses.contains(miss)) {
          continue;
        }
        if (miss.c() == letter) {
          if (miss.idx() != letterIndex) {
            usedMisses.add(miss);
            continue outer;
          }
        }
      }
      if (notFounds.contains(letter)) {
        return false;
      }
    }

    if (usedHits.size() != hits.size()) {
      return false;
    }

    return true;
  }

  private boolean containsRequiredLetters(String word, List<CharAt> hits, List<CharAt> misses) {
    Set<Character> uniqueHits = hits.stream().map(CharAt::c).collect(Collectors.toSet());
    Set<Character> uniqueMisses = misses.stream().map(CharAt::c).collect(Collectors.toSet());

    Set<Character> usedMisses = new HashSet<>();
    Set<Character> usedHits = new HashSet<>();

    int uniqueHitCount = 0;
    int uniqueMissCount = 0;

    Set<Character> wordCharacters = new HashSet<>();
    for (char c : word.toCharArray()) {
      if (wordCharacters.contains(c)) {
        continue;
      }
      if (uniqueHits.contains(c)) {
        if (!usedHits.contains(c)) {
          usedHits.add(c);
          uniqueHitCount++;
        }
      }
      if (uniqueMisses.contains(c)) {
        if (!usedMisses.contains(c)) {
          usedMisses.add(c);
          uniqueMissCount++;
        }
      }
    }

    if (uniqueHitCount < uniqueHits.size()) {
      return false;
    }
    if (uniqueMissCount < uniqueMisses.size()) {
      return false;
    }
    return true;
  }

  private String pickWord(List<String> words, List<CharAt> hits, List<CharAt> misses, Set<Character> notFounds) {
    System.out.println(String.format("Picking from %d words", words.size()));
    return switch(strategy) {
      case FIRST -> words.get(0);
      case RANDOM -> words.get(new Random().nextInt(words.size()));
    };
  }
}

