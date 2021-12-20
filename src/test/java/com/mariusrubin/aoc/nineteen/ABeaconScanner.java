package com.mariusrubin.aoc.nineteen;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ABeaconScanner {


  @Test
  public void shouldPredictablyVectorise() {
    final var scanner = new BeaconScanner(
        0,
        List.of(new BeaconReading(-1, -1, 1),
                new BeaconReading(-2, -2, 2),
                new BeaconReading(-3, -3, 3),
                new BeaconReading(-2, -3, 1),
                new BeaconReading(5, 6, -4),
                new BeaconReading(8, 0, 7)
        ));

    final var results = scanner.vectorise(new BeaconReading(-1, -1, 1));

    assertThat(results).containsExactly(
        new BeaconReading(1, 1, -1),
        new BeaconReading(2, 2, -2),
        new BeaconReading(1, 2, 0),
        new BeaconReading(-6, -7, 5),
        new BeaconReading(-9, -1, -6)
    );
  }

  @Test
  public void shouldRegisterOverlaps() {

    final var scanner = new BeaconScanner(
        1,
        5,
        List.of(new BeaconReading(-1, -1, 1),
                new BeaconReading(-2, -2, 2),
                new BeaconReading(-3, -3, 3),
                new BeaconReading(-2, -3, 1),
                new BeaconReading(5, 6, -4),
                new BeaconReading(8, 0, 7)
        ));

    final var other = new BeaconScanner(
        0,
        5,
        List.of(
            new BeaconReading(1, -1, 1),
            new BeaconReading(2, -2, 2),
            new BeaconReading(3, -3, 3),
            new BeaconReading(2, -1, 3),
            new BeaconReading(-5, 4, -6),
            new BeaconReading(-8, -7, 0)
        ));

    final var matches = scanner.matchAndReorient(other);

    assertThat(matches).containsExactlyInAnyOrder(
        new BeaconReading[]{new BeaconReading(1, -1, 1), new BeaconReading(-1, -1, 1)},
        new BeaconReading[]{new BeaconReading(2, -2, 2), new BeaconReading(-2, -2, 2)},
        new BeaconReading[]{new BeaconReading(3, -3, 3), new BeaconReading(-3, -3, 3)},
        new BeaconReading[]{new BeaconReading(2, -1, 3), new BeaconReading(-2, -3, 1)},
        new BeaconReading[]{new BeaconReading(-5, 4, -6), new BeaconReading(5, 6, -4)},
        new BeaconReading[]{new BeaconReading(-8, -7, 0), new BeaconReading(8, 0, 7)}
    );

  }

  @Test
  public void shouldDetectAndReorient() {
    final var absolute = new BeaconScanner(
        0,
        5,
        List.of(new BeaconReading(-1, -1, 1),
                new BeaconReading(-2, -2, 2),
                new BeaconReading(-3, -3, 3),
                new BeaconReading(-2, -3, 1),
                new BeaconReading(5, 6, -4),
                new BeaconReading(8, 0, 7)
        ));
    final var one = new BeaconScanner(
        1,
        5,
        List.of(new BeaconReading(1, -1, 1),
                new BeaconReading(2, -2, 2),
                new BeaconReading(3, -3, 3),
                new BeaconReading(2, -1, 3),
                new BeaconReading(-5, 4, -6),
                new BeaconReading(-8, -7, 0)
        ));
    final var two = new BeaconScanner(
        2,
        5,
        List.of(new BeaconReading(-1, -1, -1),
                new BeaconReading(-2, -2, -2),
                new BeaconReading(-3, -3, -3),
                new BeaconReading(-1, -3, -2),
                new BeaconReading(4, 6, 5),
                new BeaconReading(-7, 0, 8)
        ));
    final var three = new BeaconScanner(
        3,
        5,
        List.of(new BeaconReading(1, 1, -1),
                new BeaconReading(2, 2, -2),
                new BeaconReading(3, 3, -3),
                new BeaconReading(1, 3, -2),
                new BeaconReading(-4, -6, 5),
                new BeaconReading(7, 0, 8)
        ));
    final var four = new BeaconScanner(
        4,
        5,
        List.of(new BeaconReading(1, 1, 1),
                new BeaconReading(2, 2, 2),
                new BeaconReading(3, 3, 3),
                new BeaconReading(3, 1, 2),
                new BeaconReading(-6, -4, -5),
                new BeaconReading(0, 7, -8)
        ));

    one.matchAndReorient(absolute);
    assertThat(one.getReadings()).containsExactlyElementsOf(absolute.getReadings());

    two.matchAndReorient(absolute);
    assertThat(two.getReadings()).containsExactlyElementsOf(absolute.getReadings());

    three.matchAndReorient(two);
    assertThat(three.getReadings()).containsExactlyElementsOf(absolute.getReadings());

    four.matchAndReorient(three);
    assertThat(four.getReadings()).containsExactlyElementsOf(absolute.getReadings());

  }


}
