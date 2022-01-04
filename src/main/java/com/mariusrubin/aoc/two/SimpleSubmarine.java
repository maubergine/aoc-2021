package com.mariusrubin.aoc.two;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class SimpleSubmarine extends AbstractSubmarine {

  @Override
  public void forward(final int value) {
    setHorizontalPosition(getHorizontalPosition() + value);
  }

  @Override
  public void up(final int value) {
    preventNegative(value);
    setDepth(getDepth() - value);
  }

  @Override
  public void down(final int value) {
    preventNegative(value);
    setDepth(getDepth() + value);
  }
}
