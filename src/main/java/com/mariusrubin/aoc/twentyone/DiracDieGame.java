package com.mariusrubin.aoc.twentyone;

import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class DiracDieGame extends AbstractDieGame {

  private static final Map<Integer, Long> SCORES        = buildScores();
  private static final int                WINNING_SCORE = 21;

  private boolean hasRun;

  private static Map<Integer, Long> buildScores() {
    return IntStream.rangeClosed(1, 3)
                    .flatMap(i -> IntStream.rangeClosed(1, 3)
                                           .flatMap(j -> IntStream.rangeClosed(1, 3)
                                                                  .map(k -> i + j + k)))
                    .boxed()
                    .collect(Collectors.groupingBy(Function.identity(),
                                                   HashMap::new,
                                                   Collectors.counting()));
  }

  private final Map<GameState, Long> games = new HashMap<>();

  public DiracDieGame(final List<String> input) {
    super(input);
  }

  @Override
  public long calculateWinningScore() {
    return Math.max(playerOneWins(), playerTwoWins());
  }

  @Override
  public long calculateLosingScore() {
    return Math.min(playerOneWins(), playerTwoWins());
  }

  public long playerOneWins() {
    runGame();
    return calculateWins(GameState::getOneScore);
  }

  public long playerTwoWins() {
    runGame();
    return calculateWins(GameState::getTwoScore);
  }

  @Override
  protected int getWinningScore() {
    return WINNING_SCORE;
  }

  private void runGame() {

    if (hasRun) {
      return;
    }

    games.put(new GameState(getPlayerOneStart(), getPlayerTwoStart(), 0, 0), 1L);

    IntStream.iterate(0, i -> hasUnfinishedGames(), i -> (i + 1) % 2).forEach(this::doPlays);

    hasRun = true;
  }

  private long calculateWins(final Function<GameState, Integer> scoreCall) {
    return games.keySet()
                .stream()
                .filter(game -> scoreCall.apply(game) >= WINNING_SCORE)
                .mapToLong(games::get)
                .sum();
  }

  private void doPlays(final int playerFlag) {

    final var toWalk = games.entrySet()
                            .stream()
                            .filter(e -> e.getValue() > 0L)
                            .filter(e -> isInProgress(e.getKey()))
                            .collect(toMap(Entry::getKey, Entry::getValue));

    final BiFunction<GameState, Integer, GameState> playMethod = playerFlag == 0
                                                                 ? GameState::updateOne
                                                                 : GameState::updateTwo;

    toWalk.forEach((state, howMany) -> {
      SCORES.forEach((score, frequency) -> runGames(state, playMethod, howMany, score, frequency));
      games.merge(state, -howMany, Long::sum);
    });

  }

  private void runGames(final GameState state,
                        final BiFunction<GameState, Integer, GameState> player,
                        final long howMany,
                        final int score,
                        final long frequency) {

    final var movedState = player.apply(state, score);
    games.merge(movedState, howMany * frequency, Long::sum);

  }

  private boolean hasUnfinishedGames() {
    return games.entrySet()
                .stream()
                .filter(e -> e.getValue() > 0L)
                .anyMatch(e -> isInProgress(e.getKey()));
  }

}
