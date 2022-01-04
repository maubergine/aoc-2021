package com.mariusrubin.aoc.one;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ASonarCounter {

  @Test
  public void shouldCountItemIncreases() {

    final List<Integer> items = Arrays.asList(1, 2, 1, 3, 5, 5, 6, 3, 4);

    assertThat(SonarCounter.countIncreases(items)).isEqualTo(5);

  }

  @Test
  public void shouldCountWindowedItemIncreases() {

    final List<Integer> items = Arrays.asList(199,
                                              200,
                                              208,
                                              210,
                                              200,
                                              207,
                                              240,
                                              269,
                                              260,
                                              263);

    assertThat(SonarCounter.countIncreases(items, 3)).isEqualTo(5);

  }

}
