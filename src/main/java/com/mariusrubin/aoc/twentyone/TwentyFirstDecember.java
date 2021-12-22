package com.mariusrubin.aoc.twentyone;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class TwentyFirstDecember implements Callable<Integer> {

  private static final String INPUT = "src/main/resources/com/mariusrubin/aoc/TwentyFirstDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new TwentyFirstDecember());
  }

  @Override
  public Integer call() {

    final var input = new FileLoader(INPUT).allLines();

    final var deterministic = new DeterministicDieGame(input);
    final var dirac         = new DiracDieGame(input);

    System.out.printf("Calculated score: %d%s",
                      deterministic.calculateLosingScore(),
                      lineSeparator());

    System.out.printf("Winning universe count:: %d%s",
                      dirac.calculateWinningScore(),
                      lineSeparator());

    return Executor.SUCCESS;
  }

}