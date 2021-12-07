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

    final var school = new LanternfishSchool(initialPosition);
    school.passDays(80);

    System.out.printf("Fish count after 80 days: %d %s",
                      school.lanternFishCount(),
                      lineSeparator());

    school.passDays(256 - 80);

    System.out.printf("Fish count after 256 days: %d %s",
                      school.lanternFishCount(),
                      lineSeparator());

    return Executor.SUCCESS;
  }
}
