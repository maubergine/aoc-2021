package com.mariusrubin.aoc.four;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ABingoBoard {

  private static final int[][] INPUT_BOARD = {{14, 21, 17, 24, 4},
                                              {10, 16, 15, 9, 19},
                                              {18, 8, 23, 26, 20},
                                              {22, 11, 13, 6, 5},
                                              {2, 0, 12, 3, 7}};

  private BingoBoard underTest;

  @BeforeEach
  public void init() {
    underTest = new BingoBoard(INPUT_BOARD);
  }

  @Test
  public void shouldRetrieveNumbersAtPosition() {
    assertThat(underTest.getNumberAtPosition(4, 3)).isEqualTo(26);
    assertThat(underTest.getNumberAtPosition(2, 4)).isEqualTo(16);
  }

  @Test
  public void shouldCalculateAScore() {
    final var winningNo = IntStream.of(7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10)
                                   .map(underTest::call)
                                   .filter(i -> i >= 0)
                                   .findFirst()
                                   .orElse(-1);

    assertThat(winningNo).isEqualTo(24);
    assertThat(underTest.getScore()).isEqualTo(4512);
  }

  @Test
  public void shouldDetectHorizontalBingos() {
    assertThat(underTest.call(18)).isNegative();
    assertThat(underTest.call(8)).isNegative();
    assertThat(underTest.call(23)).isNegative();
    assertThat(underTest.call(26)).isNegative();
    assertThat(underTest.call(20)).isEqualTo(20);
  }

  @Test
  public void shouldDetectVerticalBingos() {
    assertThat(underTest.call(21)).isNegative();
    assertThat(underTest.call(16)).isNegative();
    assertThat(underTest.call(11)).isNegative();
    assertThat(underTest.call(8)).isNegative();
    assertThat(underTest.call(0)).isZero();
  }

}
