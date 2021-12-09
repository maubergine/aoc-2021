package com.mariusrubin.aoc.nine;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AHeightmapAnalyser {

  private static final int[][] HEIGHTMAP =
      {{2, 1, 9, 9, 9, 4, 3, 2, 1, 0},
       {3, 9, 8, 7, 8, 9, 4, 9, 2, 1},
       {9, 8, 5, 6, 7, 8, 9, 8, 9, 2},
       {8, 7, 6, 7, 8, 9, 6, 7, 8, 9},
       {9, 8, 9, 9, 9, 6, 5, 6, 7, 8}};

  private HeightmapAnalyser underTest = new HeightmapAnalyser(HEIGHTMAP);

  @BeforeEach
  public void init() {
    underTest = new HeightmapAnalyser(HEIGHTMAP);
  }

  @Test
  public void shouldFindLowPoints() {
    assertThat(underTest.findLowPoints()).containsExactly(1, 0, 5, 5);
  }

  @Test
  public void shouldCalculateRiskLevel() {
    assertThat(underTest.calculateRiskLevel()).isEqualTo(15);
  }

  @Test
  public void shouldFindBasins() {
    final var basins = underTest.findBasins();
    assertThat(basins).contains(
        new Basin(new int[]{3, 2, 1}),
        new Basin(new int[]{4, 3, 2, 1, 0, 4, 2, 1, 2}),
        new Basin(new int[]{8, 7, 8, 8, 5, 6, 7, 8, 8, 7, 6, 7, 8, 8}),
        new Basin(new int[]{8, 6, 7, 8, 6, 5, 6, 7, 8})
    );
  }

  @Test
  public void shouldCalculateLargestBasinSize() {
    assertThat(underTest.calculateLargestBasin()).isEqualTo(1134);
  }

}
