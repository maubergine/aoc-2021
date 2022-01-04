package com.mariusrubin.aoc.nine;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class Basin {

  private final int[] members;

  Basin(final int[] members) {
    this.members = members.clone();
    Arrays.sort(this.members);
  }

  int getSize() {
    return members.length;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }

    return o instanceof Basin basin && Arrays.equals(members, basin.members);

  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(members);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Basin.class.getSimpleName() + "[", "]")
        .add("members=" + Arrays.toString(members))
        .toString();
  }
}
