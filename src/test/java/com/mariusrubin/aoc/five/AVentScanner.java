package com.mariusrubin.aoc.five;

import com.mariusrubin.aoc.five.VentScanner.Direction;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AVentScanner {

  private static final List<String> INPUT_SEGMENTS = """
      0,9 -> 5,9
      8,0 -> 0,8
      9,4 -> 3,4
      2,2 -> 2,1
      7,0 -> 7,4
      6,4 -> 2,0
      0,9 -> 2,9
      3,4 -> 1,4
      0,0 -> 8,8
      5,5 -> 8,2"""
      .lines()
      .toList();


  @Test
  public void shouldDetectHorizontalAndVerticalOverlaps() {
    final var underTest = new VentScanner(INPUT_SEGMENTS, Direction.HORIZONTAL, Direction.VERTICAL);
    Assertions.assertThat(underTest.countOverlaps()).isEqualTo(5);
  }

  @Test
  public void shouldDetectAllOverlaps() {
    final var underTest = new VentScanner(INPUT_SEGMENTS);
    Assertions.assertThat(underTest.countOverlaps()).isEqualTo(12);
  }

}
