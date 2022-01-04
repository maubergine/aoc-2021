package com.mariusrubin.aoc.twentythree;

import java.util.Arrays;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
enum AmphipodType {
  AMBER(1, 2),
  BRONZE(10, 4),
  COPPER(100, 6),
  DESERT(1000, 8);

  private final int energy;
  private final int roomPosition;

  AmphipodType(final int energy, final int roomPosition) {
    this.energy = energy;
    this.roomPosition = roomPosition;
  }

  static AmphipodType fromLetter(final String letter) {
    return Arrays.stream(AmphipodType.values())
                 .filter(type -> type.name().startsWith(letter.substring(0, 1)))
                 .findAny()
                 .orElseThrow();
  }

  int getRoomPosition() {
    return roomPosition;
  }

  int getEnergy() {
    return energy;
  }

  static boolean isRoomPosition(final int position) {
    return Arrays.stream(AmphipodType.values())
                 .mapToInt(AmphipodType::getRoomPosition)
                 .anyMatch(i -> i == position);
  }
}
