package com.mariusrubin.aoc.twentyfour;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AnArithmeticLogicUnit {

  private ArithmeticLogicUnit underTest;

  @Test
  public void shouldNegate() {
    final var program = """
        inp x
        mul x -1
        add z x""";
    underTest = new ArithmeticLogicUnit(program.lines().toList());
    underTest.run(5L);
    assertThat(underTest.getZValue()).isEqualTo(-5L);

  }

  @Test
  public void shouldMultiplyAndCheckEquality() {
    final var program = """
        inp z
        inp x
        mul z 3
        eql z x""";
    underTest = new ArithmeticLogicUnit(program.lines().toList());
    underTest.run(5L, 15L);
    assertThat(underTest.getZValue()).isEqualTo(1L);

  }

  @Test
  public void shouldMultiplyDivideAndMod() {
    final var program = """
        inp w
        add z w
        mod z 2
        div w 2
        add y w
        mod y 2
        div w 2
        add x w
        mod x 2
        div w 2
        mod w 2""";
    underTest = new ArithmeticLogicUnit(program.lines().toList());
    underTest.run(9L);
    assertThat(underTest.getZValue()).isEqualTo(1L);

  }

}
