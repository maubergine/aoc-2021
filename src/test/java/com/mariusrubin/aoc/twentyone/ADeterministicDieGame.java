package com.mariusrubin.aoc.twentyone;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ADeterministicDieGame {

  private static final String INPUT = """
      Player 1 starting position: 4
      Player 2 starting position: 8
      """;

  private DieGame underTest;

  @BeforeEach
  public void init() {
    underTest = new DeterministicDieGame(INPUT.lines().toList());
  }

  @Test
  public void shouldCalculateLosingScore() {
    assertThat(underTest.calculateLosingScore()).isEqualTo(739785);
  }

  @Test
  public void shouldCalculateWinningScore() {
    assertThat(underTest.calculateWinningScore()).isEqualTo(993000);
  }

}
