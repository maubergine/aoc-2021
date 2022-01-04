package com.mariusrubin.aoc.fourteen;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class FourteenthDecember implements Callable<Integer> {

  private static final String POLYMER_TEMPLATE = "src/main/resources/com/mariusrubin/aoc/FourteenthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new FourteenthDecember());
  }

  @Override
  public Integer call() {

    final var template = new FileLoader(POLYMER_TEMPLATE).allLines();

    final var polymeriser = new Polymeriser(template);

    final var firstStepCount = 10;
    System.out.printf("Output after %d steps: %d%s",
                      firstStepCount,
                      polymeriser.calculateOutput(firstStepCount),
                      lineSeparator());

    final var secondStepCount = 40;
    System.out.printf("Output after %d steps: %d%s",
                      secondStepCount,
                      polymeriser.calculateOutput(secondStepCount),
                      lineSeparator());

    return Executor.SUCCESS;

  }

}