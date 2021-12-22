package com.mariusrubin.aoc.twentyone;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ADiracDieGame {

  private static final String INPUT = """
      Player 1 starting position: 4
      Player 2 starting position: 8
      """;

  private DieGame underTest;

  @BeforeEach
  public void init() {
    underTest = new DiracDieGame(INPUT.lines().toList());
  }

  @Test
  public void shouldCalculatePlayerOneWins() {
    assertThat(underTest.calculateWinningScore()).isEqualTo(444356092776315L);
  }

  @Test
  public void shouldCalculatePlayerTwoLoses() {
    assertThat(underTest.calculateLosingScore()).isEqualTo(341960390180808L);
  }

}
