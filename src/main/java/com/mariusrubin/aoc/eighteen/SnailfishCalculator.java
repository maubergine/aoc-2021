package com.mariusrubin.aoc.eighteen;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
final class SnailfishCalculator {

  private SnailfishCalculator() {
  }

  static int calculateMagnitude(final List<String> inputs) {
    return addList(inputs).getMagnitude();
  }

  static int calculateLargestMagnitude(final List<String> inputs) {

    return IntStream.range(0, inputs.size())
                    .map(i -> {
                      final var outer = inputs.get(i);
                      return IntStream.range(0, inputs.size())
                                      .filter(j -> j != i)
                                      .mapToObj(inputs::get)
                                      .map(inner -> addList(outer, inner))
                                      .mapToInt(SnailNumber.magnitude())
                                      .max()
                                      .orElseThrow();
                    })
                    .max()
                    .orElseThrow();
  }

  static SnailNumber add(final SnailNumber left, final SnailNumber right) {
    final var resultant = new SnailNumber(left, right);
    return reduce(resultant);
  }

  static SnailNumber addList(final String... numbers) {
    return addList(Arrays.stream(numbers).toList());
  }

  static SnailNumber addList(final List<String> inputs) {

    if (inputs.isEmpty()) {
      throw new IllegalArgumentException("Cannot add empty list");
    }

    if (inputs.size() == 1) {
      return new SnailNumber(inputs.get(0));
    }

    return inputs.stream()
                 .map(SnailNumber::new)
                 .reduce(SnailfishCalculator::add)
                 .orElseThrow();

  }

  static SnailNumber reduce(final SnailNumber number) {

    if (number.getLeft().isValue() && number.getRight().isValue()) {
      return number;
    }

    var firstToReduce = number.findFirstFourNested();
    var toSplit       = number.findFirstToSplit();

    while (firstToReduce.isPresent() || toSplit.isPresent()) {

      if (firstToReduce.isPresent()) {
        firstToReduce.get().doReduce();
      } else {
        toSplit.get().doSplit();
      }

      firstToReduce = number.findFirstFourNested();
      toSplit = number.findFirstToSplit();

    }

    return number;

  }

}
