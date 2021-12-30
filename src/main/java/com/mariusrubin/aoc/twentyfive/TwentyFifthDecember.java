package com.mariusrubin.aoc.twentyfive;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class TwentyFifthDecember implements Callable<Integer> {

  private static final String INPUT = "src/main/resources/com/mariusrubin/aoc/TwentyFifthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new TwentyFifthDecember());
  }

  @Override
  public Integer call() {

    final var input = new ArrayList<>(new FileLoader(INPUT).allLines());

    final var zone = new SeaCucumberZone(input);
    System.out.printf("Maximum moves: %d%s",
                      zone.countMaxMoves(),
                      lineSeparator());

    return Executor.SUCCESS;
  }

}