package com.mariusrubin.aoc.five;

import static java.lang.Integer.parseInt;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class VentScanner {

  private static final Pattern DIRECTION_SPLIT  = Pattern.compile(" -> ");
  private static final Pattern COORDINATE_SPLIT = Pattern.compile(",");

  private final List<Segment> segments;

  public VentScanner(final List<String> inputs, final Direction... allowedDirections) {
    final Set<Direction> directionSet = EnumSet.copyOf(Set.of(allowedDirections));
    segments = inputs.stream()
                     .map(VentScanner::toSegment)
                     .filter(segment -> directionSet.contains(segment.getDirection()))
                     .toList();
  }

  public VentScanner(final List<String> inputs) {
    this(inputs, Direction.HORIZONTAL, Direction.VERTICAL, Direction.DIAGONAL);
  }

  public long countOverlaps() {
    final var xMax = segments.stream()
                             .flatMapToInt(seg -> IntStream.of(seg.getStart().x(),
                                                               seg.getFinish().x()))
                             .max()
                             .orElseThrow();

    final var yMax = segments.stream()
                             .flatMapToInt(seg -> IntStream.of(seg.getStart().y(),
                                                               seg.getFinish().y()))
                             .max()
                             .orElseThrow();

    final var segMap = new int[yMax + 1][xMax + 1];

    segments.forEach(seg -> populateMap(segMap, seg));

    return Arrays.stream(segMap)
                 .flatMapToInt(Arrays::stream)
                 .filter(i -> i > 1)
                 .count();
  }

  private static void populateMap(final int[][] segMap, final Segment segment) {
    switch (segment.getDirection()) {
      case HORIZONTAL -> populateHorizontal(segMap, segment);
      case VERTICAL -> populateVertical(segMap, segment);
      case DIAGONAL -> populateDiagonal(segMap, segment);
    }
  }

  private static void populateHorizontal(final int[][] segMap,
                                         final Segment segment) {

    final var rangeStart  = Math.min(segment.getStart().x(), segment.getFinish().x());
    final var rangeFinish = Math.max(segment.getStart().x(), segment.getFinish().x());

    IntStream.rangeClosed(rangeStart, rangeFinish)
             .forEach(i -> segMap[segment.getStart().y()][i]++);
  }

  private static void populateVertical(final int[][] segMap,
                                       final Segment segment) {

    final var rangeStart  = Math.min(segment.getStart().y(), segment.getFinish().y());
    final var rangeFinish = Math.max(segment.getStart().y(), segment.getFinish().y());

    IntStream.rangeClosed(rangeStart, rangeFinish)
             .forEach(i -> segMap[i][segment.getStart().x()]++);
  }

  private static void populateDiagonal(final int[][] segMap, final Segment segment) {

    //Setup so plotting always goes left-right
    final Coordinates plotStart;
    final Coordinates plotFinish;

    if (segment.getStart().x() < segment.getFinish().x()) {
      plotStart = segment.getStart();
      plotFinish = segment.getFinish();
    } else {
      plotStart = segment.getFinish();
      plotFinish = segment.getStart();
    }

    final var descending = plotStart.y() < plotFinish.y();

    if (descending) {
      IntStream.rangeClosed(plotStart.x(), plotFinish.x())
               .forEach(i -> segMap[i - plotStart.x() + plotStart.y()][i]++);
    } else {
      IntStream.rangeClosed(plotStart.x(), plotFinish.x())
               .forEach(i -> segMap[plotStart.y() - (i - plotStart.x())][i]++);
    }

  }

  private static Segment toSegment(final String line) {
    final var startFinish = DIRECTION_SPLIT.split(line);
    final var startCoords = COORDINATE_SPLIT.split(startFinish[0]);
    final var endCoords   = COORDINATE_SPLIT.split(startFinish[1]);
    return new Segment(new Coordinates(parseInt(startCoords[0]), parseInt(startCoords[1])),
                       new Coordinates(parseInt(endCoords[0]), parseInt(endCoords[1])));
  }


  private record Coordinates(int x, int y) {

  }

  private static final class Segment {

    private final Coordinates start;
    private final Coordinates finish;
    private final Direction   direction;

    private Segment(final Coordinates start, final Coordinates finish) {
      this.start = start;
      this.finish = finish;

      direction = this.start.x() == this.finish.x()
                  ? Direction.VERTICAL
                  : this.start.y() == this.finish.y()
                    ? Direction.HORIZONTAL
                    : Direction.DIAGONAL;


    }

    private Coordinates getStart() {
      return start;
    }

    private Coordinates getFinish() {
      return finish;
    }

    private Direction getDirection() {
      return direction;
    }

  }

  public enum Direction {
    HORIZONTAL,
    VERTICAL,
    DIAGONAL
  }
}
