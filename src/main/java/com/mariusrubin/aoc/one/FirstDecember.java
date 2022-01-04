package com.mariusrubin.aoc.one;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class FirstDecember implements Callable<Integer> {

  private static final String FIRST_DECEMBER_TXT = "src/main/resources/com/mariusrubin/aoc/FirstDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new FirstDecember());
  }

  private static int countIncreases(final int window) {
    final var loader = new FileLoader(FIRST_DECEMBER_TXT);
    return SonarCounter.countIncreases(loader.allIntegers(), window);
  }

  @Override
  public Integer call() {

    final var firstWindow = 1;
    final var firstPuzzle = countIncreases(firstWindow);
    System.out.printf("Noisy counter: %d increases%s", firstPuzzle, lineSeparator());

    final var secondWindow = 3;
    final var secondPuzzle = countIncreases(secondWindow);
    System.out.printf("Smooth counter: %d increases%s", secondPuzzle, lineSeparator());

    return Executor.SUCCESS;
  }

}
