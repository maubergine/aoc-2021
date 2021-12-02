package com.mariusrubin.aoc.one;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SonarCounter {

  private static List<Integer> sumWindow(final List<Integer> items, final int window) {
    return window(items, window).map(grouped -> grouped.stream().reduce(0, Integer::sum)).toList();
  }

  private static Stream<List<Integer>> window(final List<Integer> items, final int window) {
    return IntStream.range(window - 1, items.size())
                    .mapToObj(i -> items.subList(i - window + 1, i + 1));
  }

  public int countIncreases(final List<Integer> items, final int window) {

    if (window < 1) {
      throw new IllegalArgumentException(String.format("Window cannot be smaller than 1, but was %d",
                                                       window));
    }

    if (window > items.size()) {
      throw new IllegalArgumentException(String.format(
          "Window cannot be larger than the number of items (%d), but was %d",
          items.size(), window));
    }

    if (window >= items.size()) {
      return 0;
    }

    return window == 1
           ? countIncreases(items)
           : countIncreases(sumWindow(items, window));

  }

  public int countIncreases(final List<Integer> items) {

    return window(items, 2).map(grouped -> grouped.get(1).compareTo(grouped.get(0)))
                           .filter(i -> i > 0)
                           .reduce(0, Integer::sum);

  }
}