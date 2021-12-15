package com.mariusrubin.aoc.fifteen;

import static org.assertj.core.api.Assertions.assertThat;

import com.mariusrubin.aoc.util.FileLoader;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ACavernScanner {

  private static final String INPUT_PATH    = "src/test/resources/com/mariusrubin/aoc/fifteen/ScannerInput.txt";
  private static final String EXPECTED_PATH = "src/test/resources/com/mariusrubin/aoc/fifteen/ExpectedLongRangeOutput.txt";

  private static final int[][] EXPECTED_ARRAY = {
      {1, 1, 6, 3, 7, 5, 1, 7, 4, 2},
      {1, 3, 8, 1, 3, 7, 3, 6, 7, 2},
      {2, 1, 3, 6, 5, 1, 1, 3, 2, 8},
      {3, 6, 9, 4, 9, 3, 1, 5, 6, 9},
      {7, 4, 6, 3, 4, 1, 7, 1, 1, 1},
      {1, 3, 1, 9, 1, 2, 8, 1, 3, 7},
      {1, 3, 5, 9, 9, 1, 2, 4, 2, 1},
      {3, 1, 2, 5, 4, 2, 1, 6, 3, 9},
      {1, 2, 9, 3, 1, 3, 8, 5, 2, 1},
      {2, 3, 1, 1, 9, 4, 4, 5, 8, 1}
  };

  @Test
  public void shouldScanShortRange() {
    final var scanner = new CavernScanner(1);
    assertThat(scanner.scanCavern(INPUT_PATH)).isDeepEqualTo(EXPECTED_ARRAY);
  }

  @Test
  public void shouldScanLongRange() {
    final var scanner  = new CavernScanner(5);
    final var expected = new FileLoader(EXPECTED_PATH).integerArray();
    assertThat(scanner.scanCavern(INPUT_PATH)).isDeepEqualTo(expected);
  }

}