package com.mariusrubin.aoc.nineteen;

import static java.lang.Math.abs;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class BeaconScanner {

  private static final List<RotOp> ROTATIONS = Arrays.stream(RotOp.values()).toList();

  private final int           id;
  private       BeaconReading actualPosition;
  private       RotOp         toAbsolute;

  private final List<BeaconReading> readings;
  private final int                 threshold;

  public BeaconScanner(final int id, final List<BeaconReading> readings) {

    this(id, 12, readings);
  }

  public BeaconScanner(final int id, final int threshold, final List<BeaconReading> readings) {
    this.id = id;
    this.readings = new ArrayList<>(readings);
    this.threshold = threshold;
  }

  public List<BeaconReading[]> matchAndReorient(final BeaconScanner to) {

    if (!to.isAbsolute()) {
      //Don't reorient onto non-absolute beacons
      return Collections.emptyList();
    }

    if (to == this) {
      //Don't reorient onto itself
      return Collections.emptyList();
    }

    final var matches = findMatchedPairs(to);

    if (matches.isEmpty()) {
      return matches;
    }

    final var absolute = matches.get(0)[0];
    final var relative = matches.get(0)[1];

    final var diffOp     = findDiffOp(absolute, toAbsolute.getOp().apply(relative));
    final var completeOp = toAbsolute.getOp().andThen(diffOp);

    actualPosition = completeOp.apply(new BeaconReading(0, 0, 0));

    final var reoriented = readings.stream()
                                   .map(completeOp)
                                   .toList();

    readings.clear();
    readings.addAll(reoriented);

    return matches;

  }

  public List<BeaconReading[]> findMatchedPairs(final BeaconScanner absolute) {
    final var absVec = absolute.vectorisedBeacons();

    return ROTATIONS.stream()
                    .flatMap(r -> {
                      final List<BeaconReading[]> matches = tryGetMatches(absVec, r);
                      if (matches.size() >= threshold) {
                        setToAbsolute(r);
                        return matches.stream();
                      }
                      return Stream.empty();
                    })
                    .toList();

  }

  private List<BeaconReading[]> tryGetMatches(final Map<BeaconReading, List<BeaconReading>> absoluteVectors,
                                              final RotOp rotation) {
    return absoluteVectors.entrySet()
                          .stream()
                          .flatMap(e -> tryAgainstIndividualEntry(rotation, e))
                          .toList();
  }

  private Stream<BeaconReading[]> tryAgainstIndividualEntry(final RotOp rotation,
                                                            final Entry<BeaconReading, List<BeaconReading>> absoluteEntry) {
    return vectorisedBeacons().entrySet()
                              .stream()
                              .flatMap(entry -> attemptRotation(rotation,
                                                                absoluteEntry,
                                                                entry).stream());
  }

  private Optional<BeaconReading[]> attemptRotation(final RotOp rotation,
                                                    final Entry<BeaconReading, List<BeaconReading>> absolute,
                                                    final Entry<BeaconReading, List<BeaconReading>> relative) {
    final var rotated = relative.getValue()
                                .stream()
                                .map(rotation.getOp())
                                .collect(Collectors.toCollection(
                                    ArrayList::new));

    rotated.removeAll(absolute.getValue());

    if (abs(rotated.size() - relative.getValue().size()) >= threshold - 1) {
      //In other words if applying this operation has caused this items'
      //vectors to be correctly aligned with absolute's vectors
      return Optional.of(new BeaconReading[]{absolute.getKey(), relative.getKey()});
    }

    return Optional.empty();

  }


  public List<BeaconReading> getReadings() {
    return Collections.unmodifiableList(readings);
  }

  private Function<BeaconReading, BeaconReading> findDiffOp(final BeaconReading b1,
                                                            final BeaconReading b2) {

    final var xDiff = b1.getX() - b2.getX();
    final var yDiff = b1.getY() - b2.getY();
    final var zDiff = b1.getZ() - b2.getZ();

    return beacon -> new BeaconReading(beacon.getX() + xDiff,
                                       beacon.getY() + yDiff,
                                       beacon.getZ() + zDiff);

  }

  public boolean isAbsolute() {
    return id == 0 || actualPosition != null;
  }

  public void setToAbsolute(final RotOp toAbsolute) {
    if (isAbsolute() && toAbsolute == RotOp.A) {
      return;
    }

    if (this.toAbsolute == null) {
      this.toAbsolute = toAbsolute;
    } else {
      throw new IllegalStateException("Shouldn't try to set the rotation operation twice!");
    }
  }

  private Map<BeaconReading, List<BeaconReading>> vectorisedBeacons() {
    return readings.stream().collect(toMap(Function.identity(), this::vectorise));
  }

  public List<BeaconReading> vectorise(final BeaconReading reading) {
    return readings.stream()
                   .filter(not(reading::equals))
                   .map(reading::vectorise)
                   .toList();
  }


  public int getId() {
    return id;
  }

  public BeaconReading getActualPosition() {
    return id == 0
           ? new BeaconReading(0, 0, 0)
           : actualPosition;
  }

  private enum RotOp {
    A(BeaconReading::new),
    B(bc -> new BeaconReading(bc.getZ(), bc.getY(), bc.getX() * -1)),
    C(bc -> new BeaconReading(bc.getX() * -1, bc.getY(), bc.getZ() * -1)),
    D(bc -> new BeaconReading(bc.getZ() * -1, bc.getY(), bc.getX())),
    E(bc -> new BeaconReading(bc.getX(), bc.getZ(), bc.getY() * -1)),
    F(bc -> new BeaconReading(bc.getY() * -1, bc.getZ(), bc.getX() * -1)),
    G(bc -> new BeaconReading(bc.getX() * -1, bc.getZ(), bc.getY())),
    H(bc -> new BeaconReading(bc.getY(), bc.getZ(), bc.getX())),
    I(bc -> new BeaconReading(bc.getX() * -1, bc.getY() * -1, bc.getZ())),
    J(bc -> new BeaconReading(bc.getZ(), bc.getY() * -1, bc.getX())),
    K(bc -> new BeaconReading(bc.getX(), bc.getY() * -1, bc.getZ() * -1)),
    L(bc -> new BeaconReading(bc.getZ() * -1, bc.getY() * -1, bc.getX() * -1)),
    M(bc -> new BeaconReading(bc.getY(), bc.getZ() * -1, bc.getX() * -1)),
    N(bc -> new BeaconReading(bc.getX() * -1, bc.getZ() * -1, bc.getY() * -1)),
    O(bc -> new BeaconReading(bc.getY() * -1, bc.getZ() * -1, bc.getX())),
    P(bc -> new BeaconReading(bc.getX(), bc.getZ() * -1, bc.getY())),
    Q(bc -> new BeaconReading(bc.getY(), bc.getX() * -1, bc.getZ())),
    R(bc -> new BeaconReading(bc.getZ(), bc.getX() * -1, bc.getY() * -1)),
    S(bc -> new BeaconReading(bc.getY() * -1, bc.getX() * -1, bc.getZ() * -1)),
    T(bc -> new BeaconReading(bc.getZ() * -1, bc.getX() * -1, bc.getY())),
    U(bc -> new BeaconReading(bc.getZ(), bc.getX(), bc.getY())),
    V(bc -> new BeaconReading(bc.getY(), bc.getX(), bc.getZ() * -1)),
    W(bc -> new BeaconReading(bc.getZ() * -1, bc.getX(), bc.getY() * -1)),
    X(bc -> new BeaconReading(bc.getY() * -1, bc.getX(), bc.getZ()));

    private final Function<BeaconReading, BeaconReading> op;

    RotOp(final Function<BeaconReading, BeaconReading> op) {
      this.op = op;
    }

    public Function<BeaconReading, BeaconReading> getOp() {
      return op;
    }

  }

  @Override
  public String toString() {
    return new StringJoiner(", ", BeaconScanner.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("readings=" + readings)
        .toString();
  }
}
