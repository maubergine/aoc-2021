package com.mariusrubin.aoc.eighteen;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ASnailfishCalculator {


  @Test
  public void shouldNotReduceSimpleNumber() {
    assertThat(SnailfishCalculator.reduce(new SnailNumber("[1,2]"))).isEqualTo(new SnailNumber(
        "[1,2]"));
  }

  @Test
  public void shouldExplodeNestedNumbers() {
    final var results = Stream.of("[[[[[9,8],1],2],3],4]",
                                  "[7,[6,[5,[4,[3,2]]]]]",
                                  "[[6,[5,[4,[3,2]]]],1]",
                                  "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]",
                                  "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
                              .map(SnailNumber::new)
                              .map(SnailfishCalculator::reduce)
                              .map(SnailNumber::toString)
                              .toList();

    assertThat(results).containsExactly("[[[[0,9],2],3],4]",
                                        "[7,[6,[5,[7,0]]]]",
                                        "[[6,[5,[7,0]]],3]",
                                        "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
                                        "[[3,[2,[8,0]]],[9,[5,[7,0]]]]");
  }

  @Test
  public void shouldAddSimpleNumbers() {
    assertThat(SnailfishCalculator.add(new SnailNumber("[1,2]"),
                                       new SnailNumber("[[3,4],5]"))
                                  .toString()).isEqualTo("[[1,2],[[3,4],5]]");
  }

  @Test
  public void shouldAddAndReduceComplexNumbers() {
    assertThat(SnailfishCalculator.add(new SnailNumber("[[[[4,3],4],4],[7,[[8,4],9]]]"),
                                       new SnailNumber("[1,1]"))
                                  .toString()).isEqualTo("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]");
  }

  @Test
  public void shouldAddListsOfNumbers() {

    assertThat(SnailfishCalculator.addList("[1,1]", "[2,2]", "[3,3]", "[4,4]").toString())
        .isEqualTo("[[[[1,1],[2,2]],[3,3]],[4,4]]");

    assertThat(SnailfishCalculator.addList("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]").toString())
        .isEqualTo("[[[[3,0],[5,3]],[4,4]],[5,5]]");

    assertThat(SnailfishCalculator.addList("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]")
                                  .toString())
        .isEqualTo("[[[[5,0],[7,4]],[5,5]],[6,6]]");
  }

  @Test
  public void shouldDoAComplexAddition() {

    final var numbers = List.of(
        "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
        "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
        "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
        "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
        "[7,[5,[[3,8],[1,4]]]]",
        "[[2,[2,2]],[8,[8,1]]]",
        "[2,9]",
        "[1,[[[9,3],9],[[9,0],[0,7]]]]",
        "[[[5,[7,4]],7],1]",
        "[[[[4,2],2],6],[8,7]]");

    assertThat(SnailfishCalculator.addList(numbers).toString()).isEqualTo(
        "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]");


  }

  @Test
  public void shouldCalculateMagnitude() {

    final var numbers = List.of(
        "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
        "[[[5,[2,8]],4],[5,[[9,9],0]]]",
        "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
        "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
        "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
        "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
        "[[[[5,4],[7,7]],8],[[8,3],8]]",
        "[[9,3],[[9,9],[6,[4,9]]]]",
        "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
        "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]");

    assertThat(SnailfishCalculator.calculateMagnitude(numbers)).isEqualTo(4140);

  }

  @Test
  public void shouldCalculateLargestMagnitude() {

    final var numbers = List.of(
        "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
        "[[[5,[2,8]],4],[5,[[9,9],0]]]",
        "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
        "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
        "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
        "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
        "[[[[5,4],[7,7]],8],[[8,3],8]]",
        "[[9,3],[[9,9],[6,[4,9]]]]",
        "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
        "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]");

    assertThat(SnailfishCalculator.calculateLargestMagnitude(numbers)).isEqualTo(3993);

  }

}
