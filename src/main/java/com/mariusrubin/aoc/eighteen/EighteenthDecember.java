package com.mariusrubin.aoc.eighteen;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class EighteenthDecember implements Callable<Integer> {

  private static final String NUMBERS = "src/main/resources/com/mariusrubin/aoc/EighteenthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new EighteenthDecember());
  }

  @Override
  public Integer call() {

    final var inputs = new FileLoader(NUMBERS).allLines();

    System.out.printf("Magnitude: %d%s",
                      SnailfishCalculator.calculateMagnitude(inputs),
                      lineSeparator());

    System.out.printf("Largest magnitude: %d%s",
                      SnailfishCalculator.calculateLargestMagnitude(inputs),
                      lineSeparator());

    return Executor.SUCCESS;
  }

}