package com.mariusrubin.aoc.thirteen;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ThirteenthDecember implements Callable<Integer> {

  private static final String INSTRUCTIONS = "src/main/resources/com/mariusrubin/aoc/ThirteenthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new ThirteenthDecember());
  }

  @Override
  public Integer call() {

    final var data = new FileLoader(INSTRUCTIONS).allLines();

    final var bot = new OrigamiBot(data);

    System.out.printf("Dots after 1 fold: %d%s", bot.countDotsAfterFolding(1), lineSeparator());

    final var fullFold    = new OrigamiBot(data);
    final var finalOutput = fullFold.fold().toList();

    System.out.printf("Final output:%s%s%s",
                      lineSeparator(),
                      finalOutput.get(finalOutput.size() - 1),
                      lineSeparator());

    return Executor.SUCCESS;

  }

}