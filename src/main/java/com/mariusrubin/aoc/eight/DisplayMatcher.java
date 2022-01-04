package com.mariusrubin.aoc.eight;

import static com.mariusrubin.aoc.eight.Alphabetiser.alphabetise;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
final class DisplayMatcher {

  private static final Map<String, Display> DISPLAYS;

  private static final int LENGTH = 7;


  static {
    DISPLAYS = CombinationGenerator.allCombinations('a', 'g', LENGTH)
                                   .parallelStream()
                                   .map(Display::new)
                                   .collect(Collectors.toMap(Display::segmentPattern,
                                                             Function.identity()));

  }

  static Display findDisplayMatching(final String pattern) {
    return DISPLAYS.get(alphabetise(pattern));
  }

  private DisplayMatcher() {
  }

}
