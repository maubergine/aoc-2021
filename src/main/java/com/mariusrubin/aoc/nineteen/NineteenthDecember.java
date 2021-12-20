package com.mariusrubin.aoc.nineteen;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class NineteenthDecember implements Callable<Integer> {

  private static final String SCANS = "src/main/resources/com/mariusrubin/aoc/NineteenthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new NineteenthDecember());
  }

  @Override
  public Integer call() {

    final var mapper = new BeaconMapper(new FileLoader(SCANS).allLines());

    System.out.printf("Beacon total: %d%s", mapper.countBeacons(), lineSeparator());

    System.out.printf("Largest manhattan distance: %d%s",
                      mapper.getLargestManhattanDistance(),
                      lineSeparator());

    return Executor.SUCCESS;
  }

}