package com.mariusrubin.aoc.eight;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public final class Alphabetiser {

  private static final Pattern SPLIT = Pattern.compile(" ");

  private Alphabetiser() {
  }

  public static String alphabetise(final String input) {

    if (input.contains(" ")) {
      return SPLIT.splitAsStream(input)
                  .map(Alphabetiser::alphabetise)
                  .sorted()
                  .collect(Collectors.joining(" "));
    }

    return input.chars()
                .mapToObj(i -> (char) i)
                .map(String::valueOf)
                .sorted()
                .collect(Collectors.joining());

  }


}
