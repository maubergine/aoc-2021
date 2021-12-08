package com.mariusrubin.aoc.eight;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ADisplay {

  private Display undertest;

  @BeforeEach
  public void init() {
    undertest = new Display("bcdefga");
  }

  @Test
  public void shouldProduceWiredSegments() {
    final var segments = undertest.getWiredSegments();

    assertThat(segments.get("abcdfg").number()).isZero();
    assertThat(segments.get("dg").number()).isEqualTo(1);
    assertThat(segments.get("abdef").number()).isEqualTo(2);
    assertThat(segments.get("abdeg").number()).isEqualTo(3);
    assertThat(segments.get("cdeg").number()).isEqualTo(4);
    assertThat(segments.get("abceg").number()).isEqualTo(5);
    assertThat(segments.get("abcefg").number()).isEqualTo(6);
    assertThat(segments.get("bdg").number()).isEqualTo(7);
    assertThat(segments.get("abcdefg").number()).isEqualTo(8);
    assertThat(segments.get("abcdeg").number()).isEqualTo(9);

  }

  @Test
  public void shouldProduceASegmentPattern() {
    final var expected = "abcdefg abcdeg abcdfg abcefg abceg abdef abdeg bdg cdeg dg";

    assertThat(undertest.segmentPattern()).isEqualTo(expected);

  }

  @Test
  public void shouldConvertPatterns() {
    assertThat(undertest.convert("dg")).isEqualTo(1);
  }

}
