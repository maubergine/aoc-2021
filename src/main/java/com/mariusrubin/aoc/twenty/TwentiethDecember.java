package com.mariusrubin.aoc.twenty;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class TwentiethDecember implements Callable<Integer> {

  private static final String INPUT = "src/main/resources/com/mariusrubin/aoc/TwentiethDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new TwentiethDecember());
  }

  @Override
  public Integer call() {

    final var lines = new FileLoader(INPUT).allLines();

    final var enhancer = new ImageEnhancer(lines.get(0));
    final var input    = lines.stream().sequential().skip(2).toList();

    System.out.printf("Lit cells after 2: %d%s", enhancer.countLitCells(input, 2), lineSeparator());

    System.out.printf("Lit cells after 50: %d%s",
                      enhancer.countLitCells(input, 50),
                      lineSeparator());

    return Executor.SUCCESS;
  }

}