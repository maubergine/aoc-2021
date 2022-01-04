package com.mariusrubin.aoc.twentythree;

import java.util.Collection;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
final class MoveCostCalculator {

  private MoveCostCalculator() {
  }

  static long calculateCost(final Collection<Move> moves) {
    return moves.stream()
                .mapToLong(MoveCostCalculator::calculateCost)
                .sum();
  }

  static long calculateCost(final Move move) {
    return calculateStepCount(move) * move.type().getEnergy();
  }

  static long calculateStepCount(final Move move) {
    return Math.abs(move.to().x() - move.from().x()) + move.from().y() + move.to().y();
  }

}
