package com.mariusrubin.aoc.thirteen;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AnOrigamiBot {

  private static final String INPUT = """
      6,10
      0,14
      9,10
      0,3
      10,4
      4,11
      6,0
      6,12
      4,1
      0,13
      10,12
      3,4
      3,0
      8,4
      1,10
      2,14
      8,10
      9,0
            
      fold along y=7
      fold along x=5""";

  private OrigamiBot underTest;

  @BeforeEach
  public void init() {
    underTest = new OrigamiBot(INPUT.lines().toList());
  }

  @Test
  public void shouldCountVisibleDots() {
    assertThat(underTest.countDotsAfterFolding(2)).isEqualTo(16);
  }

  @Test
  public void shouldFold() {
    Assertions.assertThat(underTest.fold())
              .containsExactly(
                  """
                      ...#..#..#.
                      ....#......
                      ...........
                      #..........
                      ...#....#.#
                      ...........
                      ...........
                      ...........
                      ...........
                      ...........
                      .#....#.##.
                      ....#......
                      ......#...#
                      #..........
                      #.#........""",
                  """
                      #.##..#..#.
                      #...#......
                      ......#...#
                      #...#......
                      .#.#..#.###
                      ...........
                      ...........""",
                  """
                      #####
                      #...#
                      #...#
                      #...#
                      #####
                      .....
                      .....""");

  }
}