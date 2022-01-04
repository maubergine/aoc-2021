package com.mariusrubin.aoc.twentyfour;

import static java.lang.Long.parseLong;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class DefaultALUOperation implements ALUOperation {

  private static final Pattern PATTERN = Pattern.compile(" ([a-z]|[-0-9]+) ([a-z]|[-0-9]+)");

  private final BiFunction<Long, Long, Long>           calculation;
  private final Function<Map<Character, Long>, Long>   getLeft;
  private final Function<Map<Character, Long>, Long>   getRight;
  private final BiConsumer<Map<Character, Long>, Long> store;


  private DefaultALUOperation(final BiFunction<Long, Long, Long> calculation,
                              final Function<Map<Character, Long>, Long> getLeft,
                              final Function<Map<Character, Long>, Long> getRight,
                              final BiConsumer<Map<Character, Long>, Long> store
  ) {
    this.calculation = calculation;
    this.getLeft = getLeft;
    this.getRight = getRight;
    this.store = store;
  }

  DefaultALUOperation(final BiFunction<Long, Long, Long> calculation,
                      final String operation) {
    this.calculation = calculation;

    final var match = PATTERN.matcher(operation);

    if (!match.find()) {
      throw new IllegalArgumentException("Unable to match against operation " + operation);
    }

    final var leftChar = match.group(1).charAt(0);
    getLeft = variables -> variables.getOrDefault(leftChar, 0L);
    store = (map, value) -> map.put(leftChar, value);

    if (isLong(match.group(2))) {
      final var right = parseLong(match.group(2));
      getRight = variables -> right;
    } else {
      final var rightChar = match.group(2).charAt(0);
      getRight = variables -> variables.getOrDefault(rightChar, 0L);
    }

  }

  @Override
  public void apply(final Map<Character, Long> variables) {
    store.accept(variables, calculation.apply(getLeft.apply(variables), getRight.apply(variables)));
  }

  private static boolean isLong(final String value) {

    if (value == null) {
      return false;
    }

    try {
      final long ignored = Long.parseLong(value);
      return true;
    } catch (final NumberFormatException ignored) {
      return false;
    }

  }

}
