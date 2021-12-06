package com.mariusrubin.aoc.six;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ALanternfishSchool {

  private static final int[] INITIAL_STATE = {3, 4, 3, 1, 2};

  private LanternfishSchool underTest;

  @BeforeEach
  public void init() {
    underTest = new LanternfishSchool(Arrays.stream(INITIAL_STATE).boxed().toList());
  }

  @Test
  public void shouldIncreaseWithTime() {
    underTest.passDays(80);
    assertThat(underTest.lanternFishCount()).isEqualTo(5934);
  }

  @Test
  public void shouldIncreaseOverALongerTime() {
    underTest.passDays(256);
    assertThat(underTest.lanternFishCount()).isEqualTo(26984457539L);
  }

}
