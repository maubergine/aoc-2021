package com.mariusrubin.aoc.eight;

import static com.mariusrubin.aoc.eight.Alphabetiser.alphabetise;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class DisplayMatcher {

  private static final Map<String, Display> DISPLAYS;

  static {
    DISPLAYS = CombinationGenerator.allCombinations('a', 'g', 7)
                                   .parallelStream()
                                   .map(Display::new)
                                   .collect(Collectors.toMap(Display::segmentPattern,
                                                             Function.identity()));

  }

  public Display findDisplayMatching(final String pattern) {
    return DISPLAYS.get(alphabetise(pattern));
  }

}
