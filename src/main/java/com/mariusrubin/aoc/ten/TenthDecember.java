package com.mariusrubin.aoc.ten;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class TenthDecember implements Callable<Integer> {

  private static final String NAVDATA = "src/main/resources/com/mariusrubin/aoc/TenthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new TenthDecember());
  }

  @Override
  public Integer call() {

    final var data = new FileLoader(NAVDATA).allLines();

    final var parser = new NavigationParser(data);

    System.out.printf("Illegality score: %d%s", parser.calculateIllegalityScore(), lineSeparator());

    System.out.printf("Completion score: %d%s", parser.calculateCompletionScore(), lineSeparator());

    return Executor.SUCCESS;

  }

}