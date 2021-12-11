package com.mariusrubin.aoc.nine;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class NinthDecember implements Callable<Integer> {

  private static final String HEIGHTMAP = "src/main/resources/com/mariusrubin/aoc/NinthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new NinthDecember());
  }

  @Override
  public Integer call() {

    final var heightMap = new FileLoader(HEIGHTMAP).integerArray();

    final var analyser = new HeightmapAnalyser(heightMap);

    System.out.printf("Risk level: %d%s", analyser.calculateRiskLevel(), lineSeparator());

    System.out.printf("Largest basin: %d%s", analyser.calculateLargestBasin(), lineSeparator());

    return Executor.SUCCESS;

  }

}