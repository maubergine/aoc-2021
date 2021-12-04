package com.mariusrubin.aoc.four;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ABingoBoard {

  private static final int[][] INPUT_BOARD = {
      {22, 13, 17, 11, 0},
      {8, 2, 23, 4, 24},
      {21, 9, 14, 16, 7},
      {6, 10, 3, 18, 5},
      {1, 12, 20, 15, 19}
  };

  private BingoBoard underTest;

  @BeforeEach
  public void init() {
    underTest = new BingoBoard(INPUT_BOARD);
  }

  @Test
  public void shouldRetrieveNumbersAtPosition() {
    assertThat(underTest.getNumberAtPosition(4, 3)).isEqualTo(16);
    assertThat(underTest.getNumberAtPosition(2, 4)).isEqualTo(2);
  }

  @Test
  public void shouldMatch() {
    assertThat(underTest.getNumberAtPosition(4, 3)).isEqualTo(16);
    assertThat(underTest.getNumberAtPosition(2, 4)).isEqualTo(2);
  }

  @Test
  public void shouldDetectHorizontalBingos() {
    assertThat(underTest.mark(17)).isFalse();
    assertThat(underTest.mark(13)).isFalse();
    assertThat(underTest.mark(11)).isFalse();
    assertThat(underTest.mark(22)).isFalse();
    assertThat(underTest.mark(0)).isTrue();
  }

  @Test
  public void shouldDetectVerticalBingos() {
    assertThat(underTest.mark(9)).isFalse();
    assertThat(underTest.mark(13)).isFalse();
    assertThat(underTest.mark(12)).isFalse();
    assertThat(underTest.mark(2)).isFalse();
    assertThat(underTest.mark(10)).isTrue();
  }

}
