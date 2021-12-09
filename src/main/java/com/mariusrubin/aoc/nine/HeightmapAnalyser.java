package com.mariusrubin.aoc.nine;

import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class HeightmapAnalyser {

  private static final int MAX_HEIGHT = 9;

  private final int[][] heightMap;
  private final int[][] visited;
  private final int     lastRow;
  private final int     lastColumn;

  public HeightmapAnalyser(final int[][] heightMap) {
    this.heightMap = heightMap.clone();
    visited = new int[heightMap.length][heightMap[0].length];
    lastRow = heightMap.length - 1;
    lastColumn = heightMap[0].length - 1;
  }

  public int[] findLowPoints() {
    return lowPoints().toArray();
  }

  public int calculateRiskLevel() {
    return lowPoints().map(i -> i + 1).sum();
  }

  private IntStream lowPoints() {
    return rows().flatMap(this::processRow);
  }

  public Set<Basin> findBasins() {
    return basins().collect(toSet());
  }

  public int calculateLargestBasin() {
    final var sizes = basins().mapToInt(Basin::getSize).sorted().toArray();
    return sizes[sizes.length - 3] * sizes[sizes.length - 2] * sizes[sizes.length - 1];
  }

  private Stream<Basin> basins() {
    return rows().mapToObj(this::findBasins)
                 .flatMap(Function.identity());
  }

  private Stream<Basin> findBasins(final int rowNum) {
    final var row = heightMap[rowNum];
    return IntStream.range(0, row.length)
                    .mapToObj(column -> basinOrEmpty(rowNum, column))
                    .flatMap(Function.identity());
  }

  private Stream<Basin> basinOrEmpty(final int rowNum, final int column) {

    final var here = toStep(rowNum, column);

    final var lowPointStream = lowPointOrEmpty(here);

    if (lowPointStream.findFirst().isEmpty()) {
      return Stream.empty();
    }

    return Stream.of(new Basin(crawl(here).distinct().mapToInt(Step::value).toArray()));

  }

  private Stream<Step> crawl(final Step from) {
    visit(from.row(), from.column());
    final var nextSteps = nextSteps(from);
    return Stream.concat(Stream.of(from), nextSteps.stream().flatMap(this::crawl));
  }

  private void visit(final int row, final int column) {
    visited[row][column] = 1;
  }

  private Step toStep(final int row, final int column) {
    return new Step(row, column, heightMap[row][column]);
  }

  private Set<Step> nextSteps(final Step from) {

    final var nextSteps = new HashSet<Step>();

    if (isNotFirstRow(from)) {
      if (shouldVisit(from.row() - 1, from.column())) {
        nextSteps.add(toStep(from.row() - 1, from.column()));
      }
    }
    if (isNotLastRow(from)) {
      if (shouldVisit(from.row() + 1, from.column())) {
        nextSteps.add(toStep(from.row() + 1, from.column()));
      }
    }
    if (isNotFirstColumn(from)) {
      if (shouldVisit(from.row(), from.column() - 1)) {
        nextSteps.add(toStep(from.row(), from.column() - 1));
      }
    }
    if (isNotLastColumn(from)) {
      if (shouldVisit(from.row(), from.column() + 1)) {
        nextSteps.add(toStep(from.row(), from.column() + 1));
      }
    }

    return nextSteps;

  }

  private IntStream rows() {
    return IntStream.range(0, heightMap.length);
  }

  private IntStream processRow(final int rowNum) {
    final var row = heightMap[rowNum];
    return IntStream.range(0, row.length)
                    .flatMap(column -> lowPointOrEmpty(new Step(rowNum, column, row[column])));
  }


  private IntStream lowPointOrEmpty(final Step step) {

    if (step.value() == MAX_HEIGHT) {
      return IntStream.empty();
    }

    final var adjacent = new HashSet<Integer>();

    if (isNotFirstRow(step)) {
      adjacent.add(heightMap[step.row() - 1][step.column()]);
    }
    if (isNotLastRow(step)) {
      adjacent.add(heightMap[step.row() + 1][step.column()]);
    }
    if (isNotFirstColumn(step)) {
      adjacent.add(heightMap[step.row()][step.column() - 1]);
    }
    if (isNotLastColumn(step)) {
      adjacent.add(heightMap[step.row()][step.column() + 1]);
    }

    if (adjacent.contains(step.value())) {
      return IntStream.empty();
    }

    return adjacent.stream()
                   .mapToInt(Integer::intValue)
                   .allMatch(i -> i > step.value())
           ? IntStream.of(step.value())
           : IntStream.empty();

  }

  private boolean shouldVisit(final int row, final int column) {
    return visited[row][column] < 1 && heightMap[row][column] < 9;
  }

  private boolean isNotFirstColumn(final Step step) {
    return step.column() > 0;
  }

  private boolean isNotFirstRow(final Step step) {
    return step.row() > 0;
  }

  private boolean isNotLastRow(final Step step) {
    return step.row() < lastRow;
  }

  private boolean isNotLastColumn(final Step step) {
    return step.column() < lastColumn;
  }

  private record Step(int row, int column, int value) {

  }
}
