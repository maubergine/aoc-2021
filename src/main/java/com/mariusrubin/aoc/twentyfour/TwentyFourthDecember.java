package com.mariusrubin.aoc.twentyfour;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class TwentyFourthDecember implements Callable<Integer> {

  private static final String INPUT = "src/main/resources/com/mariusrubin/aoc/TwentyFourthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new TwentyFourthDecember());
  }

  @Override
  public Integer call() {

    final var input = new ArrayList<>(new FileLoader(INPUT).allLines());

    final var processor = new MonadProcessor(input);
    System.out.printf("Largest valid number: %d%s",
                      processor.findLargestValidNumber(),
                      lineSeparator());

    System.out.printf("Smallest valid number: %d%s",
                      processor.findSmallestValidNumber(),
                      lineSeparator());

    return Executor.SUCCESS;
  }

}