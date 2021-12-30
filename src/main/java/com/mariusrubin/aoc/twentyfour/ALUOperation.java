package com.mariusrubin.aoc.twentyfour;

import java.util.Map;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public interface ALUOperation {

  void apply(final Map<Character, Long> variables);

  ALUOperation copy();
}
