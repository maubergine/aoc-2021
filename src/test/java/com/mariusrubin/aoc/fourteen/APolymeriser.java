package com.mariusrubin.aoc.fourteen;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class APolymeriser {

  private static final String INPUT = """
      NNCB
                    
      CH -> B
      HH -> N
      CB -> H
      NH -> C
      HB -> C
      HC -> B
      HN -> C
      NN -> C
      BH -> H
      NC -> B
      NB -> B
      BN -> B
      BB -> N
      BC -> B
      CC -> N
      CN -> C""";

  private Polymeriser underTest;

  @BeforeEach
  public void init() {
    underTest = new Polymeriser(INPUT.lines().toList());
  }

  @Test
  public void shouldCalculateOutput() {
    assertThat(underTest.calculateOutput(10)).isEqualTo(1588L);
  }

  @Test
  public void shouldCalculateLargeOutput() {
    assertThat(underTest.calculateOutput(40)).isEqualTo(2188189693529L);
  }

}