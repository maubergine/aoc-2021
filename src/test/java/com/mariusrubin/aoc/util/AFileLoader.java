package com.mariusrubin.aoc.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AFileLoader {

  private static final String STRING_PATH        = "src/test/resources/com/mariusrubin/aoc/util/load-sample.txt";
  private static final String BINARY_PATH        = "src/test/resources/com/mariusrubin/aoc/util/binary-load-sample.txt";
  private static final String INTEGER_PATH       = "src/test/resources/com/mariusrubin/aoc/util/integer-sample.txt";
  private static final String INTEGER_ARRAY_PATH = "src/test/resources/com/mariusrubin/aoc/util/integer-array-sample.txt";

  private static final List<Integer> EXPECTED_INTS = Arrays.asList(1, 2, 3, 5, 8, 13);
  private static final List<String>  EXPECTED_STR  = EXPECTED_INTS.stream()
                                                                  .map(String::valueOf)
                                                                  .toList();

  private FileLoader underTest;

  @BeforeEach
  public void init() {
    underTest = new FileLoader(STRING_PATH);
  }

  @Test
  public void shouldStreamText() {
    assertThat(underTest.allLines()).containsExactlyElementsOf(EXPECTED_STR);
  }

  @Test
  public void shouldStreamIntegers() {
    assertThat(underTest.integers()).containsExactlyElementsOf(EXPECTED_INTS);
  }

  @Test
  public void shouldReadAllIntegers() {
    assertThat(underTest.allIntegers()).containsExactlyElementsOf(EXPECTED_INTS);
  }

  @Test
  public void shouldReadAllBinaryIntegers() {
    final var loader = new FileLoader(BINARY_PATH);
    assertThat(loader.allBinaryIntegers()).containsExactlyElementsOf(EXPECTED_INTS);
  }

  @Test
  public void shouldReadCommaSeparatedIntegers() {
    final var loader = new FileLoader(INTEGER_PATH);
    assertThat(loader.allCommaSeparatedIntegers()).containsExactlyElementsOf(EXPECTED_INTS);
  }

  @Test
  public void shouldLoad2DIntegerArrays() {
    final var loader = new FileLoader(INTEGER_ARRAY_PATH);

    final var expected = new int[][]{
        {5, 4, 8, 3, 1, 4, 3, 2, 2, 3},
        {2, 7, 4, 5, 8, 5, 4, 7, 1, 1},
        {5, 2, 6, 4, 5, 5, 6, 1, 7, 3},
        {6, 1, 4, 1, 3, 3, 6, 1, 4, 6},
        {6, 3, 5, 7, 3, 8, 5, 4, 7, 8},
        {4, 1, 6, 7, 5, 2, 4, 6, 4, 5},
        {2, 1, 7, 6, 8, 4, 1, 7, 2, 1},
        {6, 8, 8, 2, 8, 8, 1, 1, 3, 4},
        {4, 8, 4, 6, 8, 4, 8, 5, 5, 4},
        {5, 2, 8, 3, 7, 5, 1, 5, 2, 6}
    };

    assertThat(loader.integerArray()).isEqualTo(expected);

  }
}