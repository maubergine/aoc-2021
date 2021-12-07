package com.mariusrubin.aoc.seven;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public abstract class AbstractCrabArmada implements CrabArmada {

  private final int[] initialPositions;

  protected AbstractCrabArmada(final int[] initialPositions) {
    this.initialPositions = Arrays.stream(initialPositions)
                                  .sorted()
                                  .toArray();
  }

  protected AbstractCrabArmada(final List<Integer> initialPositions) {
    this.initialPositions = initialPositions.stream()
                                            .mapToInt(Integer::intValue)
                                            .sorted()
                                            .toArray();
  }

  @Override
  public FuelPositionPair findCheapestPosition() {

    final var minPosition = initialPositions[0];
    final var maxPosition = initialPositions[initialPositions.length - 1];

    //For each position calculate the difference between that and the other positions
    //Then determine the position with the lowest fuel
    return IntStream.rangeClosed(minPosition, maxPosition)
                    .mapToObj(this::calculateFuelCosts)
                    .flatMap(Function.identity())
                    .collect(Collectors.groupingBy(FuelPositionPair::position))
                    .entrySet()
                    .stream()
                    .map(e -> new FuelPositionPair(e.getKey(), totalFuelCost(e.getValue())))
                    .min(Comparator.comparing(FuelPositionPair::fuel))
                    .orElseThrow();
  }

  protected Stream<FuelPositionPair> calculateFuelCosts(final int position) {
    return Arrays.stream(initialPositions)
                 .mapToObj(pos -> calculateFuelCost(position, pos));
  }

  protected abstract FuelPositionPair calculateFuelCost(final int initialPosition,
                                                        final int targetPosition);


  private static int totalFuelCost(final List<FuelPositionPair> distances) {
    return distances.stream()
                    .mapToInt(FuelPositionPair::fuel)
                    .sum();

  }

}
