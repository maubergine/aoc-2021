package com.mariusrubin.aoc.eleven;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class OctopusSwarm {

  private Octopus[][] swarm;

  public OctopusSwarm(final int[][] input) {
    swarm = IntStream.range(0, input.length)
                     .mapToObj(i -> toOctopi(input, i))
                     .toArray(Octopus[][]::new);
  }

  public Stream<int[][]> streamSteps(final int howMany) {
    return IntStream.range(0, howMany).mapToObj(i -> step());
  }

  public int countFlashes(final int steps) {
    return IntStream.range(0, steps)
                    .map(i -> doStep())
                    .sum();
  }

  public int findFirstFlash() {
    return IntStream.range(0, Integer.MAX_VALUE)
                    .sequential()
                    .flatMap(i -> {
                      final var flashes = doStep();
                      if (flashes == 100) {
                        return IntStream.of(i);
                      }
                      return IntStream.empty();
                    })
                    .map(i -> i + 1)
                    .findFirst()
                    .orElseThrow();
  }

  private int[][] step() {
    doStep();
    return Arrays.stream(swarm)
                 .map(row -> Arrays.stream(row).mapToInt(Octopus::getEnergyLevel).toArray())
                 .toArray(int[][]::new);
  }

  private void doReset() {
    streamOctopi().forEach(Octopus::reset);
  }

  private int doStep() {
    final var flashes = streamOctopi().mapToInt(this::flash).sum();
    doReset();
    return flashes;
  }

  private Stream<Octopus> streamOctopi() {
    return Arrays.stream(swarm).flatMap(Arrays::stream);
  }

  private int flash(final Octopus octopus) {

    if (octopus.hasFlashed()) {
      return 0;
    }

    final var counter = octopus.increment();

    if (counter == 0) {
      return 0;
    }

    return counter + getAdjacent(octopus).mapToInt(this::flash).sum();

  }

  private Stream<Octopus> getAdjacent(final Octopus start) {
    return generateCoordinates(start).map(coord -> swarm[coord.row()][coord.column()]);
  }

  private Stream<Coordinate> generateCoordinates(final Octopus start) {
    return Stream.of(new Coordinate(start.getRow(), start.getColumn() - 1),
                     new Coordinate(start.getRow(), start.getColumn() + 1),
                     new Coordinate(start.getRow() - 1, start.getColumn() - 1),
                     new Coordinate(start.getRow() - 1, start.getColumn()),
                     new Coordinate(start.getRow() - 1, start.getColumn() + 1),
                     new Coordinate(start.getRow() + 1, start.getColumn() - 1),
                     new Coordinate(start.getRow() + 1, start.getColumn()),
                     new Coordinate(start.getRow() + 1, start.getColumn() + 1))
                 .filter(OctopusSwarm::isAllowable);

  }

  private static boolean isAllowable(final Coordinate coordinate) {
    return coordinate.row() >= 0
           && coordinate.row() <= 9
           && coordinate.column() >= 0
           && coordinate.column() <= 9;
  }


  private Octopus[] toOctopi(final int[][] input, final int rowNum) {
    final var row = input[rowNum];
    return IntStream.range(0, row.length)
                    .mapToObj(j -> new Octopus(rowNum, j, input[rowNum][j]))
                    .toArray(Octopus[]::new);
  }

  private class Octopus {

    private final int row;
    private final int column;

    private int     energyLevel;
    private boolean hasFlashed;

    private Octopus(final int row, final int column, final int energyLevel) {
      this.row = row;
      this.column = column;
      this.energyLevel = energyLevel;
    }

    public int getRow() {
      return row;
    }

    public int getColumn() {
      return column;
    }

    public int getEnergyLevel() {
      return energyLevel;
    }

    private int increment() {
      if (energyLevel == 9) {
        hasFlashed = true;
        energyLevel = 0;
        return 1;
      }
      energyLevel++;
      return 0;
    }

    private boolean hasFlashed() {
      return hasFlashed;
    }

    private void reset() {
      hasFlashed = false;
    }

    @Override
    public String toString() {
      return String.valueOf(energyLevel);
    }
  }

  private record Coordinate(int row, int column) {

  }

}
