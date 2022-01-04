package com.mariusrubin.aoc.two;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
interface Submarine {

  void followInstruction(final String instruction);

  void forward(int value);

  void up(int value);

  void down(int value);

  int getDepth();

  int getHorizontalPosition();

  int getFinalPosition();
}
