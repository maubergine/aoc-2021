package com.mariusrubin.aoc.two;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class ComplexSubmarine extends AbstractSubmarine {

  private int aim;

  @Override
  public void forward(final int value) {
    setHorizontalPosition(getHorizontalPosition() + value);
    setDepth(getDepth() + aim * value);
  }

  @Override
  public void up(final int value) {
    aim -= value;
  }

  @Override
  public void down(final int value) {
    aim += value;
  }
}
