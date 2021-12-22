package com.mariusrubin.aoc.twentyone;

import static java.lang.Integer.parseInt;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public abstract class AbstractDieGame implements DieGame {

  private static final Pattern INPUT = Pattern.compile("Player [0-9] starting position: ([0-9])");

  private final int playerOneStart;
  private final int playerTwoStart;

  protected AbstractDieGame(final List<String> input) {

    final var match1 = INPUT.matcher(input.get(0));
    final var match2 = INPUT.matcher(input.get(1));

    if (!match1.find()) {
      throw new IllegalArgumentException("Could not find player score in " + input.get(0));
    }
    if (!match2.find()) {
      throw new IllegalArgumentException("Could not find player score in " + input.get(1));

    }

    playerOneStart = parseInt(match1.group(1));
    playerTwoStart = parseInt(match2.group(1));

  }

  protected int getPlayerOneStart() {
    return playerOneStart;
  }

  protected int getPlayerTwoStart() {
    return playerTwoStart;
  }

  protected boolean isInProgress(final GameState state) {
    return state.getOneScore() < getWinningScore() && state.getTwoScore() < getWinningScore();
  }

  protected abstract int getWinningScore();

  protected static class GameState {

    private final int onePosition;
    private final int twoPosition;
    private final int oneScore;
    private final int twoScore;

    protected GameState(final int onePosition,
                        final int twoPosition,
                        final int oneScore,
                        final int twoScore) {
      this.onePosition = onePosition;
      this.twoPosition = twoPosition;
      this.oneScore = oneScore;
      this.twoScore = twoScore;
    }

    protected int getOnePosition() {
      return onePosition;
    }

    protected int getTwoPosition() {
      return twoPosition;
    }

    protected int getOneScore() {
      return oneScore;
    }

    protected int getTwoScore() {
      return twoScore;
    }

    protected GameState updateOne(final int dieRollTotal) {
      final var newPosition = findPosition(onePosition, dieRollTotal);
      return new GameState(newPosition, twoPosition, oneScore + newPosition, twoScore);
    }

    protected GameState updateTwo(final int dieRollTotal) {
      final var newPosition = findPosition(twoPosition, dieRollTotal);
      return new GameState(onePosition, newPosition, oneScore, twoScore + newPosition);
    }

    private static int findPosition(final int playerPosition, final int dieRollTotal) {

      final var newPosition = playerPosition + dieRollTotal;

      if (newPosition % 10 == 0) {
        return 10;
      }
      return newPosition % 10;

    }


    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      final GameState gameState = (GameState) o;
      return onePosition == gameState.onePosition
             && twoPosition == gameState.twoPosition
             && oneScore == gameState.oneScore
             && twoScore == gameState.twoScore;
    }

    @Override
    public int hashCode() {
      return Objects.hash(onePosition, twoPosition, oneScore, twoScore);
    }
  }

}
