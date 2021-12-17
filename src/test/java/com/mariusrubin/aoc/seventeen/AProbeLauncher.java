package com.mariusrubin.aoc.seventeen;

import static org.assertj.core.api.Assertions.assertThat;

import com.mariusrubin.aoc.seventeen.ProbeLauncher.InitialVelocity;
import com.mariusrubin.aoc.seventeen.ProbeLauncher.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AProbeLauncher {

  private static final String INPUT = "target area: x=20..30, y=-10..-5";

  private ProbeLauncher underTest;

  @BeforeEach
  public void init() {
    underTest = new ProbeLauncher(INPUT);
  }

  @Test
  public void shouldCalculateStepsBasedOnTrajectory() {

    final var result = underTest.calculateStepsForTrajectory(7, 2, 7);

    assertThat(result).containsExactly(
        new Step(7, 2),
        new Step(13, 3),
        new Step(18, 3),
        new Step(22, 2),
        new Step(25, 0),
        new Step(27, -3),
        new Step(28, -7)
    );

  }

  @Test
  public void shouldFindViableVelocities() {
    assertThat(underTest.findViableVelocities()).contains(
        new InitialVelocity(6, 9),
        new InitialVelocity(7, 2),
        new InitialVelocity(6, 3),
        new InitialVelocity(9, 0));
  }

  @Test
  public void shouldFindHighestViableVelocity() {
    assertThat(underTest.findHighestViableVelocity()).isEqualTo(new InitialVelocity(6, 9));
  }

  @Test
  public void shouldFindHighestYPosition() {
    assertThat(underTest.findHighestYPosition()).isEqualTo(45);
  }

  @Test
  public void shouldCountViableVelocities() {
    assertThat(underTest.countViableVelocities()).isEqualTo(112);
  }

}