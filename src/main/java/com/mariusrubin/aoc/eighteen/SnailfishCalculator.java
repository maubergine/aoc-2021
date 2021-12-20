package com.mariusrubin.aoc.eighteen;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class SnailfishCalculator {

  public int calculateMagnitude(final List<String> inputs) {
    return addList(inputs).getMagnitude();
  }

  public int calculateLargestMagnitude(final List<String> inputs) {

    return IntStream.range(0, inputs.size())
                    .map(i -> {
                      final var outer = inputs.get(i);
                      return IntStream.range(0, inputs.size())
                                      .filter(j -> j != i)
                                      .mapToObj(inputs::get)
                                      .map(inner -> addList(outer, inner))
                                      .mapToInt(num -> num.getMagnitude())
                                      .max()
                                      .orElseThrow();
                    })
                    .max()
                    .orElseThrow();
  }

  public SnailNumber add(final SnailNumber left, final SnailNumber right) {
    final var resultant = new SnailNumber(left, right);
    return reduce(resultant);
  }

  public SnailNumber addList(final String... numbers) {
    return addList(Arrays.stream(numbers).toList());
  }

  public SnailNumber addList(final List<String> inputs) {

    if (inputs.isEmpty()) {
      return null;
    }

    if (inputs.size() == 1) {
      return new SnailNumber(inputs.get(0));
    }

    return inputs.stream()
                 .map(SnailNumber::new)
                 .reduce(this::add)
                 .orElseThrow();

  }

  public SnailNumber reduce(final SnailNumber number) {

    if (number.getLeft().isValue() && number.getRight().isValue()) {
      return number;
    }

    var firstToReduce = number.findFirstFourNested();
    var toSplit       = number.findFirstToSplit();

    while (firstToReduce != null || toSplit != null) {

      if (firstToReduce != null) {
        firstToReduce.doReduce();
      } else {
        toSplit.doSplit();
      }

      firstToReduce = number.findFirstFourNested();
      toSplit = number.findFirstToSplit();

    }

    return number;

  }

}
