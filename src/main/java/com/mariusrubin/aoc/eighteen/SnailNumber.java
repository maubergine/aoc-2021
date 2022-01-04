package com.mariusrubin.aoc.eighteen;

import static java.lang.Integer.parseInt;

import java.util.Objects;
import java.util.Optional;
import java.util.function.ToIntFunction;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class SnailNumber {

  private static final int DEFAULT         = -1;
  private static final int POST_SPLIT      = 0;
  private static final int SPLIT_THRESHOLD = 10;

  private int         value = DEFAULT;
  private SnailNumber left;
  private SnailNumber right;
  private SnailNumber parent;

  SnailNumber(final int value, final SnailNumber parent) {
    this.value = value;
    this.parent = parent;
  }

  SnailNumber(final String value) {
    this(value, null);
  }

  SnailNumber(final String value, final SnailNumber parent) {
    final var builder = new StringBuilder(value);
    builder.deleteCharAt(POST_SPLIT);
    builder.deleteCharAt(builder.length() - 1);

    if (Character.isDigit(builder.charAt(POST_SPLIT))) {
      left = new SnailNumber(parseInt(builder.substring(POST_SPLIT, 1)), this);
      builder.delete(POST_SPLIT, 2);
    } else {
      final var leftStr = findCompleteValue(builder.toString());
      left = new SnailNumber(findCompleteValue(builder.toString()), this);
      //Delete the left component as well as following comma
      builder.delete(POST_SPLIT, leftStr.length() + 1);
    }

    right = Character.isDigit(builder.charAt(builder.length() - 1))
            ? new SnailNumber(parseInt(builder.substring(builder.length() - 1)), this)
            : new SnailNumber(findCompleteValue(builder.toString()), this);

    this.parent = parent;

  }

  SnailNumber(final SnailNumber left, final SnailNumber right) {
    this.left = left;
    this.right = right;
    left.setParent(this);
    right.setParent(this);
  }

  int getMagnitude() {

    if (isValue()) {
      return value;
    }

    return 3 * getLeft().getMagnitude() + 2 * getRight().getMagnitude();

  }

  int getValue() {
    return value;
  }

  SnailNumber getLeft() {
    return left;
  }

  SnailNumber getRight() {
    return right;
  }

  boolean isValue() {
    return value > DEFAULT;
  }

  void setParent(final SnailNumber parent) {
    this.parent = parent;
  }

  Optional<SnailNumber> findFirstToSplit() {

    if (isValue()) {
      return value > 9 ? Optional.of(this) : Optional.empty();
    }

    final var leftSplit = left.findFirstToSplit();
    if (leftSplit.isPresent()) {
      return leftSplit;
    }

    return right.findFirstToSplit();

  }

  Optional<SnailNumber> findFirstFourNested() {

    if (isValue()) {
      return Optional.empty();
    }

    if (isFourNested()) {
      return Optional.of(this);
    }

    if (!left.isValue()) {
      final var leftNest = left.findFirstFourNested();
      if (leftNest.isPresent()) {
        return leftNest;
      }
    }

    if (!right.isValue()) {
      return right.findFirstFourNested();
    }

    return Optional.empty();

  }

  void doSplit() {

    if (!isValue()) {
      return;
    }

    if (value < SPLIT_THRESHOLD) {
      return;
    }

    final var leftValue  = (value % 2 == POST_SPLIT ? value : value - 1) / 2;
    final var rightValue = (value % 2 == POST_SPLIT ? value : value + 1) / 2;

    value = DEFAULT;
    left = new SnailNumber(leftValue, this);
    right = new SnailNumber(rightValue, this);

  }

  void doReduce() {

    if (isValue()) {
      return;
    }

    if (isFourNested()) {
      findNextLeft().ifPresent(number -> number.add(getLeft().getValue()));
      findNextRight().ifPresent(number -> number.add(getRight().getValue()));

      left = null;
      right = null;

      value = POST_SPLIT;

    }

  }

  void add(final int value) {
    if (!isValue()) {
      throw new IllegalArgumentException("Cannot add value to non-value number");
    }
    this.value += value;
  }

  static ToIntFunction<SnailNumber> magnitude() {
    return SnailNumber::getMagnitude;
  }

  private static String findCompleteValue(final String value) {

    final var builder = new StringBuilder();

    var leftBraces  = 0;
    var rightBraces = 0;
    var count       = 0;

    do {
      final var c = value.charAt(count);
      switch (c) {
        case '[' -> leftBraces++;
        case ']' -> rightBraces++;
      }
      builder.append(c);
      count++;
    } while (rightBraces < leftBraces);

    return builder.toString();

  }

  private Optional<SnailNumber> findNextLeft() {

    var child          = this;
    var relevantParent = getParent();
    while (relevantParent != null) {
      if (relevantParent.getLeft() == child) {
        child = relevantParent;
        relevantParent = child.getParent();
      } else {
        return Optional.of(relevantParent.getLeft().isValue()
                           ? relevantParent.getLeft()
                           : relevantParent.getLeft().findRightmostChild());
      }
    }

    return Optional.empty();

  }

  private Optional<SnailNumber> findNextRight() {

    var child          = this;
    var relevantParent = getParent();
    while (relevantParent != null) {
      if (relevantParent.getRight() == child) {
        child = relevantParent;
        relevantParent = child.getParent();
      } else {
        return Optional.of(relevantParent.getRight().isValue()
                           ? relevantParent.getRight()
                           : relevantParent.getRight().findLeftmostChild());
      }
    }

    return Optional.empty();

  }

  private SnailNumber findLeftmostChild() {
    return left.isValue() ? left : left.findLeftmostChild();
  }

  private SnailNumber findRightmostChild() {
    return right.isValue() ? right : right.findRightmostChild();
  }

  public SnailNumber getParent() {
    return parent;
  }

  private boolean isFourNested() {
    if (parent == null) {
      return false;
    }

    var relevantParent = this;

    for (int i = POST_SPLIT; i < 4; i++) {
      if (relevantParent != null) {
        relevantParent = relevantParent.parent;
      }
    }

    return relevantParent != null;

  }

  @Override
  public String toString() {

    if (isValue()) {
      return String.valueOf(value);
    }
    return '[' + left.toString() + ',' + right.toString() + ']';
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final SnailNumber that = (SnailNumber) o;
    return value == that.value
           && Objects.equals(left, that.left)
           && Objects.equals(right, that.right);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, left, right);
  }
}
