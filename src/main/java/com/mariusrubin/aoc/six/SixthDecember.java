package com.mariusrubin.aoc.six;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class SixthDecember implements Callable<Integer> {

  private static final String GAMES = "src/main/resources/com/mariusrubin/aoc/SixthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new SixthDecember());
  }

  @Override
  public Integer call() {

    final var initialPosition = new FileLoader(GAMES).allCommaSeparatedIntegers();

    final var school        = new LanternfishSchool(initialPosition);
    final var firstDayCount = 80;
    school.passDays(firstDayCount);

    System.out.printf("Fish count after %d days: %d %s",
                      firstDayCount,
                      school.lanternFishCount(),
                      lineSeparator());

    final var secondDayCount = 256;
    school.passDays(secondDayCount - firstDayCount);

    System.out.printf("Fish count after %d days: %d %s",
                      secondDayCount,
                      school.lanternFishCount(),
                      lineSeparator());

    return Executor.SUCCESS;
  }
}
