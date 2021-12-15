package com.mariusrubin.aoc.fifteen;

import static org.assertj.core.api.Assertions.assertThat;

import com.mariusrubin.aoc.fifteen.CavernNavigator.CavernNode;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ACavernNavigator {

  private static final int[][] INPUT = {
      {1, 1, 6, 3, 7, 5, 1, 7, 4, 2},
      {1, 3, 8, 1, 3, 7, 3, 6, 7, 2},
      {2, 1, 3, 6, 5, 1, 1, 3, 2, 8},
      {3, 6, 9, 4, 9, 3, 1, 5, 6, 9},
      {7, 4, 6, 3, 4, 1, 7, 1, 1, 1},
      {1, 3, 1, 9, 1, 2, 8, 1, 3, 7},
      {1, 3, 5, 9, 9, 1, 2, 4, 2, 1},
      {3, 1, 2, 5, 4, 2, 1, 6, 3, 9},
      {1, 2, 9, 3, 1, 3, 8, 5, 2, 1},
      {2, 3, 1, 1, 9, 4, 4, 5, 8, 1}
  };

  @Test
  public void shouldFindLowestRiskPath() {

    final var underTest = new CavernNavigator(INPUT);

    assertThat(underTest.lowestRiskPath()).containsExactly(
        new CavernNode(1, 0, 0),
        new CavernNode(1, 1, 0),
        new CavernNode(2, 2, 0),
        new CavernNode(1, 2, 1),
        new CavernNode(3, 2, 2),
        new CavernNode(6, 2, 3),
        new CavernNode(5, 2, 4),
        new CavernNode(1, 2, 5),
        new CavernNode(1, 2, 6),
        new CavernNode(1, 3, 6),
        new CavernNode(5, 3, 7),
        new CavernNode(1, 4, 7),
        new CavernNode(1, 4, 8),
        new CavernNode(3, 5, 8),
        new CavernNode(2, 6, 8),
        new CavernNode(3, 7, 8),
        new CavernNode(2, 8, 8),
        new CavernNode(1, 8, 9),
        new CavernNode(1, 9, 9)
    );
  }

  @Test
  public void shouldScoreLowestPath() {
    final var underTest = new CavernNavigator(INPUT);
    assertThat(underTest.scoreLowestPath()).isEqualTo(40L);
  }

  @Test
  public void shouldScoreLowestPathInLargeCavern() {
    final var underTest = new CavernNavigator(new CavernScanner(5).scanCavern(INPUT));
    assertThat(underTest.scoreLowestPath()).isEqualTo(315L);
  }

}