package com.quasicontrol.wordle;

import com.google.common.collect.ImmutableList;
import com.quasicontrol.wordle.core.Result;
import com.quasicontrol.wordle.core.WordResult;
import com.quasicontrol.wordle.data.Strategy;
import com.quasicontrol.wordle.data.WordListProvider;
import com.quasicontrol.wordle.data.WordleSolver;

import java.util.List;

import static com.quasicontrol.wordle.core.Result.HIT;
import static com.quasicontrol.wordle.core.Result.MISS;
import static com.quasicontrol.wordle.core.Result.NOT_FOUND;

public class Main {
  public static void main(String[] args) {
    WordListProvider wordListProvider =  new WordListProvider();
    System.out.println(String.format("loaded %d words", wordListProvider.get().size()));
    WordleSolver solver = new WordleSolver(wordListProvider, Strategy.FIRST);
    String word = solver.guess(
        ImmutableList.of(
            new WordResult("cigar", new Result[]{NOT_FOUND, NOT_FOUND, NOT_FOUND, NOT_FOUND, MISS}),
            new WordResult("rebut", new Result[]{MISS, NOT_FOUND, NOT_FOUND, MISS, MISS})
            //            new WordResult("humph", new Result[]{NOT_FOUND, NOT_FOUND, MISS, NOT_FOUND, HIT}),
//            new WordResult("stums", new Result[]{MISS, MISS, HIT, NOT_FOUND, HIT})
        )
    );

    System.out.println(word);
  }
}
