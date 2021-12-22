package com.mariusrubin.aoc.twentytwo;

import static com.mariusrubin.aoc.twentytwo.ReactorCore.CubeState.ON;
import static java.lang.Integer.parseInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ReactorCore {

  private static final Pattern STEP =
      Pattern.compile(
          "(on|off) x=([\\-0-9]{1,10})\\.\\.([\\-0-9]{1,10}),y=([\\-0-9]{1,10})\\.\\.([\\-0-9]{1,10}),z=([\\-0-9]{1,10})\\.\\.([\\-0-9]{1,10})");

  private final List<CubeExtent> rebootSteps;

  public ReactorCore(final List<String> rebootSteps) {
    this(rebootSteps, Integer.MAX_VALUE);
  }

  public ReactorCore(final List<String> rebootSteps, final int boundarySize) {
    final var checkEdge = new Edge(boundarySize * -1, boundarySize);
    final var checkCube = new CubeExtent(checkEdge, checkEdge, checkEdge, ON);
    this.rebootSteps = rebootSteps.stream()
                                  .map(STEP::matcher)
                                  .filter(Matcher::find)
                                  .map(ReactorCore::toExtent)
                                  .filter(checkCube::contains)
                                  .toList();

  }

  public long countOnCubes() {
    return runSteps();
  }

  public long runSteps() {

    final var processed = new ArrayList<CubeExtent>();

    rebootSteps.forEach(cube -> {

      final var sects = processed.stream()
                                 .filter(cb -> cb.intersectsWith(cube))
                                 .map(cb -> cb.intersectionWith(cube))
                                 .toList();

      processed.addAll(sects);

      if (cube.getState() == ON) {
        processed.add(cube);
      }

    });

    return processed.stream()
                    .parallel()
                    .mapToLong(CubeExtent::countOn)
                    .sum();

  }

  private static CubeExtent toExtent(final Matcher match) {

    final var x = new Edge(parseInt(match.group(2)), parseInt(match.group(3)));
    final var y = new Edge(parseInt(match.group(4)), parseInt(match.group(5)));
    final var z = new Edge(parseInt(match.group(6)), parseInt(match.group(7)));

    return new CubeExtent(x, y, z, CubeState.fromString(match.group(1)));
  }

  private static final class CubeExtent {

    private final Edge x;
    private final Edge y;
    private final Edge z;

    private final CubeState state;

    private CubeExtent(final Edge x, final Edge y, final Edge z, final CubeState state) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.state = state;
    }

    private long countOn() {
      return x.getLength() * y.getLength() * z.getLength() * (isOn() ? 1 : -1);
    }

    private boolean isOn() {
      return getState() == ON;
    }

    private boolean contains(final CubeExtent other) {
      return getX().contains(other.getX())
             && getY().contains(other.getY())
             && getZ().contains(other.getZ());
    }

    private CubeExtent intersectionWith(final CubeExtent other) {
      return new CubeExtent(getX().intersectionWith(other.getX()),
                            getY().intersectionWith(other.getY()),
                            getZ().intersectionWith(other.getZ()),
                            getState().flip());
    }

    private boolean intersectsWith(final CubeExtent other) {
      return getX().intersectsWith(other.getX())
             && getY().intersectsWith(other.getY())
             && getZ().intersectsWith(other.getZ());
    }

    private Edge getX() {
      return x;
    }

    private Edge getY() {
      return y;
    }

    private Edge getZ() {
      return z;
    }

    public CubeState getState() {
      return state;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      final CubeExtent that = (CubeExtent) o;
      return x.equals(that.x) && y.equals(that.y) && z.equals(that.z) && state == that.state;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y, z, state);
    }

    @Override
    public String toString() {
      return new StringJoiner(",", state.name().toLowerCase() + " ", "")
          .add("x=" + x)
          .add("y=" + y)
          .add("z=" + z)
          .toString();
    }
  }

  private static final class Edge {

    private final int start;

    private final int end;


    private Edge(final int start, final int end) {
      this.start = start;
      this.end = end;
    }

    private boolean intersectsWith(final Edge other) {
      //Entirely contained by other
      if (other.getStart() <= getStart() && other.getEnd() >= getEnd()) {
        return true;
      }
      //Entirely contains other
      if (contains(other)) {
        return true;
      }
      //Other end overlaps start
      if (other.getEnd() >= getStart() && other.getEnd() <= getEnd()) {
        return true;
      }
      //Other start overlaps end
      return other.getStart() >= getStart() && other.getStart() <= getEnd();
    }

    private boolean contains(final Edge other) {
      return other.getStart() >= getStart() && other.getEnd() <= getEnd();
    }

    private int getStart() {
      return start;
    }

    private int getEnd() {
      return end;
    }

    private long getLength() {
      return end - start + 1;
    }

    private Edge intersectionWith(final Edge other) {
      return new Edge(Math.max(getStart(), other.getStart()), Math.min(getEnd(), other.getEnd()));
    }

    @Override
    public String toString() {
      return String.format("%d..%d", start, end);
    }
  }

  enum CubeState {

    ON,
    OFF;

    private static CubeState fromString(final String value) {
      return CubeState.valueOf(value.toUpperCase());
    }

    private CubeState flip() {
      return this == ON ? OFF : ON;
    }

  }

}

