package com.mariusrubin.aoc.four;

import static java.lang.System.lineSeparator;

import com.mariusrubin.aoc.util.Executor;
import com.mariusrubin.aoc.util.FileLoader;
import java.util.concurrent.Callable;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class FourthDecember implements Callable<Integer> {

  private static final String GAMES = "src/main/resources/com/mariusrubin/aoc/FourthDecember.txt";

  public static void main(final String... args) {
    Executor.doRun(new FourthDecember());
  }

  @Override
  public Integer call() {

    final var parser    = new BingoInputParser(new FileLoader(GAMES).allLines());
    final var firstGame = new BingoGame(parser.calls(), parser.boards());

    System.out.printf("Score on winning board: %d%s",
                      firstGame.findWinningBoard().getScore(),
                      lineSeparator());

    final var secondGame = new BingoGame(parser.calls(), parser.boards());
    System.out.printf("Score on last board to win: %d%s",
                      secondGame.findLastWinningBoard().getScore(),
                      lineSeparator());

    return Executor.SUCCESS;
  }
}
