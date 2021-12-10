package com.mariusrubin.aoc.ten;

import com.mariusrubin.aoc.ten.NavigationParser.IllegalCharacter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Marius Rubin
 * @since 0.1.0
 */
public class ANavigationParser {

  private static final String INPUT = """
      [({(<(())[]>[[{[]{<()<>>
      [(()[<>])]({[<{<<[]>>(
      {([(<{}[<>[]}>{[]{[(<()>
      (((({<>}<{<{<>}{[]{[]{}
      [[<[([]))<([[{}[[()]]]
      [{[{({}]{}}([{[{{{}}([]
      {<[[]]>}<{[{[{[]{()[[[]
      [<(<(<(<{}))><([]([]()
      <{([([[(<>()){}]>(<<{{
      <{([{{}}[<[[[<>{}]]]>[]]""";

  private NavigationParser underTest;

  @BeforeEach
  public void init() {
    underTest = new NavigationParser(INPUT.lines().toList());
  }

  @Test
  public void shouldFindIllegalCharacters() {
    final var illegalChars = underTest.findIllegalCharacters();
    Assertions.assertThat(illegalChars).contains(
        new IllegalCharacter(']', '}', 12, 2),
        new IllegalCharacter(']', ')', 8, 4),
        new IllegalCharacter(')', ']', 7, 5),
        new IllegalCharacter('>', ')', 10, 7),
        new IllegalCharacter(']', '>', 16, 8)
    );
  }

  @Test
  public void shouldCalculateIllegalityScore() {
    Assertions.assertThat(underTest.calculateIllegalityScore()).isEqualTo(26397);
  }

  @Test
  public void shouldCompleteLines() {
    final var completedLines = underTest.getCompletedLines();
    Assertions.assertThat(completedLines).contains(
        "[({(<(())[]>[[{[]{<()<>>}}]])})]",
        "[(()[<>])]({[<{<<[]>>()}>]})",
        "(((({<>}<{<{<>}{[]{[]{}}}>}>))))",
        "{<[[]]>}<{[{[{[]{()[[[]]]}}]}]}>",
        "<{([{{}}[<[[[<>{}]]]>[]]])}>");
  }

  @Test
  public void shouldCalculateCompletionScore() {
    Assertions.assertThat(underTest.calculateCompletionScore()).isEqualTo(288957L);
  }

}
