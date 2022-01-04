package com.mariusrubin.aoc.twentythree;

import java.util.Objects;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class Amphipod {

  private final AmphipodType type;
  private       Coordinate   position;
  private final int          roomBottom;

  Amphipod(final Amphipod source) {
    this(source.getType(), source.getPosition(), source.getRoomBottom());
  }

  Amphipod(final AmphipodType type, final int x, final int y, final int roomBottom) {
    this(type, new Coordinate(x, y), roomBottom);
  }

  Amphipod(final AmphipodType type, final Coordinate position, final int roomBottom) {
    this.type = type;
    this.position = position;
    this.roomBottom = roomBottom;
  }

  AmphipodType getType() {
    return type;
  }

  Coordinate getPosition() {
    return position;
  }

  int getX() {
    return position.x();
  }

  int getY() {
    return position.y();
  }

  void setPosition(final Coordinate position) {
    this.position = position;
  }

  boolean inHallway() {
    return getY() == 0;
  }

  boolean inRoom() {
    return !inHallway();
  }

  boolean isTop() {
    return getY() == 1;
  }

  boolean isBottom() {
    return getY() == roomBottom;
  }

  int getRoomBottom() {
    return roomBottom;
  }

  int getTargetRoom() {
    return getType().getRoomPosition();
  }

  boolean inCorrectRoom() {
    return getX() == getTargetRoom();
  }

  boolean inFinalPosition() {
    return inCorrectRoom() && getY() == roomBottom;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Amphipod amphipod = (Amphipod) o;
    return type == amphipod.type && position.equals(amphipod.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, position);
  }

  @Override
  public String toString() {
    return type.name().substring(0, 1);
  }
}
