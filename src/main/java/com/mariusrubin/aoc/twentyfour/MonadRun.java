package com.mariusrubin.aoc.twentyfour;

import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class MonadRun {

  private final RunType type;
  private final int     offset;
  private       int     inputMin;
  private       int     inputMax;

  MonadRun(final RunType type, final int offset) {
    this.type = type;
    this.offset = offset;
  }

  boolean isComplete() {
    return inputMin > 0 && inputMax > 0;
  }

  RunType getType() {
    return type;
  }

  int getOffset() {
    return offset;
  }

  int getInputMin() {
    return inputMin;
  }

  void setInputMin(final int inputMin) {
    this.inputMin = inputMin;
  }

  int getInputMax() {
    return inputMax;
  }

  void setInputMax(final int inputMax) {
    this.inputMax = inputMax;
  }

  enum RunType {
    PUSH,
    POP
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final MonadRun monadRun = (MonadRun) o;
    return offset == monadRun.offset
           && inputMin == monadRun.inputMin
           && inputMax == monadRun.inputMax
           && type == monadRun.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, offset, inputMin, inputMax);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", MonadRun.class.getSimpleName() + "[", "]")
        .add("type=" + type)
        .add("offset=" + offset)
        .add("inputMin=" + inputMin)
        .add("inputMax=" + inputMax)
        .toString();
  }
}
