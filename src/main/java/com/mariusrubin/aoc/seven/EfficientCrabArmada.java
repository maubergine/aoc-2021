package com.mariusrubin.aoc.seven;

import java.util.List;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class EfficientCrabArmada extends AbstractCrabArmada {

  EfficientCrabArmada(final int[] initialPositions) {
    super(initialPositions);
  }

  EfficientCrabArmada(final List<Integer> initialPositions) {
    super(initialPositions);
  }

  @Override
  protected FuelPositionPair calculateFuelCost(final int initialPosition,
                                               final int targetPosition) {
    return new FuelPositionPair(initialPosition, Math.abs(targetPosition - initialPosition));
  }
}
