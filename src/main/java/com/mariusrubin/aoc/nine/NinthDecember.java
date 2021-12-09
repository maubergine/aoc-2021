package com.mariusrubin.aoc.nine;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class NinthDecember implements Callable<Integer> {

  private static final String HEIGHTMAP        = "src/main/resources/com/mariusrubin/aoc/NinthDecember.txt";
  private static final int    INTERPRET_OFFSET = 48;

  public static void main(final String... args) {
    Executor.doRun(new NinthDecember());
  }

  @Override
  public Integer call() {

    final var heightMap = new FileLoader(HEIGHTMAP).lines()
                                                   .map(String::chars)
                                                   .map(NinthDecember::toIntArray)
                                                   .toArray(int[][]::new);

    final var analyser = new HeightmapAnalyser(heightMap);

    System.out.printf("Risk level: %d%s", analyser.calculateRiskLevel(), lineSeparator());

    System.out.printf("Largest basin: %d%s", analyser.calculateLargestBasin(), lineSeparator());

    return Executor.SUCCESS;

  }

  private static int[] toIntArray(final IntStream charStream) {
    return charStream.map(i -> i - INTERPRET_OFFSET).toArray();
  }
}