package com.mariusrubin.aoc.eight;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ADisplayMatcher {

  private static final String INPUT_PATTERN = "acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab";

  @Test
  public void shouldFindADisplayMatchingPattern() {
    final var display = DisplayMatcher.findDisplayMatching(INPUT_PATTERN);

    assertThat(display).isNotNull();
    assertThat(display.convert("ab")).isEqualTo(1);
    assertThat(display.convert("dab")).isEqualTo(7);
    assertThat(display.convert("eafb")).isEqualTo(4);
    assertThat(display.convert("acedgfb")).isEqualTo(8);
  }

}
