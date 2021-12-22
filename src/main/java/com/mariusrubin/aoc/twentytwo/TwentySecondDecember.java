package com.mariusrubin.aoc.twentytwo;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class TwentySecondDecember implements Callable<Integer> {

  private static final String INPUT = "src/main/resources/com/mariusrubin/aoc/TwentySecondDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new TwentySecondDecember());
  }

  @Override
  public Integer call() {

    final var input = new FileLoader(INPUT).allLines();

    final var smallReactor = new ReactorCore(input, 50);

    System.out.printf("Small reactor on cubes: %d%s",
                      smallReactor.countOnCubes(),
                      lineSeparator());

    final var bigReactor = new ReactorCore(input);
    System.out.printf("Big reactor on cubes: %d%s",
                      bigReactor.countOnCubes(),
                      lineSeparator());

    return Executor.SUCCESS;
  }

}