package com.mariusrubin.aoc.twelve;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class APathfinder {

  private static final List<String> INPUT = Stream.of(
      "start-A",
      "start-b",
      "A-c",
      "A-b",
      "b-d",
      "A-end",
      "b-end").toList();

  @Test
  public void shouldFindPaths() {
    final var underTest = new Pathfinder(INPUT);
    assertThat(underTest.findPaths()).containsExactlyInAnyOrder(
        "start,A,b,A,c,A,end",
        "start,A,b,A,end",
        "start,A,b,end",
        "start,A,c,A,b,A,end",
        "start,A,c,A,b,end",
        "start,A,c,A,end",
        "start,A,end",
        "start,b,A,c,A,end",
        "start,b,A,end",
        "start,b,end"
    );
  }

  @Test
  public void shouldCountPaths() {
    final var underTest = new Pathfinder(INPUT);
    assertThat(underTest.countPaths()).isEqualTo(10);
  }

  @Test
  public void shouldBeAbleToVisitASmallCaveTwice() {
    final var underTest = new Pathfinder(INPUT, 2);
    assertThat(underTest.findPaths()).containsExactlyInAnyOrder(
        "start,A,b,A,b,A,c,A,end",
        "start,A,b,A,b,A,end",
        "start,A,b,A,b,end",
        "start,A,b,A,c,A,b,A,end",
        "start,A,b,A,c,A,b,end",
        "start,A,b,A,c,A,c,A,end",
        "start,A,b,A,c,A,end",
        "start,A,b,A,end",
        "start,A,b,d,b,A,c,A,end",
        "start,A,b,d,b,A,end",
        "start,A,b,d,b,end",
        "start,A,b,end",
        "start,A,c,A,b,A,b,A,end",
        "start,A,c,A,b,A,b,end",
        "start,A,c,A,b,A,c,A,end",
        "start,A,c,A,b,A,end",
        "start,A,c,A,b,d,b,A,end",
        "start,A,c,A,b,d,b,end",
        "start,A,c,A,b,end",
        "start,A,c,A,c,A,b,A,end",
        "start,A,c,A,c,A,b,end",
        "start,A,c,A,c,A,end",
        "start,A,c,A,end",
        "start,A,end",
        "start,b,A,b,A,c,A,end",
        "start,b,A,b,A,end",
        "start,b,A,b,end",
        "start,b,A,c,A,b,A,end",
        "start,b,A,c,A,b,end",
        "start,b,A,c,A,c,A,end",
        "start,b,A,c,A,end",
        "start,b,A,end",
        "start,b,d,b,A,c,A,end",
        "start,b,d,b,A,end",
        "start,b,d,b,end",
        "start,b,end"
    );
  }

  @Test
  public void shouldCountMultipleVisitPaths() {
    final var underTest = new Pathfinder(INPUT, 2);
    assertThat(underTest.countPaths()).isEqualTo(36);
  }

}
