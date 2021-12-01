package uk.co.credera.aoc.util;

import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AFileLoader {

  private FileLoader underTest;

  private static final List<Integer> EXPECTED_INTS = Arrays.asList(1, 2, 3, 5, 8, 13);

  private static final List<String> EXPECTED_STR = EXPECTED_INTS.stream()
                                                                .map(String::valueOf)
                                                                .toList();


  @BeforeEach
  public void init() {
    underTest = new FileLoader("src/test/resources/uk/co/credera/aoc/util/load-sample.txt");
  }

  @Test
  public void shouldStreamText() {
    Assertions.assertThat(underTest.allLines()).containsExactlyElementsOf(EXPECTED_STR);
  }

  @Test
  public void shouldStreamIntegers() {
    Assertions.assertThat(underTest.integers()).containsExactlyElementsOf(EXPECTED_INTS);
  }

  @Test
  public void shouldReadAllIntegers() {
    Assertions.assertThat(underTest.allIntegers()).containsExactlyElementsOf(EXPECTED_INTS);
  }
}