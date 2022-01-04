package com.mariusrubin.aoc.two;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
abstract class AbstractSubmarine implements Submarine {

  private static final Pattern SPLIT_PATTERN = Pattern.compile(" ");

  private int depth;
  private int horizontalPosition;

  protected static void preventNegative(final int value) {
    if (value < 0) {
      throw new IllegalArgumentException(String.format("Value cannot be negative, was %d",
                                                       value));
    }
  }

  @Override
  public void followInstruction(final String instruction) {

    final var split = SPLIT_PATTERN.split(instruction);

    final var distance = Integer.parseInt(split[1]);

    switch (Direction.fromText(split[0])) {
      case UP -> up(distance);
      case DOWN -> down(distance);
      case FORWARD -> forward(distance);
    }

  }

  @Override
  public abstract void forward(int value);

  @Override
  public abstract void up(int value);

  @Override
  public abstract void down(int value);

  @Override
  public final int getDepth() {
    return depth;
  }

  protected void setDepth(final int depth) {
    //Prevent ascending to negative depth (i.e. flying!)
    this.depth = Math.max(0, depth);
  }

  @Override
  public final int getHorizontalPosition() {
    return horizontalPosition;
  }

  protected void setHorizontalPosition(final int horizontalPosition) {
    this.horizontalPosition = horizontalPosition;
  }

  @Override
  public final int getFinalPosition() {
    return depth * horizontalPosition;
  }

  private enum Direction {
    UP("up"),
    DOWN("down"),
    FORWARD("forward");

    private final String text;

    Direction(final String text) {
      this.text = text;
    }

    private static Direction fromText(final String text) {
      return Arrays.stream(Direction.values())
                   .filter(inst -> text.equals(inst.text))
                   .findFirst()
                   .orElseThrow(() -> new IllegalArgumentException(
                       "Could not find Direction matching " + text));
    }

  }
}