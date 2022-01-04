package com.mariusrubin.aoc.twentyfive;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
class SeaCucumberZone {

  private final SeaCucumber[][] cucumbers;

  private final int lengthMax;
  private final int rowMax;

  SeaCucumberZone(final List<String> inputs) {

    lengthMax = inputs.size();
    rowMax = inputs.get(0).length();

    cucumbers = new SeaCucumber[lengthMax][rowMax];

    IntStream.range(0, lengthMax)
             .sequential()
             .forEach(y -> {
               final var row = inputs.get(y);
               IntStream.range(0, rowMax)
                        .forEach(x -> cucumbers[y][x] = SeaCucumber.from(row.substring(x, x + 1)));
             });

  }

  int countMaxMoves() {

    final var previous = new AtomicLong(Arrays.deepHashCode(cucumbers));
    final var current  = new AtomicLong(previous.get() * -1);

    return IntStream.iterate(1, i -> current.get() != previous.get(), i -> i + 1)
                    .sequential()
                    .peek(i -> {
                      previous.set(Arrays.deepHashCode(cucumbers));
                      step(i);
                      current.set(Arrays.deepHashCode(cucumbers));
                    })
                    .max()
                    .orElseThrow();

  }

  Stream<String> states(final int howMany) {
    return IntStream.range(0, howMany)
                    .mapToObj(this::step)
                    .map(this::stringify);
  }

  private void reset() {
    Arrays.stream(cucumbers)
          .forEach(row -> IntStream.range(0, row.length)
                                   .filter(x -> row[x] != null)
                                   .forEach(x -> {
                                     if (row[x].isPhantom()) {
                                       row[x] = null;
                                     } else {
                                       row[x].reset();
                                     }
                                   }));
  }

  private SeaCucumber[][] step(final int which) {

    //Do horizontal
    IntStream.range(0, lengthMax)
             .sequential()
             .mapToObj(y -> cucumbers[y])
             .forEach(this::doHorizontal);

    reset();

    //Do vertical
    IntStream.range(0, lengthMax)
             .sequential()
             .forEach(this::doVertical);

    reset();

    return cucumbers;
  }

  private void doVertical(final int y) {
    final var row = cucumbers[y];
    IntStream.range(0, rowMax)
             .filter(x -> row[x] != null && !row[x].hasMoved() && row[x].isVertical())
             .forEach(x -> {
               final var next = y + 1 == lengthMax ? 0 : y + 1;
               if (cucumbers[next][x] == null) {
                 cucumbers[next][x] = row[x];
                 cucumbers[next][x].move();
                 row[x] = new SeaCucumber(SeaCucumberType.PHANTOM);
               }
             });
  }

  private void doHorizontal(final SeaCucumber[] row) {
    IntStream.range(0, rowMax)
             .filter(x -> row[x] != null && !row[x].hasMoved() && row[x].isHorizontal())
             .forEach(x -> {
               final var next = x + 1 == rowMax ? 0 : x + 1;
               if (row[next] == null) {
                 row[next] = row[x];
                 row[next].move();
                 row[x] = new SeaCucumber(SeaCucumberType.PHANTOM);
               }
             });
  }


  @Override
  public String toString() {

    return stringify(cucumbers);

  }

  private String stringify(final SeaCucumber[][] cucumberArray) {
    return Arrays.stream(cucumberArray)
                 .map(row -> IntStream.range(0, rowMax)
                                      .mapToObj(i -> row[i] == null || row[i].isPhantom()
                                                     ? "."
                                                     : row[i].toString())
                                      .collect(Collectors.joining()))
                 .collect(Collectors.joining(System.lineSeparator()));
  }

  private static final class SeaCucumber {

    private final SeaCucumberType type;
    private       boolean         moved;

    private SeaCucumber(final SeaCucumberType type) {
      this.type = type;
    }

    private static SeaCucumber from(final String value) {
      return SeaCucumberType.fromString(value).map(SeaCucumber::new).orElse(null);
    }

    private boolean isHorizontal() {
      return type.isHorizontal();
    }

    private boolean isVertical() {
      return type.isVertical();
    }

    private boolean isPhantom() {
      return type.isPhantom();
    }

    private boolean hasMoved() {
      return moved;
    }

    private void move() {
      moved = true;
    }

    private void reset() {
      moved = false;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      final SeaCucumber that = (SeaCucumber) o;
      return moved == that.moved && type == that.type;
    }

    @Override
    public int hashCode() {
      return Objects.hash(type, moved);
    }

    @Override
    public String toString() {
      return type.representation;
    }
  }

  private enum SeaCucumberType {
    HORIZONTAL(">"),
    VERTICAL("v"),
    PHANTOM("P");

    private final String representation;

    SeaCucumberType(final String representation) {
      this.representation = representation;
    }

    private boolean isHorizontal() {
      return this == HORIZONTAL;
    }

    private boolean isVertical() {
      return this == VERTICAL;
    }

    private boolean isPhantom() {
      return this == PHANTOM;
    }

    private static Optional<SeaCucumberType> fromString(final String value) {
      return Arrays.stream(SeaCucumberType.values())
                   .filter(t -> value.equals(t.representation))
                   .findFirst();
    }

    @Override
    public String toString() {
      return representation;
    }
  }
}
