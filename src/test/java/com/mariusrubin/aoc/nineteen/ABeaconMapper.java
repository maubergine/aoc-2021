package com.mariusrubin.aoc.nineteen;

import static org.assertj.core.api.Assertions.assertThat;

import com.mariusrubin.aoc.util.FileLoader;
import java.util.AbstractMap.SimpleEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ABeaconMapper {


  private static final String ORIENTATION_PATH = "src/test/resources/com/mariusrubin/aoc/nineteen/scanner-full-example.txt";
  private static final String FULL_LIST        = "src/test/resources/com/mariusrubin/aoc/nineteen/full-beacon-list.txt";

  private BeaconMapper underTest;

  @BeforeEach
  public void init() {
    underTest = new BeaconMapper(new FileLoader(ORIENTATION_PATH).allLines());
  }


  @Test
  public int getManhattanDistance() {
    return 0;
  }

  @Test
  public void shouldCountUniqueBeacons() {
    assertThat(underTest.countBeacons()).isEqualTo(79);
  }

  @Test
  public void shouldFindRelativePositions() {
    final var result = underTest.getRelativePositions();
    assertThat(result).containsExactly(
        new SimpleEntry<>(0, new BeaconReading(0, 0, 0)),
        new SimpleEntry<>(1, new BeaconReading(68, -1246, -43)),
        new SimpleEntry<>(2, new BeaconReading(1105, -1205, 1229)),
        new SimpleEntry<>(3, new BeaconReading(-92, -2380, -20)),
        new SimpleEntry<>(4, new BeaconReading(-20, -1133, 1061))
    );
  }

  @Test
  public void shouldFindAllBeacons() {
    final var result = underTest.allReadings()
                                .stream()
                                .map(BeaconReading::toString)
                                .map(reading -> reading.substring(1, reading.length() - 1))
                                .toList();

    assertThat(result).containsExactlyInAnyOrderElementsOf(new FileLoader(FULL_LIST).allLines());

  }

  @Test
  public void shouldCalculateLargestManhattanDistance() {
    assertThat(underTest.getLargestManhattanDistance()).isEqualTo(3621);
  }

}
