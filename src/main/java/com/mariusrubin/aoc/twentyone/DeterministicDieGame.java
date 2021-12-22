package com.mariusrubin.aoc.twentyone;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class DeterministicDieGame extends AbstractDieGame {

  private static final int WINNING_SCORE = 1000;

  public DeterministicDieGame(final List<String> input) {
    super(input);
  }

  @Override
  public long calculateWinningScore() {
    return calculateScore(Math::max);
  }

  @Override
  public long calculateLosingScore() {
    return calculateScore(Math::min);
  }

  @Override
  protected int getWinningScore() {
    return WINNING_SCORE;
  }

  private long calculateScore(final BiFunction<Integer, Integer, Integer> op) {
    final var result = runGame();
    return (long) op.apply(result.state().getOneScore(), result.state().getTwoScore())
           * result.rolls();
  }


  private StateRoll runGame() {

    final var dieValue  = new AtomicInteger(1);
    final var diceRolls = new AtomicInteger();

    final var state = new AtomicReference<>(new GameState(getPlayerOneStart(),
                                                          getPlayerTwoStart(),
                                                          0,
                                                          0));

    IntStream.iterate(0, i -> isInProgress(state.get()), i -> (i + 1) % 2).forEach(player -> {

      final BiFunction<GameState, Integer, GameState> update = player == 0
                                                               ? GameState::updateOne
                                                               : GameState::updateTwo;

      final var dieRollTotal = IntStream.range(0, 3)
                                        .map(i -> dieValue.getAndIncrement())
                                        .sum();

      diceRolls.addAndGet(3);

      state.set(update.apply(state.get(), dieRollTotal));

    });

    return new StateRoll(state.get(), diceRolls.get());

  }

  private record StateRoll(GameState state, int rolls) {

  }


}
