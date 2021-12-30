package com.mariusrubin.aoc.twentyfive;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ASeaCucumberZone {

  private SeaCucumberZone underTest;


  @Test
  public void shouldProcessMoves() {
    final var input = """
        ...>...
        .......
        ......>
        v.....>
        ......>
        .......
        ..vvv..       
        """;
    underTest = new SeaCucumberZone(input.lines().toList());
    assertThat(underTest.states(4)).containsExactly(
        """
            ..vv>..
            .......
            >......
            v.....>
            >......
            .......
            ....v..""",
        """
            ....v>.
            ..vv...
            .>.....
            ......>
            v>.....
            .......
            .......""",
        """
            ......>
            ..v.v..
            ..>v...
            >......
            ..>....
            v......
            .......""",
        """
            >......
            ..v....
            ..>.v..
            .>.v...
            ...>...
            .......
            v......""");
  }

  @Test
  public void shouldCountMaxMoves() {
    final var input = """
        v...>>.vv>
        .vv>>.vv..
        >>.>v>...v
        >>v>>.>.v.
        v>v.vv.v..
        >.>>..v...
        .vv..>.>v.
        v.v..>>v.v
        ....v..v.>
        """;
    underTest = new SeaCucumberZone(input.lines().toList());
    assertThat(underTest.countMaxMoves()).isEqualTo(58);
  }


}
