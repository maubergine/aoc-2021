package com.mariusrubin.aoc.five;

import static com.mariusrubin.aoc.five.VentScanner.Direction.HORIZONTAL;
import static com.mariusrubin.aoc.five.VentScanner.Direction.VERTICAL;
import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class FifthDecember implements Callable<Integer> {

  private static final String VENTS = "src/main/resources/com/mariusrubin/aoc/FifthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new FifthDecember());
  }

  @Override
  public Integer call() {

    final var simple = new VentScanner(new FileLoader(VENTS).allLines(), HORIZONTAL, VERTICAL);

    System.out.printf("Scanner detected overlaps: %d%s",
                      simple.countOverlaps(),
                      lineSeparator());

    final var complex = new VentScanner(new FileLoader(VENTS).allLines());
    System.out.printf("Horizontal scanner detected overlaps: %d%s",
                      complex.countOverlaps(),
                      lineSeparator());

    return Executor.SUCCESS;

  }
}