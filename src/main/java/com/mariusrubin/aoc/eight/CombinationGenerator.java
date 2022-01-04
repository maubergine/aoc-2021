package com.mariusrubin.aoc.eight;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
final class CombinationGenerator {

  private CombinationGenerator() {
  }

  static Set<String> allCombinations(final char start, final char end, final int length) {
    return generateCombinations(start, end, length)
        .filter(CombinationGenerator::stringDoesNotContainDupes)
        .collect(Collectors.toSet());
  }

  static Stream<String> generateCombinations(final char start,
                                             final char end,
                                             final int length) {

    if (length == 0) {
      return Stream.empty();
    }

    return IntStream.rangeClosed(start, end)
                    .mapToObj(letter -> (char) letter)
                    .map(String::valueOf)
                    .flatMap(s -> lengthBasedStream(start, end, s, length));

  }

  private static boolean stringDoesNotContainDupes(final String input) {
    return input.length() == input.chars().distinct().toArray().length;
  }

  private static Stream<String> lengthBasedStream(final char start,
                                                  final char end,
                                                  final String character,
                                                  final int length) {
    return length == 1
           ? Stream.of(character)
           : generateCombinations(start, end, length - 1).map(character::concat);
  }

}
