package com.mariusrubin.aoc.twenty;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AnImageEnhancer {

  private static final String ALGORITHM = """
      ..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##\
      #..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###\
      .######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.\
      .#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....\
      .#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..\
      ...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....\
      ..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#""";

  private static final char[][] INPUT =
      {
          {'#', '.', '.', '#', '.'},
          {'#', '.', '.', '.', '.'},
          {'#', '#', '.', '.', '#'},
          {'.', '.', '#', '.', '.'},
          {'.', '.', '#', '#', '#'}
      };

  private ImageEnhancer underTest;

  @BeforeEach
  public void init() {
    underTest = new ImageEnhancer(ALGORITHM);
  }

  @Test
  public void shouldEnhanceImages() {

    final var expected = """
        ...............
        ...............
        ...............
        ..........#....
        ....#..#.#.....
        ...#.#...###...
        ...#...##.#....
        ...#.....#.#...
        ....#.#####....
        .....#.#####...
        ......##.##....
        .......###.....
        ...............
        ...............
        ...............""";

    final var result = underTest.enhanceAsString(INPUT, 2);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  public void shouldCountLitCells() {
    assertThat(underTest.countLitCells(INPUT, 2)).isEqualTo(35L);
  }

}
