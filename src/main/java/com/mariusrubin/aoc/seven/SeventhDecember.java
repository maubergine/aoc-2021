package com.mariusrubin.aoc.seven;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class SeventhDecember implements Callable<Integer> {

  private static final String INITIAL_POSITIONS = "src/main/resources/com/mariusrubin/aoc/SeventhDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new SeventhDecember());
  }

  @Override
  public Integer call() {

    final var initialPositions = new FileLoader(INITIAL_POSITIONS).allCommaSeparatedIntegers();

    final CrabArmada efficient         = new EfficientCrabArmada(initialPositions);
    final var        efficientPosition = efficient.findCheapestPosition();

    System.out.printf("Efficient armada cheapest position, fuel cost: %d, %d %s",
                      efficientPosition.position(),
                      efficientPosition.fuel(),
                      lineSeparator());

    final CrabArmada inefficient         = new InefficientCrabArmada(initialPositions);
    final var        inefficientPosition = inefficient.findCheapestPosition();
    System.out.printf("Inefficient armada cheapest position, fuel cost: %d, %d %s",
                      inefficientPosition.position(),
                      inefficientPosition.fuel(),
                      lineSeparator());

    return Executor.SUCCESS;
  }
}
