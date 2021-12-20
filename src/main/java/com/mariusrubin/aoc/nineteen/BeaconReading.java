package com.mariusrubin.aoc.nineteen;

import java.util.Objects;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class BeaconReading {

  private final int x;
  private final int y;
  private final int z;

  public BeaconReading(final BeaconReading beaconReading) {
    x = beaconReading.getX();
    y = beaconReading.getY();
    z = beaconReading.getZ();
  }

  public BeaconReading(final int x, final int y, final int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public int distanceTo(final BeaconReading other) {
    return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
  }

  public BeaconReading vectorise(final BeaconReading other) {
    return new BeaconReading(x - other.x, y - other.y, z - other.z);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final BeaconReading that = (BeaconReading) o;
    return x == that.x && y == that.y && z == that.z;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    return String.format("[%d,%d,%d]", x, y, z);
  }
}
