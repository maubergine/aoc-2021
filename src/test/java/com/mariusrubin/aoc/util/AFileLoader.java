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

  private static final String BINARY_PATH = "src/test/resources/com/mariusrubin/aoc/util/binary-load-sample.txt";

  private static final List<Integer> EXPECTED_INTS = Arrays.asList(1, 2, 3, 5, 8, 13);
  private static final List<String>  EXPECTED_STR  = EXPECTED_INTS.stream()
                                                                  .map(String::valueOf)
                                                                  .toList();

  private FileLoader underTest;

  @BeforeEach
  public void init() {
    underTest = new FileLoader("src/test/resources/com/mariusrubin/aoc/util/load-sample.txt");
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
    final FileLoader binaryLoader = new FileLoader(BINARY_PATH);
    assertThat(binaryLoader.allBinaryIntegers()).containsExactlyElementsOf(EXPECTED_INTS);
  }
}