package com.mariusrubin.aoc.eight;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AnAlphabetiser {

  @Test
  public void shouldAlphabetisePatterns() {
    assertThat(Alphabetiser.alphabetise("bacd gfb")).isEqualTo("abcd bfg");
  }

  @Test
  public void shouldAlphabetiseSingleStrings() {
    assertThat(Alphabetiser.alphabetise("fdge")).isEqualTo("defg");
  }

}
