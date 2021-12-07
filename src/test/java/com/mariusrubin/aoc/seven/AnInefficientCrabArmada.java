package com.mariusrubin.aoc.seven;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AnInefficientCrabArmada {

  private static final int[] INITIAL_POSITIONS = {16, 1, 2, 0, 4, 2, 7, 1, 2, 14};

  private CrabArmada underTest;

  @BeforeEach
  public void init() {
    underTest = new InefficientCrabArmada(INITIAL_POSITIONS);
  }

  @Test
  public void shouldFindTheCheapestPosition() {
    assertThat(underTest.findCheapestPosition().position()).isEqualTo(5);
  }

  @Test
  public void shouldCalculateFuelCostOfCheapestPosition() {
    assertThat(underTest.findCheapestPosition().fuel()).isEqualTo(168);
  }


}
