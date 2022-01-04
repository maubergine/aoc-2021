package com.mariusrubin.aoc.one;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

final class SonarCounter {

  private static final int DEFAULT_WINDOW      = 2;
  private static final int MINIMUM_WINDOW_SIZE = 1;

  private SonarCounter() {
  }

  static int countIncreases(final List<Integer> items, final int window) {

    if (window < MINIMUM_WINDOW_SIZE) {
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

    return window == MINIMUM_WINDOW_SIZE
           ? countIncreases(items)
           : countIncreases(sumWindow(items, window));

  }

  static int countIncreases(final List<Integer> items) {

    return window(items, DEFAULT_WINDOW).map(grouped -> grouped.get(1).compareTo(grouped.get(0)))
                                        .filter(i -> i > 0)
                                        .reduce(0, Integer::sum);

  }

  private static List<Integer> sumWindow(final List<Integer> items, final int window) {
    return window(items, window).map(grouped -> grouped.stream().reduce(0, Integer::sum)).toList();
  }

  private static Stream<List<Integer>> window(final List<Integer> items, final int window) {
    return IntStream.range(window - 1, items.size())
                    .mapToObj(i -> items.subList(i - window + 1, i + 1));
  }
}