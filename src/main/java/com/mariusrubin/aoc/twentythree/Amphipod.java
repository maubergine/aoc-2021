package com.mariusrubin.aoc.twentythree;

import java.util.Objects;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class Amphipod {

  private final AmphipodType type;
  private       Coordinate   position;
  private final int          roomBottom;

  public Amphipod(final Amphipod source) {
    this(source.getType(), source.getPosition(), source.getRoomBottom());
  }

  public Amphipod(final AmphipodType type, final int x, final int y, final int roomBottom) {
    this(type, new Coordinate(x, y), roomBottom);
  }

  public Amphipod(final AmphipodType type, final Coordinate position, final int roomBottom) {
    this.type = type;
    this.position = position;
    this.roomBottom = roomBottom;
  }

  public AmphipodType getType() {
    return type;
  }

  public Coordinate getPosition() {
    return position;
  }

  public int getX() {
    return position.x();
  }

  public int getY() {
    return position.y();
  }

  public void setPosition(final Coordinate position) {
    this.position = position;
  }

  public boolean inHallway() {
    return getY() == 0;
  }

  public boolean inRoom() {
    return !inHallway();
  }

  public boolean isTop() {
    return getY() == 1;
  }

  public boolean isBottom() {
    return getY() == roomBottom;
  }

  public int getRoomBottom() {
    return roomBottom;
  }

  public int getTargetRoom() {
    return getType().getRoomPosition();
  }

  public boolean inCorrectRoom() {
    return getX() == getTargetRoom();
  }

  public boolean inFinalPosition() {
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
