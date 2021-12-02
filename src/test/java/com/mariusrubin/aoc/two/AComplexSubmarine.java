package com.mariusrubin.aoc.two;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AComplexSubmarine {

  private Submarine underTest;

  @BeforeEach
  public void init() {
    underTest = new ComplexSubmarine();
  }

  @Test
  public void shouldStartAtZero() {
    assertThat(underTest.getDepth()).isZero();
    assertThat(underTest.getHorizontalPosition()).isZero();
  }

  @Test
  public void shouldGoUp() {
    //This should take the submarine down to 50
    underTest.down(10);
    underTest.forward(5);

    //This should take the submarine up to 25.
    underTest.up(15);
    underTest.forward(5);

    assertThat(underTest.getDepth()).isEqualTo(25);
  }

  @Test
  public void shouldGoDown() {
    underTest.down(5);
    underTest.forward(10);
    assertThat(underTest.getDepth()).isEqualTo(50);
  }

  @Test
  public void shouldNotFly() {
    underTest.up(10);
    underTest.forward(10);
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

    assertThat(underTest.getFinalPosition()).isEqualTo(900);
  }

}