package com.mariusrubin.aoc.twentythree;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class TwentyThirdDecember implements Callable<Integer> {

  private static final String INPUT = "src/main/resources/com/mariusrubin/aoc/TwentyThirdDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new TwentyThirdDecember());
  }

  @Override
  public Integer call() {

    final var input = new ArrayList<>(new FileLoader(INPUT).allLines());

    var mover = new AmphipodMover(input);
    System.out.printf("Lowest energy route: %d%s", mover.calculateLowestEnergy(), lineSeparator());

    input.add(3, "#D#B#A#C#");
    input.add(3, "#D#C#B#A#");
    mover = new AmphipodMover(input);
    System.out.printf("Lowest energy route for complex: %d%s",
                      mover.calculateLowestEnergy(),
                      lineSeparator());

    return Executor.SUCCESS;
  }

}