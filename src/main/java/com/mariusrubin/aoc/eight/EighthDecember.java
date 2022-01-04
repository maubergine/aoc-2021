package com.mariusrubin.aoc.eight;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class EighthDecember implements Callable<Integer> {

  private static final String DISPLAY_RECORDS = "src/main/resources/com/mariusrubin/aoc/EighthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new EighthDecember());
  }

  @Override
  public Integer call() {

    final var inputData = new FileLoader(DISPLAY_RECORDS).allLines();

    final int[] lookFor = {1, 4, 7, 8};

    System.out.printf("Number of times %s appear: %d%s",
                      Arrays.toString(lookFor),
                      DisplayAnalyser.countInstancesOf(lookFor, inputData),
                      lineSeparator());

    System.out.printf("Summed output: %d%s",
                      DisplayAnalyser.sumOutput(inputData),
                      lineSeparator());

    return Executor.SUCCESS;

  }
}