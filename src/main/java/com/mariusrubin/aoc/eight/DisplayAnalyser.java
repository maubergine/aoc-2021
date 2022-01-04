package com.mariusrubin.aoc.eight;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
final class DisplayAnalyser {

  private static final Pattern IO_SPLIT   = Pattern.compile(" \\| ");
  private static final Pattern ITEM_SPLIT = Pattern.compile(" ");

  private DisplayAnalyser() {
  }

  static long countInstancesOf(final int[] lookFor, final List<String> inputs) {
    final var searchSet = Arrays.stream(lookFor).boxed().collect(Collectors.toSet());
    return inputs.stream()
                 .mapToLong(input -> countInstancesOf(searchSet, input))
                 .sum();
  }

  static long sumOutput(final List<String> inputs) {
    return inputs.stream()
                 .map(DisplayAnalyser::toOutputPair)
                 .map(DisplayAnalyser::toCompleteOutput)
                 .mapToInt(Integer::parseInt)
                 .sum();
  }

  private static PatternOutputPair toOutputPair(final String input) {
    final var splits  = IO_SPLIT.split(input);
    final var pattern = splits[0];
    final var output  = splits[1];

    return new PatternOutputPair(pattern, output);
  }

  private static String toCompleteOutput(final PatternOutputPair patternOutputPair) {
    final var display = DisplayMatcher.findDisplayMatching(patternOutputPair.pattern());
    return ITEM_SPLIT.splitAsStream(patternOutputPair.output())
                     .map(display::convert)
                     .map(String::valueOf)
                     .collect(Collectors.joining());
  }

  private static long countInstancesOf(final Set<Integer> searchSet, final String input) {

    final PatternOutputPair pop = toOutputPair(input);

    final var display = DisplayMatcher.findDisplayMatching(pop.pattern());

    return ITEM_SPLIT.splitAsStream(pop.output())
                     .mapToInt(display::convert)
                     .filter(searchSet::contains)
                     .count();

  }

  private record PatternOutputPair(String pattern, String output) {

  }


}
