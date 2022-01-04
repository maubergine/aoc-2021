package com.mariusrubin.aoc.three;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class APowerMonitor {

  private static final List<Integer> INPUT = List.of(
      0b00100,
      0b11110,
      0b10110,
      0b10111,
      0b10101,
      0b01111,
      0b00111,
      0b11100,
      0b10000,
      0b11001,
      0b00010,
      0b01010
  );

  private PowerMonitor underTest;

  @BeforeEach
  public void init() {
    underTest = new PowerMonitor(INPUT);
  }

  @Test
  public void shouldCalculateGammaRate() {
    assertThat(underTest.getGammaRate()).isEqualTo(22);
  }

  @Test
  public void shouldCalculateEpsilonRate() {
    assertThat(underTest.getEpsilonRate()).isEqualTo(9);
  }

  @Test
  public void shouldCalculatePowerConsumption() {
    assertThat(underTest.getPowerConsumption()).isEqualTo(198);
  }

  @Test
  public void shouldCalculateOxygenRating() {
    assertThat(underTest.getOxygenRating()).isEqualTo(23);
  }

  @Test
  public void shouldCalculateScrubberRating() {
    assertThat(underTest.getScrubberRating()).isEqualTo(10);
  }

  @Test
  public void shouldCalculateLifeSupportRating() {
    assertThat(underTest.getLifeSupportRating()).isEqualTo(230);
  }

}