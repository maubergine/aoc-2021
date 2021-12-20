package com.mariusrubin.aoc.eighteen;

import static java.lang.Integer.parseInt;

import java.util.Objects;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class SnailNumber {

  private static final int DEFAULT = -1;

  private int         value = DEFAULT;
  private SnailNumber left;
  private SnailNumber right;
  private SnailNumber parent;

  public SnailNumber(final int value, final SnailNumber parent) {
    this.value = value;
    this.parent = parent;
  }

  public SnailNumber(final String value) {
    this(value, null);
  }

  public SnailNumber(final String value, final SnailNumber parent) {
    final var builder = new StringBuilder(value);
    builder.deleteCharAt(0);
    builder.deleteCharAt(builder.length() - 1);

    if (Character.isDigit(builder.charAt(0))) {
      left = new SnailNumber(parseInt(builder.substring(0, 1)), this);
      builder.delete(0, 2);
    } else {
      final var leftStr = findCompleteValue(builder.toString());
      left = new SnailNumber(findCompleteValue(builder.toString()), this);
      //Delete the left component as well as following comma
      builder.delete(0, leftStr.length() + 1);
    }

    right = Character.isDigit(builder.charAt(builder.length() - 1))
            ? new SnailNumber(parseInt(builder.substring(builder.length() - 1)), this)
            : new SnailNumber(findCompleteValue(builder.toString()), this);

    this.parent = parent;

  }

  public SnailNumber(final SnailNumber left, final SnailNumber right) {
    this.left = left;
    this.right = right;
    left.setParent(this);
    right.setParent(this);
  }

  public int getMagnitude() {

    if (isValue()) {
      return value;
    }

    return 3 * getLeft().getMagnitude() + 2 * getRight().getMagnitude();

  }

  public int getValue() {
    return value;
  }

  public SnailNumber getLeft() {
    return left;
  }

  public SnailNumber getRight() {
    return right;
  }

  public boolean isValue() {
    return value > DEFAULT;
  }

  public void setParent(final SnailNumber parent) {
    this.parent = parent;
  }

  private String findCompleteValue(final String value) {

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

  public SnailNumber findFirstToSplit() {

    if (isValue()) {
      return value > 9 ? this : null;
    }

    final var leftSplit = left.findFirstToSplit();
    if (leftSplit != null) {
      return leftSplit;
    }

    final var rightSplit = right.findFirstToSplit();
    if (rightSplit != null) {
      return rightSplit;
    }

    return null;

  }

  public SnailNumber findFirstFourNested() {

    if (isValue()) {
      return null;
    }

    if (isFourNested()) {
      return this;
    }

    if (!left.isValue()) {
      final var leftNest = left.findFirstFourNested();
      if (leftNest != null) {
        return leftNest;
      }
    }

    if (!right.isValue()) {
      return right.findFirstFourNested();
    }

    return null;

  }

  public void doSplit() {

    if (!isValue()) {
      return;
    }

    if (value < 10) {
      return;
    }

    final var leftValue  = (value % 2 == 0 ? value : value - 1) / 2;
    final var rightValue = (value % 2 == 0 ? value : value + 1) / 2;

    value = -1;
    left = new SnailNumber(leftValue, this);
    right = new SnailNumber(rightValue, this);

  }

  public void doReduce() {

    if (isValue()) {
      return;
    }

    if (isFourNested()) {
      final var firstLeft = findNextLeft();
      if (firstLeft != null) {
        firstLeft.add(getLeft().getValue());
      }
      final var firstRight = findNextRight();
      if (firstRight != null) {
        firstRight.add(getRight().getValue());
      }
      left = null;
      right = null;
      value = 0;
    }

  }

  public void add(final int value) {
    if (!isValue()) {
      throw new IllegalArgumentException("Cannot add value to non-value number");
    }
    this.value += value;
  }

  private SnailNumber findNextLeft() {

    var child          = this;
    var relevantParent = getParent();
    while (relevantParent != null) {
      if (relevantParent.getLeft() == child) {
        child = relevantParent;
        relevantParent = child.getParent();
      } else {
        return relevantParent.getLeft().isValue()
               ? relevantParent.getLeft()
               : relevantParent.getLeft().findRightmostChild();
      }
    }

    return null;

  }

  private SnailNumber findNextRight() {

    var child          = this;
    var relevantParent = getParent();
    while (relevantParent != null) {
      if (relevantParent.getRight() == child) {
        child = relevantParent;
        relevantParent = child.getParent();
      } else {
        return relevantParent.getRight().isValue()
               ? relevantParent.getRight()
               : relevantParent.getRight().findLeftmostChild();
      }
    }

    return null;

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

    for (int i = 0; i < 4; i++) {
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
