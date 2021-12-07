package com.mariusrubin.aoc.seven;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class InefficientCrabArmada extends AbstractCrabArmada {

  public InefficientCrabArmada(final int[] initialPositions) {
    super(initialPositions);
  }

  public InefficientCrabArmada(final List<Integer> initialPositions) {
    super(initialPositions);
  }

  @Override
  protected FuelPositionPair calculateFuelCost(final int initialPosition,
                                               final int targetPosition) {

    final var positionDelta = Math.abs(targetPosition - initialPosition);
    final var fuelCost      = IntStream.rangeClosed(1, positionDelta).sum();

    return new FuelPositionPair(initialPosition, fuelCost);
  }
}
