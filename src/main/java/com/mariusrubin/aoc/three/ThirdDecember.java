package com.mariusrubin.aoc.three;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ThirdDecember implements Callable<Integer> {

  private static final String READINGS = "src/main/resources/com/mariusrubin/aoc/ThirdDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new ThirdDecember());
  }

  @Override
  public Integer call() {

    final var readings = new FileLoader(READINGS).allBinaryIntegers();

    final var monitor = new PowerMonitor(readings);

    System.out.printf("Monitor power reading: %d%s",
                      monitor.getPowerConsumption(),
                      lineSeparator());
    System.out.printf("Monitor life support reading: %d%s",
                      monitor.getLifeSupportRating(),
                      lineSeparator());

    return Executor.SUCCESS;
  }
}
