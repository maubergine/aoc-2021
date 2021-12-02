package com.mariusrubin.aoc.two;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ASimpleSubmarine {

  private Submarine underTest;

  @BeforeEach
  public void init() {
    underTest = new SimpleSubmarine();
  }

  @Test
  public void shouldStartAtZero() {
    assertThat(underTest.getDepth()).isZero();
    assertThat(underTest.getHorizontalPosition()).isZero();
  }

  @Test
  public void shouldGoUp() {
    underTest.down(10);
    underTest.up(6);
    assertThat(underTest.getDepth()).isEqualTo(4);
  }

  @Test
  public void shouldGoDown() {
    underTest.down(5);
    assertThat(underTest.getDepth()).isEqualTo(5);
  }

  @Test
  public void shouldNotFly() {
    underTest.down(5);
    underTest.up(10);
    assertThat(underTest.getDepth()).isZero();
  }

  @Test
  public void shouldGoForward() {
    underTest.forward(5);
    underTest.forward(6);
    assertThat(underTest.getHorizontalPosition()).isEqualTo(11);
  }

  @Test
  public void shouldFollowTextInstruction() {
    Stream.of("forward 5",
              "down 5",
              "forward 8",
              "up 3",
              "down 8",
              "forward 2")
          .forEach(underTest::followInstruction);

    assertThat(underTest.getFinalPosition()).isEqualTo(150);
  }

}