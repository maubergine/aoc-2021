package com.mariusrubin.aoc.seventeen;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class SeventeenthDecember implements Callable<Integer> {

  private static final String TARGET_DATA = "src/main/resources/com/mariusrubin/aoc/SeventeenthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new SeventeenthDecember());
  }

  @Override
  public Integer call() {

    final var launcher = new ProbeLauncher(new FileLoader(TARGET_DATA).allLines().get(0));

    System.out.printf("Highest Y position: %d%s", launcher.findHighestYPosition(), lineSeparator());

    System.out.printf("Distinct velocities: %d%s",
                      launcher.countViableVelocities(),
                      lineSeparator());

    return Executor.SUCCESS;
  }

}