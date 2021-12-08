package com.mariusrubin.aoc.eight;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ACombinationGenerator {

  @Test
  public void shouldProduceCombinations() {

    assertThat(CombinationGenerator.allCombinations('a', 'g', 1))
        .containsExactly("a", "b", "c", "d", "e", "f", "g");

    assertThat(CombinationGenerator.allCombinations('a', 'c', 3))
        .containsExactlyInAnyOrder("abc", "acb", "bac", "bca", "cab", "cba");
    
  }

}
