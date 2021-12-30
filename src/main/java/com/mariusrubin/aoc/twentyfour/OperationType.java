package com.mariusrubin.aoc.twentyfour;

import java.util.Arrays;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
enum OperationType {
  INP,
  ADD,
  MUL,
  DIV,
  MOD,
  EQL;


  static OperationType fromString(final String operation) {
    return Arrays.stream(OperationType.values())
                 .filter(type -> type.name().toLowerCase().equals(operation.substring(0, 3)))
                 .findAny()
                 .orElseThrow();
  }

}
