package com.mariusrubin.aoc.sixteen;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class SixteenthDecember implements Callable<Integer> {

  private static final String TRANSMISSION = "src/main/resources/com/mariusrubin/aoc/SixteenthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new SixteenthDecember());
  }

  @Override
  public Integer call() {

    final var decoder = new BitsDecoder(new FileLoader(TRANSMISSION).allLines().get(0));

    System.out.printf("Version sum: %d%s", decoder.sumVersions(), lineSeparator());

    System.out.printf("Value: %d%s", decoder.outermostValue(), lineSeparator());

    return Executor.SUCCESS;
  }

}