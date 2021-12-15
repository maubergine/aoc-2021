package com.mariusrubin.aoc.fifteen;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class FifteenthDecember implements Callable<Integer> {

  private static final String CAVERN = "src/main/resources/com/mariusrubin/aoc/FifteenthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new FifteenthDecember());
  }

  @Override
  public Integer call() {

    final var smallCavern = new CavernScanner().scanCavern(CAVERN);
    final var navigator   = new CavernNavigator(smallCavern);

    System.out.printf("Lowest risk path: %d%s",
                      navigator.scoreLowestPath(),
                      lineSeparator());

    final var bigCavern    = new CavernScanner(5).scanCavern(CAVERN);
    final var bigNavigator = new CavernNavigator(bigCavern);

    System.out.printf("Lowest risk path: %d%s",
                      bigNavigator.scoreLowestPath(),
                      lineSeparator());

    return Executor.SUCCESS;

  }

}