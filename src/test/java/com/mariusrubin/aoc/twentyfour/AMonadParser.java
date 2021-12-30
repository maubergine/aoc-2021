package com.mariusrubin.aoc.twentyfour;

import com.mariusrubin.aoc.twentyfour.MonadRun.RunType;
import com.mariusrubin.aoc.util.FileLoader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class AMonadParser {

  private static final String      SAMPLE = "src/test/resources/com/mariusrubin/aoc/twentyfour/sample-monad.txt";
  private              MonadParser underTest;

  @BeforeEach
  public void init() {
    underTest = new MonadParser();
  }

  @Test
  public void shouldParseInput() {

    final var input  = new FileLoader(SAMPLE).allLines();
    final var output = underTest.parse(input);
    Assertions.assertThat(output).containsExactly(
        new MonadRun(RunType.PUSH, 6),
        new MonadRun(RunType.POP, -11)
    );

  }


}
