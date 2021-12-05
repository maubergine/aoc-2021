package com.mariusrubin.aoc.four;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ABingoInputParser {

  private static final String INPUT = """
      7,4,9,5,11,17,23,2,0,14,21,24,10,16
            
      22 13 17 11  0
       8  2 23  4 24
      21  9 14 16  7
       6 10  3 18  5
       1 12 20 15 19
            
       3 15  0  2 22
       9 18 13 17  5
      19  8  7 25 23
      20 11 10 24  4
      14 21 16 12  6
            
      14 21 17 24  4
      10 16 15  9 19
      18  8 23 26 20
      22 11 13  6  5
       2  0 12  3  7
      """;

  private BingoInputParser underTest;

  @BeforeEach
  public void init() {
    underTest = new BingoInputParser(INPUT.lines().toList());
  }


  @Test
  public void shouldParseCalls() {
    assertThat(underTest.calls()).containsExactly(7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10, 16);
  }

  @Test
  public void shouldParseBingoBoards() {
    assertThat(underTest.boards().get(0).getNumberAtPosition(3, 2)).isEqualTo(3);
    assertThat(underTest.boards().get(1).getNumberAtPosition(1, 4)).isEqualTo(9);
    assertThat(underTest.boards().get(2).getNumberAtPosition(3, 3)).isEqualTo(23);
  }

}
