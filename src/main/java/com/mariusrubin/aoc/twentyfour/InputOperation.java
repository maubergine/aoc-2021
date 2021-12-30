package com.mariusrubin.aoc.twentyfour;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class InputOperation implements ALUOperation {

  private static final Pattern VARIABLE = Pattern.compile(" ([a-z])$");

  private final String input;
  private       long   value;

  public InputOperation(final String input) {
    this.input = input;
  }

  @Override
  public void apply(final Map<Character, Long> variables) {
    final var match = VARIABLE.matcher(input);

    if (!match.find()) {
      throw new IllegalArgumentException("Could not find variable in " + input);
    }

    variables.put(match.group(1).charAt(0), value);

  }

  @Override
  public ALUOperation copy() {
    return new InputOperation(input);
  }

  public void setValue(final long value) {
    this.value = value;
  }
}
