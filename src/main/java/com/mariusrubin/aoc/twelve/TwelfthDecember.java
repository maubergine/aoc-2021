package com.mariusrubin.aoc.twelve;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class TwelfthDecember implements Callable<Integer> {

  private static final String CAVE_MAP = "src/main/resources/com/mariusrubin/aoc/TwelfthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new TwelfthDecember());
  }

  @Override
  public Integer call() {

    final var data = new FileLoader(CAVE_MAP).allLines();

    final var oneStop = new Pathfinder(data);
    System.out.printf("Possible one-stop paths: %d%s", oneStop.countPaths(), lineSeparator());

    final var twoStop = new Pathfinder(data, 2);
    System.out.printf("Possible two-stop paths: %d%s", twoStop.countPaths(), lineSeparator());

    return Executor.SUCCESS;

  }

}